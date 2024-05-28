package server.handlers;

import model.GameData;

public record ListGamesResponse(GameData[] games) {
}
