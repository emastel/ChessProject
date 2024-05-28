package service;

import dataaccess.AuthDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ServiceTests {

    @Test
    @DisplayName("Register")
    public void testRegister() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            AuthData testResult = userService.register(user);
            assertUsername(user.username(),testResult.username());
        }
        catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
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

    @Test
    @DisplayName("Login")
    public void testLogin() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            userService.register(user);
            AuthData testResult = userService.login(user);
            assertUsername(user.username(),testResult.username());
        }
        catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Bad Login")
    public void badLogin() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            userService.register(user);
            UserData badUser = new UserData("not", "the", "same");
            userService.login(badUser);
        }
        catch (Exception e) {
            assertUnauthorized(e.getMessage());
        }
    }

    @Test
    @DisplayName("Logout")
    public void testLogout() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            userService.register(user);
            AuthData testResult = userService.login(user);
            userService.logout(testResult.authToken());
            Assertions.assertNull(authDAO.getAuth(testResult.authToken()));
        }
        catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Bad Logout")
    public void badLogout() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            userService.register(user);
            userService.login(user);
            userService.logout("notAnAuthtoken");
        }
        catch (Exception e) {
            assertUnauthorized(e.getMessage());
        }
    }

    @Test
    @DisplayName("Create Games")
    public void testCreateGames() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            userService.register(user);
            AuthData authData = userService.login(user);
            GameData gameResult = gameService.createGame(authData.authToken(),"testGame");
            assertGame("testGame",gameResult.getGameName());
        }
        catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Bad Create Games")
    public void badCreateGames() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            userService.register(user);
            userService.login(user);
            gameService.createGame("notAnAuthToken","testGame");
        }
        catch (Exception e) {
            assertUnauthorized(e.getMessage());
        }
    }

    @Test
    @DisplayName("List Games")
    public void testListGames() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            userService.register(user);
            AuthData authData = userService.login(user);
            gameService.createGame(authData.authToken(),"testGame");
            gameService.listGames(authData.authToken());
        }
        catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Bad List Games")
    public void badListGames() {
        UserData user = new UserData("testUser", "testPassword", "testemail@gmail.com");
        try {
            userService.register(user);
            AuthData authData = userService.login(user);
            gameService.createGame(authData.authToken(),"testGame");
            gameService.listGames("notAnAuthToken");
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

    private AuthDAO authDAO = new AuthDAO();


}
