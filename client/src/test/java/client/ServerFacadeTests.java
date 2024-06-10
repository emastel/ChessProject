package client;

import dataaccess.DataAccessException;
import dataaccess.SqlAuthDAO;
import dataaccess.SqlGameDAO;
import dataaccess.SqlUserDAO;
import model.GameData;
import net.ServerFacade;
import org.junit.jupiter.api.*;
import reqrep.ListGamesResponse;
import server.Server;
import ui.Client;
import ui.Gameplay;

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
    public void testClient() {
        Client client = new Client();
        client.eval("help");
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
        //serverFacade.login("username", "password");
        String token = auths.getToken("username");
        serverFacade.logout(token);
        Assertions.assertNull(auths.getUser(token));
    }

    @Test
    public void badLogout() throws SQLException {
        serverFacade.register("username", "password", "email");
        //serverFacade.login("username", "password");
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
        int id = games.getID("game");
        Assertions.assertNotNull(games.getGame(id));
    }

    @Test
    public void badCreateGame() throws SQLException, DataAccessException {
        serverFacade.register("username", "password", "email");
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            serverFacade.createGame("game", "wrong");
        });

        String expectedMessage = "failure: 401";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testListGame() throws SQLException, DataAccessException {
        serverFacade.register("username", "password", "email");
        //serverFacade.login("username", "password");
        String token = auths.getToken("username");
        serverFacade.createGame("game1", token);
        serverFacade.createGame("game2", token);
        serverFacade.createGame("game3", token);
        Object games = serverFacade.listGames(token);
        GameData[] list = ((ListGamesResponse)games).games();
        Assertions.assertEquals(3, list.length);

    }

    @Test
    public void badListGame() throws SQLException, DataAccessException {
        serverFacade.register("username", "password", "email");
        //serverFacade.login("username", "password");
        String token = auths.getToken("username");
        serverFacade.createGame("game1", token);
        serverFacade.createGame("game2", token);
        serverFacade.createGame("game3", token);
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            serverFacade.listGames("wrong");
        });

        String expectedMessage = "failure: 401";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void testJoinGame() throws SQLException, DataAccessException {
        serverFacade.register("username", "password", "email");
        //serverFacade.login("username", "password");
        String token = auths.getToken("username");
        serverFacade.createGame("game1", token);
        int id = games.getID("game1");
        serverFacade.joinGame(token, "BLACK", id);
        String actual = games.getGame(id).getWhiteUsername();
        Assertions.assertEquals("username", actual);
    }

    @Test
    public void badJoinGame() throws SQLException, DataAccessException {
        serverFacade.register("username", "password", "email");
        //serverFacade.login("username", "password");
        String token = auths.getToken("username");
        serverFacade.createGame("game1", token);
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            serverFacade.joinGame("wrong","BLACK",1);
        });

        String expectedMessage = "failure: 401";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }



    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
