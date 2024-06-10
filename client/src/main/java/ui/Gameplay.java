package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class Gameplay {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = BOARD_SIZE_IN_SQUARES / 2;

    private enum Player {WHITE, BLACK}
    private static Player color;

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
                drawSquares(out, col);
                printBuffer(out, col);
                out.print(RESET_BG_COLOR);
                out.println();
            }
        }
        else {
            for (int col = BOARD_SIZE_IN_SQUARES-1; col >=0; --col) {
                printBuffer(out, col);
                drawSquares(out, col);
                out.print(SET_BG_COLOR_LIGHT_GREY);
                printBuffer(out, col);
                out.print(RESET_BG_COLOR);
                out.println();
            }
        }

    }

    private static void drawSquares(PrintStream out, int col) {
        for(int squareRow = 0; squareRow < BOARD_SIZE_IN_SQUARES; squareRow++) {
            if(col % 2 == 0) {
                if(squareRow % 2 == 0) {
                    out.print(SET_BG_COLOR_WHITE);
                    printSquares(out, col, squareRow);
                }
                else {
                    out.print(SET_BG_COLOR_BLACK);
                    printSquares(out, col, squareRow);
                }
            }
            else {
                if(squareRow % 2 == 0) {
                    out.print(SET_BG_COLOR_BLACK);
                    printSquares(out, col, squareRow);
                }
                else {
                    out.print(SET_BG_COLOR_WHITE);
                    printSquares(out, col, squareRow);
                }
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
        if((row==0&&col==0) || (row==7&&col==0)) {
            out.print(SET_TEXT_COLOR_RED);
            out.print(WHITE_ROOK);
        }
        else if ((row==1&&col==0)||(row==6&&col==0)) {
            out.print(SET_TEXT_COLOR_RED);
            out.print(WHITE_KNIGHT);
        }
        else if ((row==2&&col==0) || (row==5&&col==0)) {
            out.print(SET_TEXT_COLOR_RED);
            out.print(WHITE_BISHOP);
        }
        else if (row==3&&col==0) {
            out.print(SET_TEXT_COLOR_RED);
            out.print(WHITE_KING);
        }
        else if (row==4&&col==0) {
            out.print(SET_TEXT_COLOR_RED);
            out.print(WHITE_QUEEN);
        }
        else if (col==1) {
            out.print(SET_TEXT_COLOR_RED);
            out.print(WHITE_PAWN);
        }
        else if((row==0&&col==7) || (row==7&&col==7)) {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(BLACK_ROOK);
        }
        else if ((row==1&&col==7)||(row==6&&col==7)) {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(BLACK_KNIGHT);
        }
        else if ((row==2&&col==7) || (row==5&&col==7)) {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(BLACK_BISHOP);
        }
        else if (row==3&&col==7) {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(BLACK_KING);
        }
        else if (row==4&&col==7) {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(BLACK_QUEEN);
        }
        else if (col==6) {
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(BLACK_PAWN);
        }
        else {
            out.print("   ");
        }
    }
}
