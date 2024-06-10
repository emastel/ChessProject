package client;

import dataaccess.DataAccessException;
import dataaccess.SqlAuthDAO;
import dataaccess.SqlGameDAO;
import dataaccess.SqlUserDAO;
import org.junit.jupiter.api.*;
import server.Server;
import ui.Gameplay;
import net.ServerFacade;

import java.sql.SQLException;


public class ServerFacadeTests {

    private static Server server;
    private static SqlUserDAO users;
    private static ServerFacade serverFacade;
    private static SqlAuthDAO auths;
    private static SqlGameDAO games;

    @BeforeAll
    public static void init() throws DataAccessException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        users = new SqlUserDAO();
        auths = new SqlAuthDAO();
        games = new SqlGameDAO();
        serverFacade = new ServerFacade(port);
    }

    @AfterEach
    public void clear() throws DataAccessException {
        users.clear();
        auths.clear();
        games.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void testBoard() {
        Gameplay.main();
    }

    @Test
    public void testRegister() throws SQLException {
        serverFacade.register("username", "password", "email");
        String username = users.getElement("username", "username");
        Assertions.assertEquals(username, "username");
    }

    @Test
    public void failRegister() throws SQLException {
        serverFacade.register("username", "password", "email");
        String username = users.getElement("invalid", "username");
        Assertions.assertNull(username);
    }

    @Test
    public void testLogin() throws SQLException {
        serverFacade.register("username", "password", "email");
        serverFacade.login("username", "password");
        Assertions.assertNotNull(auths.getToken("username"));
    }

    @Test
    public void badLogin() {
        serverFacade.register("username", "password", "email");
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            serverFacade.login("invalidUsername", "invalidPassword");
        });

        String expectedMessage = "failure: 401";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testLogout() throws SQLException {
        serverFacade.register("username", "password", "email");
        serverFacade.login("username", "password");
        String token = auths.getToken("username");
        serverFacade.logout(token);
        Assertions.assertNull(auths.getUser(token));
    }

    @Test
    public void badLogout() throws SQLException {
        serverFacade.register("username", "password", "email");
        serverFacade.login("username", "password");
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            serverFacade.logout("wrong");
        });

        String expectedMessage = "failure: 401";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCreateGame() throws SQLException, DataAccessException {
        serverFacade.register("username", "password", "email");
        //serverFacade.login("username", "password");
        String token = auths.getToken("username");
        serverFacade.createGame("game", token);
        Assertions.assertNotNull(games.getGame(1));
    }



    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
