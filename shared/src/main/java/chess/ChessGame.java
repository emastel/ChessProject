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
        Collection<ChessMove> validMoves = new ArrayList<>();
        if(thisBoard.getPiece(startPosition) != null) {
            ChessPiece thisPiece = thisBoard.getPiece(startPosition);
            TeamColor team = thisPiece.getTeamColor();
            if(isInStalemate(team)) {
                return validMoves;
            }
            validMoves = thisPiece.pieceMoves(thisBoard,startPosition);
            return validMoves;
        }
        return null;
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
                    else if(isInStalemate(getOppositeTeam())){
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
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> allMoves = getTeamMoves(getOppositeTeam());
        for (ChessMove tempMove : allMoves) {
            ChessPosition tempPosition = tempMove.getEndPosition();
            ChessPiece tempPiece = thisBoard.getPiece(tempPosition);
            if(tempPiece.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        return false;
    }
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> oppositeMoves = getTeamMoves(getOppositeTeam());
        Collection<ChessMove> teamMoves = getTeamMoves(teamColor);
        for (ChessMove tempOppMove : oppositeMoves) {
            for (ChessMove tempTeamMove : teamMoves) {
                ChessPosition teamPosition = tempTeamMove.getEndPosition();
                ChessPosition oppPosition = tempOppMove.getEndPosition();
                if(teamPosition.equals(oppPosition)) {
                    return false;
                }
            }
        }
        return true;
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
