package net;

import com.google.gson.Gson;
import model.GameName;
import reqrep.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(int port) {
        serverUrl = "http://localhost:" + Integer.toString(port);
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public RegisterLoginResponse register(String username, String password, String email) {
        var path = "/user";
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            RegisterRequest request = new RegisterRequest(username, password, email);
            writeBody(request,connection);
            connection.connect();
            //throwIfNotSuccessful(connection);
            return readBody(connection, RegisterLoginResponse.class);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public RegisterLoginResponse login(String username, String password) {
        var path = "/session";
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            LoginRequest request = new LoginRequest(username, password);
            writeBody(request,connection);
            connection.connect();
            //throwIfNotSuccessful(connection);
            return readBody(connection, RegisterLoginResponse.class);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public BlankResponse logout(String authToken) {
        var path = "/session";
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setDoOutput(true);

            connection.addRequestProperty("Authorization", authToken);
            connection.connect();
            //throwIfNotSuccessful(connection);
            return readBody(connection, BlankResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Object listGames(String authToken) {
        var path = "/game";
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            connection.addRequestProperty("Authorization", authToken);
            AuthTokenRequest request = new AuthTokenRequest(authToken);
            //writeBody(request,connection);
            connection.connect();
            //throwIfNotSuccessful(connection);
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return readBody(connection, ListGamesResponse.class);
            }
            else {
                return readBody(connection, BlankResponse.class);
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public BlankResponse joinGame(String authToken, String color, int gameId) {
        var path = "/game";
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);

            connection.addRequestProperty("Authorization", authToken);
            JoinGameRequest request = new JoinGameRequest(color, gameId);
            //AuthTokenRequest request = new AuthTokenRequest(authToken);
            writeBody(request,connection);
            connection.connect();
            //throwIfNotSuccessful(connection);
            return readBody(connection, BlankResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Object createGame(String name, String authToken) {
        var path = "/game";
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            connection.addRequestProperty("Authorization", authToken);
            GameName request = new GameName(name);
            writeBody(request,connection);
            connection.connect();
            //throwIfNotSuccessful(connection);
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return readBody(connection, CreateGameResponse.class);
            }
            else {
                return readBody(connection, BlankResponse.class);
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection connection) throws IOException {
        connection.setRequestProperty("Content-Type", "application/json");
        String reqData = new Gson().toJson(request);
        try(OutputStream reqBody = connection.getOutputStream()) {
            reqBody.write(reqData.getBytes());
        }
    }

    private static <T> T readBody(HttpURLConnection connection, Class<T> responseClass) throws IOException {
        T response = null;
        if(connection.getContentLength() < 0) {
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try(InputStream resBody = connection.getInputStream()) {
                    InputStreamReader bodyReader = new InputStreamReader(resBody);
                    response = new Gson().fromJson(bodyReader, responseClass);
                }
            }
            else {
                try(InputStream resBody = connection.getErrorStream()) {
                    InputStreamReader bodyReader = new InputStreamReader(resBody);
                    response = new Gson().fromJson(bodyReader, responseClass);
                }
            }

        }
        return response;
    }



}


