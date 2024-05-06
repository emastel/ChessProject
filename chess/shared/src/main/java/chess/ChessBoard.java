package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {}

    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        /* White Rook */
        ChessPiece newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPosition newPosition = new ChessPosition(1,1);
        addPiece(newPosition,newPiece);
        newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        newPosition = new ChessPosition(1,8);
        addPiece(newPosition,newPiece);
        /* White Knight */
        newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        newPosition = new ChessPosition(1,2);
        addPiece(newPosition,newPiece);
        newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        newPosition = new ChessPosition(1,7);
        addPiece(newPosition,newPiece);
        /* White Bishop */
        newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        newPosition = new ChessPosition(1,3);
        addPiece(newPosition,newPiece);
        newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        newPosition = new ChessPosition(1,6);
        addPiece(newPosition,newPiece);
        /* White Queen */
        newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        newPosition = new ChessPosition(1,4);
        addPiece(newPosition,newPiece);
        /* White King */
        newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        newPosition = new ChessPosition(1,5);
        addPiece(newPosition,newPiece);
        /* White Pawns */
        for (int i = 0; i < 8; i++) {
            newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            newPosition = new ChessPosition(2,i+1);
            addPiece(newPosition,newPiece);
        }
        /* Black Pawns */
        for (int i = 0; i < 8; i++) {
            newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            newPosition = new ChessPosition(7,i+1);
            addPiece(newPosition,newPiece);
        }
        /* Black Rook */
        newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        newPosition = new ChessPosition(8,1);
        addPiece(newPosition,newPiece);
        newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        newPosition = new ChessPosition(8,8);
        addPiece(newPosition,newPiece);
        /* Black Knight */
        newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        newPosition = new ChessPosition(8,2);
        addPiece(newPosition,newPiece);
        newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        newPosition = new ChessPosition(8,7);
        addPiece(newPosition,newPiece);
        /* Black Bishop */
        newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        newPosition = new ChessPosition(8,3);
        addPiece(newPosition,newPiece);
        newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        newPosition = new ChessPosition(8,6);
        addPiece(newPosition,newPiece);
        /* Black Queen */
        newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        newPosition = new ChessPosition(8,4);
        addPiece(newPosition,newPiece);
        /* Black King */
        newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        newPosition = new ChessPosition(8,5);
        addPiece(newPosition,newPiece);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }
}
