package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AuthDAO {

    private static Map<String, AuthData> authTokens = new HashMap<>();

    public void clearAuth() {
        if(!authTokens.isEmpty() && authTokens != null) {
            authTokens.clear();
        }
    }

    public void createAuth(AuthData input) {
        authTokens.put(input.username(),input);
    }

    public AuthData getAuth(String authToken) {
        for(Map.Entry<String, AuthData> entry : authTokens.entrySet()) {
            if(Objects.equals(entry.getValue().authToken(), authToken)) {
                return entry.getValue();
            }
        }
        return authTokens.get(authToken);
    }

    public void deleteAuth(String username, AuthData input) {
        authTokens.remove(username, input);
    }
}
