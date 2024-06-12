package ui;

import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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

    public static void main() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        color = Player.WHITE;
        out.print(ERASE_SCREEN);
        drawHeaders(out);
        drawChessBoard(out);
        drawHeaders(out);
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        color = Player.BLACK;
        drawLine(out);
        drawHeaders(out);
        drawChessBoard(out);
        drawHeaders(out);
    }

    public static void drawBoard(String team) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        if(Objects.equals(team, "WHITE")){
            color = Player.WHITE;
        }
        else {
            color = Player.BLACK;
        }
        out.print(ERASE_SCREEN);
        drawHeaders(out);
        drawChessBoard(out);
        drawHeaders(out);
    }




    private static void setGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void drawHeaders(PrintStream out) {
        setGrey(out);

        String[] headers = {" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "};
        out.print("   ");
        if(color == Player.WHITE) {
            for(int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
                printHeaderText(out,headers[boardCol]);
            }
        }
        else {
            for(int boardCol = BOARD_SIZE_IN_SQUARES -1; boardCol >= 0; --boardCol) {
                printHeaderText(out,headers[boardCol]);
            }
        }
        out.print("   ");
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void printHeaderText(PrintStream out, String text) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(text);
        setGrey(out);
    }

    private static void printBuffer(PrintStream out, int row) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(" " + (row + 1) + " ");
    }

    private static void drawChessBoard(PrintStream out) {
        if(color == Player.WHITE) {
            for (int col = 0; col < BOARD_SIZE_IN_SQUARES; ++col) {
                printBuffer(out, col);
                drawSquares(out, col, null);
                printBuffer(out, col);
                out.print(RESET_BG_COLOR);
                out.println();
            }
        }
        else {
            for (int col = BOARD_SIZE_IN_SQUARES-1; col >=0; --col) {
                printBuffer(out, col);
                drawSquares(out, col,null);
                out.print(SET_BG_COLOR_LIGHT_GREY);
                printBuffer(out, col);
                out.print(RESET_BG_COLOR);
                out.println();
            }
        }

    }

    private void hightlightLegalMoves(int row, int col) {
        ChessPosition position = new ChessPosition(row, col);


    }

    private static void drawSquares(PrintStream out, int col, ) {
        for(int squareRow = 0; squareRow < BOARD_SIZE_IN_SQUARES; squareRow++) {
            if(col % 2 == 0) {
                if(squareRow % 2 == 0) {
                    out.print(SET_BG_COLOR_WHITE);
                }
                else {
                    out.print(SET_BG_COLOR_BLACK);
                }
            }
            else {
                if(squareRow % 2 == 0) {
                    out.print(SET_BG_COLOR_BLACK);
                }
                else {
                    out.print(SET_BG_COLOR_WHITE);
                }
            }
            if(started) {
                printSquares(out, col, squareRow);
            }
            else {
                initialPrintSquares(out, col, squareRow);
            }
        }
    }

    private static void drawLine(PrintStream out) {
        for(int i=0; i < 10; i++) {
            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print("   ");
        }
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void printSquares(PrintStream out, int col, int row) {
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

    private static void initialPrintSquares(PrintStream out, int col, int row) {
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
