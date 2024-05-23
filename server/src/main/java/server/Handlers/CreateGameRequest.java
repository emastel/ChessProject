package server.Handlers;

public record CreateGameRequest(String authToken, String gameName) {
}
