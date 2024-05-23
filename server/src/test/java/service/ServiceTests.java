package service;

import dataaccess.AuthDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class ServiceTests {

    @Test
    @Order(1)
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
    @Order(2)
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
    @Order(2)
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


    private void assertUsername(String expected, String actual) {
        Assertions.assertEquals(expected, actual, "username is incorrect");
    }


    private UserService userService = new UserService();

    private AuthDAO authDAO = new AuthDAO();


}
