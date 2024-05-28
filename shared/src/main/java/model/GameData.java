package model;

import chess.ChessGame;

public class GameData {

    private int gameId;
    private String whiteUsername;
    private String blackUsername;
    private String thisGameName;
    private ChessGame thisGame;

    public GameData(int gameID, String whiteUser, String blackUser, String gameName, ChessGame game) {
        gameId = gameID;
        whiteUsername = whiteUser;
        blackUsername = blackUser;
        thisGame = game;
        thisGameName = gameName;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public String getGameName() {
        return thisGameName;
    }

    public void setGameName(String gameName) {
        this.thisGameName = gameName;
    }

    public ChessGame getGame() {
        return thisGame;
    }

    public void setGame(ChessGame game) {
        this.thisGame = game;
    }
}
