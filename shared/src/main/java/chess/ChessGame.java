package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessGame {

    public ChessGame() {}

    public enum TeamColor {
        WHITE,
        BLACK
    }

    public void setBoard(ChessBoard board) {
        thisBoard = board;
    }

    public ChessBoard getBoard() {
        return thisBoard;
    }

    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    public void setTeamTurn(TeamColor team) {
        currentTeam = team;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        if(currentTeam == teamColor) {
            return getTeamMoves(teamColor).isEmpty();
        }
        return false;
    }

    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> allMoves = getTeamMoves(getOppositeTeam(teamColor));
        for (ChessMove tempMove : allMoves) {
            ChessPosition tempPosition = tempMove.getEndPosition();
            ChessPiece tempPiece = thisBoard.getPiece(tempPosition);
            if(tempPiece != null && tempPiece.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        return false;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> oppositeMoves = getTeamMoves(getOppositeTeam(teamColor));
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

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        if(thisBoard.getPiece(startPosition) != null) {
            ChessPiece thisPiece = thisBoard.getPiece(startPosition);
            TeamColor team = thisPiece.getTeamColor();
            if(isInStalemate(team)) {
                return validMoves;
            }
            Collection<ChessMove> tempMoves = thisPiece.pieceMoves(thisBoard,startPosition);
            for(ChessMove move : tempMoves) {
                if(!moveMakesCheck(move)) {
                    validMoves.add(move);
                }
            }
            return validMoves;
        }
        return null;
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece tempPiece = thisBoard.getPiece(move.getStartPosition());
        if(tempPiece == null || tempPiece.getTeamColor() != currentTeam) {
            throw new InvalidMoveException();
        }
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (!validMoves.isEmpty()) {
            for (ChessMove validMove : validMoves) {
                if (validMove.getEndPosition().equals(move.getEndPosition())) {
                    if(isInCheck(getOppositeTeam(tempPiece.getTeamColor()))) {
                        throw new InvalidMoveException();
                    }
                    else if(isInStalemate(getOppositeTeam(tempPiece.getTeamColor()))){
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

    private ChessBoard thisBoard;

    private TeamColor currentTeam;

    private TeamColor getOppositeTeam(TeamColor teamColor) {
        if(teamColor == TeamColor.BLACK) {
            return TeamColor.WHITE;
        }
        else{
            return TeamColor.BLACK;
        }
    }

    private boolean moveMakesCheck(ChessMove move){
        ChessPiece tempPiece = thisBoard.getPiece(move.getStartPosition());
        TeamColor teamColor = tempPiece.getTeamColor();
        thisBoard.addPiece(move.getEndPosition(),tempPiece);
        thisBoard.removePiece(move.getStartPosition());
        if(isInCheck(teamColor)){
            thisBoard.removePiece(move.getEndPosition());
            thisBoard.addPiece(move.getStartPosition(),tempPiece);
            return true;
        }
        thisBoard.removePiece(move.getEndPosition());
        thisBoard.addPiece(move.getStartPosition(),tempPiece);
        return false;
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

}
