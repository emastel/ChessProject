package dataaccess;

import model.GameData;

import java.util.HashMap;
import java.util.Map;
import static server.handlers.Handler.gson;

public class GameDAO {

    private static Map<Integer, GameData> games = new HashMap<>();

    public void clear() {
        if(!games.isEmpty()) {
            games.clear();
        }
    }

    public void createGame(GameData input) {
        games.put(input.getGameID(),input);
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    public Map<Integer, GameData> listGames() {
        return games;
    }

    public void updateGame(GameData input) {
        games.put(input.getGameID(), input);
    }

    public String gameToString(GameData theGame) {
        return gson.toJson(theGame.getGame());
    }
}
