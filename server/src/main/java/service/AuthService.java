package service;

import dataaccess.AuthDAO;

public class AuthService {

    private AuthDAO authDAO;

    public void clearAuth() {
        authDAO.clearAuth();
    }

}
