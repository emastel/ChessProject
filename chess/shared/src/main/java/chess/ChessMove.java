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

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        if(thisType == ChessPiece.PieceType.QUEEN) {
            return ChessPiece.PieceType.QUEEN;
        }
        else if(thisType == ChessPiece.PieceType.KING) {
            return ChessPiece.PieceType.KING;
        }
        else if(thisType == ChessPiece.PieceType.BISHOP) {
            return ChessPiece.PieceType.BISHOP;
        }
        else if(thisType == ChessPiece.PieceType.KNIGHT) {
            return ChessPiece.PieceType.KNIGHT;
        }
        else if(thisType == ChessPiece.PieceType.ROOK) {
            return ChessPiece.PieceType.ROOK;
        }
        else {
            return null;
        }
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
