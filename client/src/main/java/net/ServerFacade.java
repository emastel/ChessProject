package net;

import com.google.gson.Gson;
import exception.ResponseException;
import requestResponse.*;

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
            throwIfNotSuccessful(connection);
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
            throwIfNotSuccessful(connection);
            return readBody(connection, RegisterLoginResponse.class);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void logout(String authToken) {
        var path = "/session";
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setDoOutput(true);

            connection.addRequestProperty("Authorization", authToken);
            AuthTokenRequest request = new AuthTokenRequest(authToken);
            writeBody(request,connection);
            connection.connect();
            throwIfNotSuccessful(connection);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ListGamesResponse listGames(String authToken) {
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
            throwIfNotSuccessful(connection);
            return readBody(connection, ListGamesResponse.class);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void joinGame(String authToken) {
        var path = "/game";
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);

            connection.addRequestProperty("Authorization", authToken);
            AuthTokenRequest request = new AuthTokenRequest(authToken);
            writeBody(request,connection);
            connection.connect();
            throwIfNotSuccessful(connection);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public CreateGameResponse createGame(String name, String authToken) {
        var path = "/game";
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            connection.addRequestProperty("Authorization", authToken);
            CreateGameRequest request = new CreateGameRequest(authToken,name);
            writeBody(request,connection);
            connection.connect();
            throwIfNotSuccessful(connection);
            return readBody(connection, CreateGameResponse.class);

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
            try(InputStream resBody = connection.getInputStream()) {
                InputStreamReader bodyReader = new InputStreamReader(resBody);
                response = new Gson().fromJson(bodyReader, responseClass);
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }


}


