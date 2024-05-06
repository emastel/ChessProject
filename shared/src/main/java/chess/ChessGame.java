package chess;

import java.util.ArrayList;
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

    public ChessGame() {}
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
    private TeamColor getOppositeTeam() {
        if(currentTeam == TeamColor.BLACK) {
            return TeamColor.WHITE;
        }
        else{
            return TeamColor.BLACK;
        }
    }
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece newPiece = thisBoard.getPiece(startPosition);
        Collection<ChessMove> tempMoves = newPiece.pieceMoves(thisBoard, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        for(ChessMove move : tempMoves) {

        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece tempPiece = thisBoard.getPiece(move.getStartPosition());
        if(tempPiece == null || tempPiece.getTeamColor() != currentTeam) {
            throw new InvalidMoveException();
        }
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (!validMoves.isEmpty()) {
            for (ChessMove validMove : validMoves) {
                if (validMove.getEndPosition().equals(move.getEndPosition())) {
                    if(isInCheck(getOppositeTeam())) {
                        throw new InvalidMoveException();
                    }
                    else {
                        thisBoard.removePiece(move.getStartPosition());
                        thisBoard.addPiece(move.getEndPosition(),tempPiece);
                    }
                }
                else {
                    throw new InvalidMoveException();
                }
            }
        }
        else {
            throw new InvalidMoveException();
        }
    }
    private Collection<ChessMove> getTeamMoves(TeamColor teamColor) {
        Collection<ChessPosition> teamPieces = thisBoard.getTeamPieces(teamColor);
        Collection<ChessMove> allMoves = new ArrayList<>();
        for (ChessPosition tempPosition : teamPieces) {
            ChessPiece tempPiece = thisBoard.getPiece(tempPosition);
            Collection<ChessMove> tempMoves = tempPiece.pieceMoves(thisBoard, tempPosition);
            allMoves.addAll(tempMoves);
        }
        return allMoves;
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> allMoves = new ArrayList<>();
        allMoves = getTeamMoves(getOppositeTeam());
        for (ChessMove tempMove : allMoves) {
            ChessPosition tempPosition = tempMove.getEndPosition();
            ChessPiece tempPiece = thisBoard.getPiece(tempPosition);
            if(tempPiece.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        return false;
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
