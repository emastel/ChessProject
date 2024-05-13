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
            Collection<ChessPosition> teamPieces = thisBoard.getTeamPieces(teamColor);
            Collection<ChessMove> allMoves = new ArrayList<>();
            for (ChessPosition tempPosition : teamPieces) {
                allMoves.addAll(validMoves(tempPosition));
            }
            return allMoves.isEmpty();
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
        if(tempPiece == null || tempPiece.getTeamColor() != currentTeam || isInStalemate(tempPiece.getTeamColor())) {
            throw new InvalidMoveException();
        }
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (!validMoves.isEmpty()) {
            for (ChessMove validMove : validMoves) {
                if (validMove.equals(move)){
                    thisBoard.removePiece(move.getStartPosition());
                    if(move.getPromotionPiece() != null)
                    {
                        tempPiece = new ChessPiece(currentTeam, move.getPromotionPiece());
                    }
                    thisBoard.addPiece(move.getEndPosition(),tempPiece);
                    currentTeam = getOppositeTeam(currentTeam);
                }
            }
            if(thisBoard.getPiece(move.getStartPosition()) != null) {
                throw new InvalidMoveException();
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
        Collection<ChessPosition> teamPieces;
        Collection<ChessMove> allMoves = new ArrayList<>();
        if(testBoard != null) {
            teamPieces = testBoard.getTeamPieces(teamColor);
            allMoves = new ArrayList<>();
            for (ChessPosition tempPosition : teamPieces) {
                ChessPiece tempPiece = testBoard.getPiece(tempPosition);
                Collection<ChessMove> tempMoves = tempPiece.pieceMoves(testBoard, tempPosition);
                allMoves.addAll(tempMoves);
            }
        }
        else {
            teamPieces = thisBoard.getTeamPieces(teamColor);
            for (ChessPosition tempPosition : teamPieces) {
                ChessPiece tempPiece = thisBoard.getPiece(tempPosition);
                Collection<ChessMove> tempMoves = tempPiece.pieceMoves(thisBoard, tempPosition);
                allMoves.addAll(tempMoves);
            }
        }
        return allMoves;
    }

}
