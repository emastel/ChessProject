package unit;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

public class ServiceTests {

    @Test
    @Order(1)
    @DisplayName("Register")
    public void testRegister() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            userService.clearUsers();
            AuthData testResult = userService.register(user);
            assertUsername(user.username(),testResult.username());
        }
        catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(2)
    @DisplayName("Fail Register")
    public void failRegister() {
        UserData user = new UserData(null, null, null);
        try {
            userService.register(user);
        }
        catch (Exception e) {
            assertBadRequest("Error: bad request",e.getMessage());
        }
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        userService.clearUsers();
        gameService.clearGames();
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            userService.register(user);
        }
        catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(3)
    @DisplayName("Login")
    public void testLogin() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            AuthData testResult = userService.login(user);
            assertUsername(user.username(),testResult.username());
        }
        catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(4)
    @DisplayName("Bad Login")
    public void badLogin() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            UserData badUser = new UserData("not", "the", "same");
            userService.login(badUser);
        }
        catch (Exception e) {
            assertUnauthorized(e.getMessage());
        }
    }

    @Test
    @Order(5)
    @DisplayName("Logout")
    public void testLogout() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            AuthData testResult = userService.login(user);
            userService.logout(testResult.authToken());
            Assertions.assertNull(authDAO.getUser(testResult.authToken()));
        }
        catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(6)
    @DisplayName("Bad Logout")
    public void badLogout() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            userService.login(user);
            userService.logout("notAnAuthtoken");
        }
        catch (Exception e) {
            assertUnauthorized(e.getMessage());
        }
    }

    @Test
    @Order(7)
    @DisplayName("Create Games")
    public void testCreateGames() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            AuthData authData = userService.login(user);
            GameData gameResult = gameService.createGame(authData.authToken(),"testGame");
            assertGame("testGame",gameResult.getGameName());
        }
        catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(8)
    @DisplayName("Bad Create Games")
    public void badCreateGames() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            gameService.createGame("notAnAuthToken","testGame");
        }
        catch (Exception e) {
            assertUnauthorized(e.getMessage());
        }
    }

    @Test
    @Order(9)
    @DisplayName("List Games")
    public void testListGames() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            AuthData authData = userService.login(user);
            gameService.createGame(authData.authToken(),"testGame");
            gameService.listGames(authData.authToken());
        }
        catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(10)
    @DisplayName("Bad List Games")
    public void badListGames() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            AuthData authData = userService.login(user);
            gameService.createGame(authData.authToken(),"testGame");
            gameService.listGames("notAnAuthToken");
        }
        catch (Exception e) {
            assertUnauthorized(e.getMessage());
        }
    }

    @Test
    @Order(11)
    @DisplayName("User Clear")
    public void testUserClear() {
        try {
            userService.clearUsers();
        }
        catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(12)
    @DisplayName("Game Clear")
    public void testGameClear() {
        try {
            gameService.clearGames();
        }
        catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(13)
    @DisplayName("Join Game")
    public void testJoinGame() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            AuthData authData = userService.login(user);
            GameData gameResult = gameService.createGame(authData.authToken(),"testGame");
            gameService.joinGame("WHITE", gameResult.getGameID(),authData.authToken());
            GameData joinedGame = gameDAO.getGame(gameResult.getGameID());
            assertUsername("testUser", joinedGame.getWhiteUsername());
        }
        catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(14)
    @DisplayName("Bad Join Game")
    public void badJoinGame() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            AuthData authData = userService.login(user);
            GameData gameResult = gameService.createGame(authData.authToken(),"testGame");
            gameService.joinGame("WHITE", gameResult.getGameID(),"invalid");
        }
        catch (Exception e) {
            assertUnauthorized(e.getMessage());
        }
    }




    private void assertUsername(String expected, String actual) {
        Assertions.assertEquals(expected, actual, "username is incorrect");
    }

    private void assertGame(String expected, String actual) {
        Assertions.assertEquals(expected, actual, "game is incorrect");
    }

    private void assertBadRequest(String expected, String actual) {
        Assertions.assertEquals(expected, actual, "different error");
    }

    private void assertUnauthorized(String actual) {
        Assertions.assertEquals("Error: unauthorized", actual, "different error");
    }



    private UserService userService = new UserService();

    private GameService gameService = new GameService();

    private SqlGameDAO gameDAO;
    private SqlAuthDAO authDAO;

    public ServiceTests() {
        try {
            authDAO = new SqlAuthDAO();
            gameDAO = new SqlGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
