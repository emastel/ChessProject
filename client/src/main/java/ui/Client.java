package ui;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import dataaccess.SqlAuthDAO;
import exception.ResponseException;
import model.GameData;
import net.ServerFacade;
import net.State;
import reqrep.BlankResponse;
import reqrep.ListGamesResponse;
import reqrep.RegisterLoginResponse;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client {

    private  ServerFacade server;
    private  State state = State.SIGNEDOUT;
    private  PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private  boolean running = true;
    private  String authToken;
    String user;
    GameData[] list;
    private static boolean inGame = false;
    private String team;
    private Gameplay game;
    private String url;
    private SqlAuthDAO auths;
    private WsClient ws;
    private static boolean isObserver = false;
    int gameID;


    public Client() {
        server = new ServerFacade(8080);
        url = server.getServerUrl();
    }

    public static void main(String[] args) {
        Client client = new Client();
        while (client.running) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if(inGame) {
                client.gameEval(input);
            }
            else if(isObserver) {
                client.obsEval(input);
            }
            else {
                client.eval(input);
            }
        }
    }

    public  void eval(String input) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "quit" -> quit();
                default -> help();
            }
        } catch (Exception e) {
            out.print(SET_TEXT_COLOR_RED);
            out.print(e.getMessage());
            out.println();
        }
    }

    public  void gameEval(String input) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            switch (cmd) {
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> makeMove(params);
                case "resign" -> resign();
                case "moves" -> legalMoves(params);
                default -> gameHelp();
            }
        } catch (Exception e) {
            out.print(SET_TEXT_COLOR_RED);
            out.print(e.getMessage());
            out.println();
        }
    }

    public  void obsEval(String input) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            switch (cmd) {
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "moves" -> legalMoves(params);
                default -> obsHelp();
            }
        } catch (Exception e) {
            out.print(SET_TEXT_COLOR_RED);
            out.print(e.getMessage());
            out.println();
        }
    }

    public  void register(String...params) {
        if(params.length >= 1) {
            RegisterLoginResponse res = server.register(params[0], params[1], params[2]);
            if(res.message() == null) {
                authToken = res.authToken();
            }
            else {
                out.print(SET_TEXT_COLOR_RED);
                out.print(res.message());
            }
            out.print(SET_TEXT_COLOR_GREEN);
            out.print("Registered successfully");
            state = State.SIGNEDIN;
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
            out.print("Missing information");
        }
        out.println();
    }

    public  void login(String...params) {
        if(params.length >= 1) {
            state = State.SIGNEDIN;
            RegisterLoginResponse res = server.login(params[0], params[1]);
            if(res.message() == null) {
                authToken = res.authToken();
            }
            else {
                out.print(SET_TEXT_COLOR_RED);
                out.print(res.message());
            }
            out.print(SET_TEXT_COLOR_GREEN);
            out.print("Logged in successfully");
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
            out.print("Missing information");
        }
        out.println();
    }

    public  void logout() {
        BlankResponse res = server.logout(authToken);
        if(res == null) {
            out.print(SET_TEXT_COLOR_GREEN);
            out.print("Logged out successfully");
            state = State.SIGNEDOUT;
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
            out.print(res.message());
        }
        out.println();
    }

    public  void createGame(String...params) {
        if(params.length >= 1) {
            Object res = server.createGame(params[0], authToken);
            if(res instanceof BlankResponse) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(((BlankResponse)res).message());
            }
            else {
                out.print(SET_TEXT_COLOR_GREEN);
                out.print("Created game successfully");
            }
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
            out.print("Missing information");
        }
        out.println();
    }

    public  void listGames() {
        Object allGames = server.listGames(authToken);
        if(allGames instanceof ListGamesResponse) {
            list = ((ListGamesResponse)allGames).games();
            int i = 1;
            for(GameData game : list) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(i +". Game Name: ");
                out.print(SET_TEXT_COLOR_MAGENTA);
                out.print(game.getGameName());
                out.println();
                out.print(SET_TEXT_COLOR_BLUE);
                out.print("White Player: ");
                out.print(SET_TEXT_COLOR_MAGENTA);
                out.print(game.getWhiteUsername());
                out.println();
                out.print(SET_TEXT_COLOR_BLUE);
                out.print("Black Player: ");
                out.print(SET_TEXT_COLOR_MAGENTA);
                out.print(game.getBlackUsername());
                out.println();
                ++i;
            }
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
            out.print(((BlankResponse)allGames).message());
        }
        out.println();
    }

    public  void joinGame(String...params) throws ResponseException, IOException {
        if(params.length >= 1) {
            int gameId = list[Integer.parseInt(params[1])-1].getGameID();
            team = params[0];
            BlankResponse res = server.joinGame(authToken, team, gameId);
            if(res == null) {
                inGame = true;
                game = new Gameplay();
                ws.connect(authToken,gameId);
            }
            else {
                out.print(SET_TEXT_COLOR_RED);
                out.print(res.message());
            }
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
            out.print("Missing information");
        }
        out.println();
    }

    public  void observeGame(String...params) throws IOException {
        if(params.length >= 1) {
            int gameId = list[Integer.parseInt(params[1])-1].getGameID();
            ws.connect(authToken,gameId);
            game = new Gameplay();
        } else {
            out.print(SET_TEXT_COLOR_RED);
            out.print("Missing information");
        }
    }

    public void redraw() {
        game.drawBoard(team,null);
    }

    public void leave() throws IOException {
        ws.leave(authToken,gameID);
    }

    public void resign() throws IOException {
        ws.resign(authToken,gameID);

    }

    public void legalMoves(String...params) {
        if(params.length >= 1) {
            try {
                int row = Integer.parseInt(params[0]);
                int col = Integer.parseInt(params[1]);
                String user = auths.getUser(authToken);
                game.highlightLegalMoves(row, col, user);
            } catch (Exception e) {
                out.print(SET_TEXT_COLOR_RED);
                out.print("Invalid");
            }
        }
    }

    public void makeMove(String...params) throws IOException {
        int startRow = Integer.parseInt(params[0]);
        int startCol = Integer.parseInt(params[1]);
        int endRow = Integer.parseInt(params[2]);
        int endCol = Integer.parseInt(params[3]);
        ChessMove move = null;
        ChessPosition startPosition = new ChessPosition(startRow, startCol);
        ChessPosition endPosition = new ChessPosition(endRow, endCol);
        String piece = game.getPiece(startPosition);
        if (piece != null) {
            if (piece.equals("whitePawn") || piece.equals("blackPawn")) {
                if(endRow == 8 || endRow == 1) {
                    out.print(SET_TEXT_COLOR_YELLOW);
                    out.print("What piece would you like to promote it to?");
                    out.println();
                    Scanner scanner = new Scanner(System.in);
                    String input = scanner.nextLine();
                    if (Objects.equals(input, "bishop")) {
                        move = new ChessMove(startPosition,endPosition, ChessPiece.PieceType.BISHOP);
                    }
                    else if (Objects.equals(input, "knight")) {
                        move = new ChessMove(startPosition,endPosition, ChessPiece.PieceType.KNIGHT);
                    }
                    else if (Objects.equals(input, "rook")) {
                        move = new ChessMove(startPosition,endPosition, ChessPiece.PieceType.ROOK);
                    }
                    else if (Objects.equals(input, "queen")) {
                        move = new ChessMove(startPosition,endPosition, ChessPiece.PieceType.QUEEN);
                    }
                    else {
                        out.print(SET_TEXT_COLOR_RED);
                        out.print("Invalid input");
                    }
                }
            }
            else {
                move = new ChessMove(startPosition,endPosition, null);
            }
        }
        ws.makeMove(authToken,gameID,move);
        game.drawBoard(team,null);
    }

    private  void printHelpQuit(PrintStream out) {
        out.print(SET_TEXT_COLOR_BLUE);
        out.print("quit ");
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print("- playing chess");
        out.println();
        out.print(SET_TEXT_COLOR_BLUE);
        out.print("help ");
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print("- with possible commands");
        out.println();
    }

    public  void help() {
        if(state == State.SIGNEDIN) {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("create <NAME> ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- a game");
            out.println();
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("list ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- games");
            out.println();
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("join [WHITE|BLACK] <ID> ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- a game");
            out.println();
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("observe <ID> ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- a game");
            out.println();
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("logout ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- when you are done");
            out.println();
            printHelpQuit(out);
        } else {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("register <USERNAME> <PASSWORD> <EMAIL> ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- to create an account");
            out.println();
            out.print(SET_TEXT_COLOR_BLUE);
            out.print("login <USERNAME> <PASSWORD> ");
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.print("- to play chess");
            out.println();
            printHelpQuit(out);
        }
    }

    public void gameHelp() {
        out.print(SET_TEXT_COLOR_BLUE);
        out.print("redraw");
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print("- redraws chess board");
        out.println();
        out.print(SET_TEXT_COLOR_BLUE);
        out.print("move <Start Row, Start Column, End Row, End Column>");
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print("- make a move");
        out.println();
        out.print(SET_TEXT_COLOR_BLUE);
        out.print("moves <ROW,COLUMN>");
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print("- highlights legal moves");
        out.println();
        out.print(SET_TEXT_COLOR_BLUE);
        out.print("resign");
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print("- forfeit");
        out.println();
        out.print(SET_TEXT_COLOR_BLUE);
        out.print("leave");
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print("- leave the game");
        out.println();
    }

    public void obsHelp() {
        out.print(SET_TEXT_COLOR_BLUE);
        out.print("redraw");
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print("- redraws chess board");
        out.println();
        out.print(SET_TEXT_COLOR_BLUE);
        out.print("moves <ROW,COLUMN>");
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print("- highlights legal moves");
        out.println();
        out.print(SET_TEXT_COLOR_BLUE);
        out.print("leave");
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print("- leave the game");
        out.println();
    }

    public  void quit() {
        running = false;
        out.print(SET_TEXT_COLOR_GREEN);
        out.print("Quit successfully");
    }
}
