package server.Handlers;

import dataaccess.DataAccessException;
import service.AuthService;
import service.GameService;
import service.UserService;

public class Handler {

    private UserService userService;

    private GameService gameService;

    private AuthService authService;

    public ClearResponse clear() {
       userService.clearUsers();
       gameService.clearGames();
       authService.clearAuth();
    }



//    public RegisterLoginResult register(RegisterRequest input) {
//        UserData user = new UserData(input.username(), input.password(), input.email());
//        AuthData retrievedAuthData = null;
//        try {
//            retrievedAuthData = userService.register(user);
//            return new RegisterLoginResult(retrievedAuthData.username(),retrievedAuthData.authToken());
//        }
//        catch (DataAccessException e) {
//
//        }
//    }
//
//    public RegisterLoginResult login(LoginRequest input) {
//        UserData user = new UserData(input.username(), input.password(), null);
//        AuthData retrievedAuthData = null;
//        try {
//            retrievedAuthData = userService.login(user);
//            return new RegisterLoginResult(retrievedAuthData.username(),retrievedAuthData.authToken());
//        }
//        catch (DataAccessException e) {
//
//        }
//    }
//
//    public void logout(AuthTokenRequest input) {
//        AuthData retrievedAuthData = null;
//        try {
//            userService.logout(input.authToken());
//        }
//        catch (DataAccessException e) {
//
//        }
//    }
}
