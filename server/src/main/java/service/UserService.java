package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    private AuthDAO authDAO = new AuthDAO();

    public AuthData register(UserData user) throws AlreadyTakenException {
        UserData retrievedUser = userDAO.getUser(user.username());
        if(retrievedUser != null && Objects.equals(retrievedUser.password(), user.password())) {
            throw new AlreadyTakenException("Error: already taken");
        }
        userDAO.createUser(user);
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, user.username());
        authDAO.createAuth(authData);
        return authData;
    }

    public AuthData login(UserData user) throws UnauthorizedException {
        UserData retrievedUser = userDAO.getUser(user.username());
        if(retrievedUser == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if(!Objects.equals(retrievedUser.password(), user.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, retrievedUser.username());
        authDAO.createAuth(authData);
        return authData;
    }

    public void logout(String authToken) throws UnauthorizedException {
        AuthData retrievedUser = authDAO.getAuth(authToken);
        if(retrievedUser == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        authDAO.deleteAuth(retrievedUser.username(), retrievedUser);
    }

    public void clearUsers() {
        userDAO.clear();
    }
}
