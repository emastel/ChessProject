package dataaccess;

import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class GameDAO {

    private static Map<Integer, GameData> games = new HashMap<>();

    public void clear() {
        games.clear();
    }

    public void createGame(GameData input) {
        games.put(input.getGameId(),input);
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    public Map<Integer, GameData> listGames() {
        return games;
    }

    public void updateGame(GameData input) {
        games.put(input.getGameId(), input);
    }
}
