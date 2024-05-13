package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private ChessPosition thisStart;

    private ChessPosition thisEnd;

    private ChessPiece.PieceType thisType;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        thisStart = startPosition;
        thisEnd = endPosition;
        thisType = promotionPiece;
    }

    public ChessPosition getStartPosition() {
        return thisStart;
    }

    public ChessPosition getEndPosition() {
        return thisEnd;
    }

    public ChessPiece.PieceType getPromotionPiece() {
        return thisType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(thisStart, chessMove.thisStart) && Objects.equals(thisEnd, chessMove.thisEnd) && thisType == chessMove.thisType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(thisStart, thisEnd, thisType);
    }

    @Override
    public String toString() {
        return "ChessMove{" +
                "thisStart=" + thisStart +
                ", thisEnd=" + thisEnd +
                ", thisType=" + thisType +
                '}';
    }
}
