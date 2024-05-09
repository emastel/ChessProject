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
            ChessPiece tempPiece = testBoard.getPiece(tempPosition);
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
        if(thisBoard == null) {
            throw new InvalidMoveException();
        }
        ChessPiece tempPiece = thisBoard.getPiece(move.getStartPosition());
        if(tempPiece == null || tempPiece.getTeamColor() != currentTeam) {
            throw new InvalidMoveException();
        }
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (!validMoves.isEmpty()) {
            for (ChessMove validMove : validMoves) {
                if (validMove.equals(move)){
                    thisBoard.removePiece(move.getStartPosition());
                    thisBoard.addPiece(move.getEndPosition(),tempPiece);
                }
            }
        }
        else {
            throw new InvalidMoveException();
        }
    }

    private ChessBoard thisBoard;

    private TeamColor currentTeam;

    private ChessBoard testBoard;

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
        try {
            testBoard = (ChessBoard) thisBoard.clone();
            testBoard.addPiece(move.getEndPosition(), tempPiece);
            testBoard.removePiece(move.getStartPosition());
            if (isInCheck(teamColor)) {
                return true;
            }
        } catch (CloneNotSupportedException ex){}
        return false;
    }

    private Collection<ChessMove> getTeamMoves(TeamColor teamColor) {
        Collection<ChessPosition> teamPieces = testBoard.getTeamPieces(teamColor);
        Collection<ChessMove> allMoves = new ArrayList<>();
        for (ChessPosition tempPosition : teamPieces) {
            ChessPiece tempPiece = testBoard.getPiece(tempPosition);
            Collection<ChessMove> tempMoves = tempPiece.pieceMoves(testBoard, tempPosition);
            allMoves.addAll(tempMoves);
        }
        return allMoves;
    }

}
