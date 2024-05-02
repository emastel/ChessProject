package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor thisColor;
    private ChessPiece.PieceType thisType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.thisColor = pieceColor;
        this.thisType = type;
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

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if(thisType == ChessPiece.PieceType.KING) {

        }
        else if(thisType == ChessPiece.PieceType.QUEEN) {

        }
        else if(thisType == ChessPiece.PieceType.BISHOP) {

        }
        else if(thisType == ChessPiece.PieceType.KNIGHT) {

        }
        else if(thisType == ChessPiece.PieceType.ROOK) {

        }
        else if(thisType == ChessPiece.PieceType.PAWN) {

        }
    }
}
