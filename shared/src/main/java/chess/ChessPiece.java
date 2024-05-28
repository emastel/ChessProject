package chess;

import java.util.Collection;
import java.util.Objects;

public class ChessPiece {

    private ChessGame.TeamColor thisColor;

    private ChessPiece.PieceType thisType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        thisColor = pieceColor;
        thisType = type;
    }

    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    public ChessGame.TeamColor getTeamColor() {
        return thisColor;
    }

    public PieceType getPieceType() {
        return thisType;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceMovesCalculator moveType = new PieceMovesCalculator(board, myPosition);
        if(thisType == PieceType.BISHOP) {
            return moveType.bishopMoves();
        }
        else if(thisType == PieceType.KING) {
            return moveType.kingMoves();
        }
        else if(thisType == PieceType.KNIGHT) {
            return moveType.knightMoves();
        }
        else if(thisType == PieceType.PAWN) {
            return moveType.pawnMoves();
        }
        else if(thisType == PieceType.ROOK) {
            return moveType.rookMoves();
        }
        else if(thisType == PieceType.QUEEN) {
            return moveType.queenMoves();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return thisColor == that.thisColor && thisType == that.thisType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(thisColor, thisType);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "thisColor=" + thisColor +
                ", thisType=" + thisType +
                '}';
    }
}
