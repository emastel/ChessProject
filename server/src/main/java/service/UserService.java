package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

public class UserService {

    private UserDAO userDAO;

    private AuthDAO authDAO;

    public AuthData register(UserData user) throws DataAccessException {
        UserData retrievedUser = userDAO.getUser(user.username());
        if(Objects.equals(retrievedUser.password(), user.password())) {
            throw new DataAccessException("Error: already taken");
        }
        userDAO.createUser(user);
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(retrievedUser.username(), authToken);
        authDAO.createAuth(authData);
        return authData;
    }

    public AuthData login(UserData user) throws DataAccessException {
        UserData retrievedUser = userDAO.getUser(user.username());
        if(retrievedUser == null) {
            throw new DataAccessException("Error: user not found");
        }
        if(!Objects.equals(retrievedUser.password(), user.password())) {
            throw new DataAccessException("Error: unauthorized");
        }
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(retrievedUser.username(), authToken);
        authDAO.createAuth(authData);
        return authData;
    }

    public void logout(UserData user) throws DataAccessException {
        AuthData retrievedAuth = authDAO.getAuth(user.username());
        authDAO.deleteAuth(user.username(), retrievedAuth);
    }

}
