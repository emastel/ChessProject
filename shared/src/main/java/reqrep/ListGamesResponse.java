package reqrep;

import model.GameData;

public record ListGamesResponse(GameData[] games) {
}
