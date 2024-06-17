package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import dataaccess.DataAccessException;
import dataaccess.SqlGameDAO;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class Gameplay {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private enum Player {WHITE, BLACK}
    private static Player color;
    private static Map<ChessPosition,String> pieces = new HashMap<>();
    private static boolean started = false;
    private static GameData gameData;
    private static ChessGame game;
    private static SqlGameDAO gameDAO;
    private static PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    static {
        try {
            gameDAO = new SqlGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void startGame(String color, int id) {
//        try{
//            started = true;
//            gameData = gameDAO.getGame(id);
//            game = gameData.getGame();
//            drawBoard(color, null);
//        } catch(Exception e) {
//            out.print(SET_TEXT_COLOR_RED);
//            out.print("Invalid game ID");
//        }
//    }
//
//    public static void leaveGame(String user) {
//        if(Objects.equals(gameData.getWhiteUsername(), user)) {
//            gameData.setWhiteUsername(null);
//        }
//        else if(Objects.equals(gameData.getBlackUsername(), user)) {
//            gameData.setBlackUsername(null);
//        }
//    }

    public static void drawBoard(String team, Collection<ChessMove> moves) {
        if(Objects.equals(team, "WHITE")){
            color = Player.WHITE;
        }
        else {
            color = Player.BLACK;
        }
        out.print(ERASE_SCREEN);
        drawHeaders();
        drawChessBoard(moves);
        drawHeaders();
    }

    private static void setGrey() {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void drawHeaders() {
        setGrey();

        String[] headers = {" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "};
        out.print("   ");
        if(color == Player.WHITE) {
            for(int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
                printHeaderText(headers[boardCol]);
            }
        }
        else {
            for(int boardCol = BOARD_SIZE_IN_SQUARES -1; boardCol >= 0; --boardCol) {
                printHeaderText(headers[boardCol]);
            }
        }
        out.print("   ");
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void printHeaderText(String text) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(text);
        setGrey();
    }

    private static void printBuffer(int row) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(" " + (row + 1) + " ");
    }

    private static void drawChessBoard(Collection<ChessMove> moves) {
        if(color == Player.WHITE) {
            for (int col = 0; col < BOARD_SIZE_IN_SQUARES; ++col) {
                printBuffer(col);
                drawSquares(col, moves);
                printBuffer(col);
                out.print(RESET_BG_COLOR);
                out.println();
            }
        }
        else {
            for (int col = BOARD_SIZE_IN_SQUARES-1; col >=0; --col) {
                printBuffer(col);
                drawSquares(col, moves);
                out.print(SET_BG_COLOR_LIGHT_GREY);
                printBuffer(col);
                out.print(RESET_BG_COLOR);
                out.println();
            }
        }

    }

    public void highlightLegalMoves(int row, int col, String user) {
        ChessPosition position = new ChessPosition(row, col);
        Collection<ChessMove> moves = game.validMoves(position);
        if(game.getTeamTurn() == ChessGame.TeamColor.WHITE) {
            drawBoard("White",moves);
        }
        else {
            drawBoard("Black",moves);
        }
    }


    private static void printSquare(ChessPosition position, Collection<ChessMove> moves, String color) {
        if(moves != null) {
            boolean printed = false;
            for(ChessMove move : moves) {
                if(move.getEndPosition() == position) {
                    if(Objects.equals(color, "white")) {
                        out.print(SET_BG_COLOR_GREEN);
                    }
                    else {
                        out.print(SET_BG_COLOR_DARK_GREEN);
                    }
                    printed = true;
                    break;
                }
            }
            if(!printed) {
                if(Objects.equals(color, "white")) {
                    out.print(SET_BG_COLOR_WHITE);
                }
                else {
                    out.print(SET_BG_COLOR_BLACK);
                }
            }
        }
        else {
            if(Objects.equals(color, "white")) {
                out.print(SET_BG_COLOR_WHITE);
            }
            else {
                out.print(SET_BG_COLOR_BLACK);
            }
        }
    }


    private static void drawSquares(int col, Collection<ChessMove> moves) {
        for(int squareRow = 0; squareRow < BOARD_SIZE_IN_SQUARES; squareRow++) {
            ChessPosition pos = new ChessPosition(squareRow, col);
            if(col % 2 == 0) {
                if(squareRow % 2 == 0) {
                    printSquare(pos, moves, "white");
                }
                else {
                    printSquare(pos, moves, "black");
                }
            }
            else {
                if(squareRow % 2 == 0) {
                    printSquare(pos, moves, "black");
                }
                else {
                    printSquare(pos, moves, "white");
                }
            }
            if(started) {
                printSquares(col, squareRow);
            }
            else {
                initialPrintSquares(col, squareRow);
            }
        }
    }

    private static void drawLine() {
        for(int i=0; i < 10; i++) {
            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print("   ");
        }
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void printSquares(int col, int row) {
        ChessPosition position = new ChessPosition(row, col);
        if(pieces.containsKey(position)) {
            if(Objects.equals(pieces.get(position), "whiteRook")) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(WHITE_ROOK);
            }
            else if (Objects.equals(pieces.get(position), "whiteKnight")) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(WHITE_KNIGHT);
            }
            else if (Objects.equals(pieces.get(position), "whiteBishop")) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(WHITE_BISHOP);
            }
            else if (Objects.equals(pieces.get(position), "whiteQueen")) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(WHITE_QUEEN);
            }
            else if (Objects.equals(pieces.get(position), "whiteKing")) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(WHITE_KING);
            }
            else if (Objects.equals(pieces.get(position), "whitePawn")) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(WHITE_PAWN);
            }
            else if (Objects.equals(pieces.get(position), "blackRook")) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(BLACK_ROOK);
            }
            else if (Objects.equals(pieces.get(position), "blackKnight")) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(BLACK_KNIGHT);
            }
            else if (Objects.equals(pieces.get(position), "blackBishop")) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(BLACK_BISHOP);
            }
            else if (Objects.equals(pieces.get(position), "blackQueen")) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(BLACK_QUEEN);
            }
            else if (Objects.equals(pieces.get(position), "blackKing")) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(BLACK_KING);
            }
            else if (Objects.equals(pieces.get(position), "blackPawn")) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(BLACK_PAWN);
            }
            else {
                out.print("   ");
            }
        }
    }

    private static void initialPrintSquares(int col, int row) {
        ChessPosition position = new ChessPosition(row, col);
        if((row==0&&col==0) || (row==7&&col==0)) {
            out.print(SET_TEXT_COLOR_RED);
            out.print(WHITE_ROOK);
            pieces.put(position, "whiteRook");
        }
        else if ((row==1&&col==0)||(row==6&&col==0)) {
            out.print(SET_TEXT_COLOR_RED);
            out.print(WHITE_KNIGHT);
            pieces.put(position,"whiteKnight");
        }
        else if ((row==2&&col==0) || (row==5&&col==0)) {
            out.print(SET_TEXT_COLOR_RED);
            out.print(WHITE_BISHOP);
            pieces.put(position,"whiteBishop");
        }
        else if (row==3&&col==0) {
            out.print(SET_TEXT_COLOR_RED);
            out.print(WHITE_KING);
            pieces.put(position,"whiteKing");
        }
        else if (row==4&&col==0) {
            out.print(SET_TEXT_COLOR_RED);
            out.print(WHITE_QUEEN);
            pieces.put(position,"whiteQueen");
        }
        else if (col==1) {
            out.print(SET_TEXT_COLOR_RED);
            out.print(WHITE_PAWN);
            pieces.put(position,"whitePawn");
        }
        else if((row==0&&col==7) || (row==7&&col==7)) {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(BLACK_ROOK);
            pieces.put(position,"blackRook");
        }
        else if ((row==1&&col==7)||(row==6&&col==7)) {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(BLACK_KNIGHT);
            pieces.put(position,"blackKnight");
        }
        else if ((row==2&&col==7) || (row==5&&col==7)) {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(BLACK_BISHOP);
            pieces.put(position,"blackBishop");
        }
        else if (row==3&&col==7) {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(BLACK_KING);
            pieces.put(position,"blackKing");
        }
        else if (row==4&&col==7) {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(BLACK_QUEEN);
            pieces.put(position,"blackQueen");
        }
        else if (col==6) {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(BLACK_PAWN);
            pieces.put(position,"blackPawn");
        }
        else {
            out.print("   ");
        }
    }
}
