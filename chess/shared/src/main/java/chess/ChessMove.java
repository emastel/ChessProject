package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private:
    ChessPosition thisStart;
    ChessPosition thisEnd;
    ChessPiece.PieceType thisPromotion;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        thisStart = startPosition;
        thisEnd = endPosition;
        thisPromotion = promotionPiece;
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
        throw new RuntimeException("Not implemented");
    }
}
