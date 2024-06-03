package unit;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class UserService {

//    private UserDAO userDAO = new UserDAO();
//
//    private AuthDAO authDAO = new AuthDAO();

    private SqlUserDAO userDAO;


    private SqlAuthDAO authDAO;

    public UserService() {
        try {
            userDAO = new SqlUserDAO();
            authDAO = new SqlAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthData register(UserData user) throws AlreadyTakenException, BadRequestException, SQLException {
        if(user.username() == null || user.password() == null || user.username().isEmpty() || user.password().isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        String retrievedPassword = userDAO.getElement(user.username(), "password");
        if(retrievedPassword != null && Objects.equals(retrievedPassword, user.password())) {
            throw new AlreadyTakenException("Error: already taken");
        }
        try {
            userDAO.createUser(user);
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, user.username());
        authDAO.createAuth(authData);
        return authData;
    }

    public AuthData login(UserData user) throws UnauthorizedException, SQLException {
        String retrievedUser = userDAO.getElement(user.username(), "username");
        if(retrievedUser == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        String retrievePassword = userDAO.getElement(user.username(), "username");
        if(!Objects.equals(retrievePassword, user.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, retrievedUser);
        authDAO.createAuth(authData);
        return authData;
    }

    public void logout(String authToken) throws UnauthorizedException, SQLException, DataAccessException {
        String retrievedUser = authDAO.getUser(authToken);
        if(retrievedUser == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        authDAO.deleteAuth(retrievedUser, authToken);
    }

    public void clearUsers() throws DataAccessException {
        try {
            userDAO.clear();
        }
        catch(Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
