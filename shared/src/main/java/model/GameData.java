package model;

import chess.ChessGame;
import com.google.gson.Gson;

public class GameData {

    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private ChessGame game;

    public static Gson gson = new Gson();

    public GameData() {}

    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.game = game;
        this.gameName = gameName;
    }

    public int getGameID() {
        return this.gameID;
    }

    public String gameToString() {
        return gson.toJson(game);
    }

    public String getWhiteUsername() {
        return this.whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return this.blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public String getGameName() {
        return this.gameName;
    }

    public ChessGame getGame() {
        return this.game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }
}
