package requestResponse;

import model.GameData;

public record ListGamesResponse(GameData[] games) {
}
