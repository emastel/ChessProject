package unit;

import dataaccess.DataAccessException;
import dataaccess.SqlAuthDAO;
import dataaccess.SqlUserDAO;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.UUID;

public class UserService {

//    private UserDAO userDAO = new UserDAO();
//
//    private AuthDAO authDAO = new AuthDAO();

    private SqlUserDAO userDAO;
    private SqlAuthDAO authDAO;

    public UserService() {
        try {
            authDAO = new SqlAuthDAO();
            userDAO = new SqlUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthData register(UserData user) throws AlreadyTakenException, BadRequestException, SQLException {
        if(user.username() == null || user.password() == null || user.username().isEmpty() || user.password().isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        String retrievedUsername = userDAO.getElement(user.username(), "username");
        if(retrievedUsername != null && retrievedUsername.equals(user.username())) {
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
        String retrievePassword = userDAO.getElement(user.username(), "password");
        if(!BCrypt.checkpw(user.password(), retrievePassword)) {
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
