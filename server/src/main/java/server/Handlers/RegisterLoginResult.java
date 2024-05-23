package server.Handlers;

public record RegisterLoginResult(boolean success, String message, String username, String authToken) {
}
