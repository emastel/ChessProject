package dataaccess;

import model.AuthData;

import java.util.*;

public class AuthDAO {

    private static Map<String, List<AuthData>> authTokens = new HashMap<>();
    private static List<AuthData> tokens = new ArrayList<>();

    public void clearAuth() {
        if(!authTokens.isEmpty()) {
            authTokens.clear();
        }
    }

    public void createAuth(AuthData input) {
        tokens.add(input);
        authTokens.put(input.username(),tokens);
    }

    public void deleteAuth(String username, AuthData input) {
        tokens.remove(input);
        authTokens.remove(username);
        authTokens.put(input.username(),tokens);
    }
}
