package service;

import dataaccess.AuthDAO;

public class AuthService {

    private AuthDAO authDAO = new AuthDAO();

    public void clearAuth() {
        authDAO.clearAuth();
    }

}
