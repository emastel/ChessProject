package dataaccess;

import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class GameDAO {

    private Map<String, GameData> games = new HashMap<>();

    public void clear() {
        games.clear();
    }

    public void createGame(GameData input) {
        games.put(input.gameName(),input);
    }

    public GameData getGame(String gameName) {
        return games.get(gameName);
    }

    public Map<String, GameData> listGames() {
        return games;
    }

    public void updateGame(GameData input) {
        games.put(input.gameName(),input);
    }
}
