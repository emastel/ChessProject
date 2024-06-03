package unit;

import dataaccess.DataAccessException;
import dataaccess.SqlAuthDAO;

public class AuthService {

//    private AuthDAO authDAO = new AuthDAO();

    private SqlAuthDAO authDAO;

    public AuthService() {
        try {
            authDAO = new SqlAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearAuth() throws DataAccessException {
        authDAO.clear();
    }

}
