package server.handlers;

public record RegisterRequest(String username, String password, String email) {}