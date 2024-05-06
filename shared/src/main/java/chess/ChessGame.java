package chess;

import java.util.Collection;
import java.util.Iterator;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard thisBoard;
    private TeamColor currentTeam;

    public ChessGame() {

    }

    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    public void setTeamTurn(TeamColor team) {
        currentTeam = team;
    }

    public enum TeamColor {
        WHITE,
        BLACK
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece newPiece = thisBoard.getPiece(startPosition);
        return newPiece.pieceMoves(thisBoard, startPosition);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
//        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
//        if (!validMoves.isEmpty()) {
//            for (Iterator<ChessMove> it = validMoves.iterator(); it.hasNext();) {
//                if(it.next().getEndPosition().equals(move.getEndPosition())) {
//                    if()
//                }
//                throw new InvalidMoveException();
//            }
//        }
//        else {
//            throw new InvalidMoveException();
//        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    public void setBoard(ChessBoard board) {
        thisBoard = board;
    }

    public ChessBoard getBoard() {
        return thisBoard;
    }
}
