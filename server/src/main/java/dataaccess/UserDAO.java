package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDAO {

    private static Map<String, UserData> users = new HashMap<>();

    public void clear() {
        users.clear();
    }

    public void createUser(UserData input) {
        users.put(input.username(), input);
    }

    public UserData getUser(String username) {
        return users.get(username);
    }
}
