package server.Handlers;

import model.GameData;

public record ListGamesResponse(GameData[] games) {
}
