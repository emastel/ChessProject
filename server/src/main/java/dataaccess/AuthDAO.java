package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class AuthDAO {

    private Map<String, AuthData> authTokens = new HashMap<>();

    public void clearAuth() {
        authTokens.clear();
    }

    public void createAuth(AuthData input) {
        authTokens.put(input.username(),input);
    }

    public AuthData getAuth(String username) {
        return authTokens.get(username);
    }

    public void deleteAuth(String username) {
        authTokens.remove(username);
    }
}
