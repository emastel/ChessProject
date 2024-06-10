package server.handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.GameName;
import model.UserData;
import reqrep.*;
import spark.Request;
import spark.Response;
import unit.AuthService;
import unit.GameService;
import unit.UserService;

public class Handler {

    private static UserService userService = new UserService();

    private static GameService gameService = new GameService();

    private static AuthService authService = new AuthService();

    public static Gson gson = new Gson();

    public static String clearRequest(Request req, Response res) {
        try {
            userService.clearUsers();
            gameService.clearGames();
            authService.clearAuth();
        }
        catch (Exception e) {
            BlankResponse result = new BlankResponse(e.getMessage());
            res.status(500);
            return gson.toJson(result);
        }
        res.status(200);
        return "null";
    }

    public static String registerRequest(Request req, Response res) {
        RegisterRequest regReq = gson.fromJson(req.body(), RegisterRequest.class);
        RegisterLoginResponse result = null;
        //String hashedPassword = BCrypt.hashpw(regReq.password(), BCrypt.gensalt());
        try {
            if(regReq.password() == null) {
                throw new DataAccessException("Error: bad request");
            }
            UserData user = new UserData(regReq.username(), regReq.password(), regReq.email());
            AuthData retrievedAuthData = userService.register(user);
            result = new RegisterLoginResponse(null,retrievedAuthData.username(),retrievedAuthData.authToken());
            res.status(200);
        }
        catch (Exception e) {
            if(e.getMessage().equals("Error: already taken")) {
                res.status(403);
            }
            else if (e.getMessage().equals("Error: bad request")) {
                res.status(400);
            }
            else {
                res.status(500);
            }
            result = new RegisterLoginResponse(e.getMessage(),null,null);
        }
        return gson.toJson(result);
    }

    private static BlankResponse unauthorizedError(Response res, Exception e) {
        if(e.getMessage().equals("Error: unauthorized")) {
            res.status(401);
        }
        else {
            res.status(500);
        }
         return new BlankResponse(e.getMessage());
    }

    public static String loginRequest(Request req, Response res) {
        LoginRequest regReq = gson.fromJson(req.body(), LoginRequest.class);
        try {
            UserData user = new UserData(regReq.username(), regReq.password(), null);
            AuthData retrievedAuthData = userService.login(user);
            RegisterLoginResponse result = new RegisterLoginResponse(null,retrievedAuthData.username(),retrievedAuthData.authToken());
            res.status(200);
            return gson.toJson(result);
        }
        catch (Exception e) {
            BlankResponse result = unauthorizedError(res, e);
            return gson.toJson(result);
        }

    }

    public static String logoutRequest(Request req, Response res) {
        String authToken = req.headers("authorization");
        try {
            userService.logout(authToken);
        }
        catch(Exception e) {
            BlankResponse result = unauthorizedError(res, e);
            return gson.toJson(result);
        }
        res.status(200);
        return "null";
    }

    public static String listGamesRequest(Request req, Response res) {
        String authToken = req.headers("authorization");
        try {
            GameData[] games = gameService.listGames(authToken);
            ListGamesResponse result = new ListGamesResponse(games);
            return gson.toJson(result);
        }
        catch (Exception e) {
            BlankResponse result = unauthorizedError(res, e);
            return gson.toJson(result);
        }
    }

    public static String createGameRequest(Request req, Response res) {
        String authToken = req.headers("Authorization");
        GameName gameName = gson.fromJson(req.body(), GameName.class);
        try {
            GameData game = gameService.createGame(authToken,gameName.gameName());
            CreateGameResponse result = new CreateGameResponse(null, game.getGameID());
            return gson.toJson(result);
        }
        catch (Exception e) {
            if(e.getMessage().equals("Error: unauthorized")) {
                res.status(401);
            }
            else if (e.getMessage().equals("Error: bad request")) {
                res.status(400);
            }
            else {
                res.status(500);
            }
            BlankResponse result = new BlankResponse(e.getMessage());
            return gson.toJson(result);
        }
    }

    public static String joinGameRequest(Request req, Response res) {
        String authToken = req.headers("authorization");
        JoinGameRequest gameReq = gson.fromJson(req.body(), JoinGameRequest.class);
        try {
            gameService.joinGame(gameReq.playerColor(), gameReq.gameID(),authToken);
            res.status(200);
            return "null";
        }
        catch (Exception e) {
            if(e.getMessage().equals("Error: unauthorized")) {
                res.status(401);
            }
            else if (e.getMessage().equals("Error: bad request")) {
                res.status(400);
            }
            else if (e.getMessage().equals("Error: already taken")) {
                res.status(403);
            }
            else  {
                res.status(500);
            }
            BlankResponse result = new BlankResponse(e.getMessage());
            return gson.toJson(result);
        }
    }
}
