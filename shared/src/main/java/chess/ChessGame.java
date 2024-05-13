package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessGame {

    public ChessGame() {
        thisBoard = new ChessBoard();
        thisBoard.resetBoard();
        currentTeam = TeamColor.WHITE;
    }

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
        if(!isInCheck(teamColor)){
            Collection<ChessPosition> teamPieces = thisBoard.getTeamPieces(teamColor);
            Collection<ChessMove> allMoves = new ArrayList<>();
            for (ChessPosition tempPosition : teamPieces) {
                Collection<ChessMove> tempMoves = validMoves(tempPosition);
                if(tempMoves != null) {
                    allMoves.addAll(tempMoves);
                }
            }
            return allMoves.isEmpty();
        }
        else {
            return false;
        }
    }

    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> allMoves = getTeamMoves(getOppositeTeam(teamColor));
        ChessPiece tempPiece;
        for (ChessMove tempMove : allMoves) {
            ChessPosition tempPosition = tempMove.getEndPosition();
            if(testBoard !=null) {
                tempPiece = testBoard.getPiece(tempPosition);
            }
            else {
                tempPiece = thisBoard.getPiece(tempPosition);
            }
            if(tempPiece != null && tempPiece.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        return false;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)) {
            return false;
        }
        Collection<ChessMove> teamMoves = getTeamMoves(teamColor);
        Collection<ChessMove> allMoves = new ArrayList<>();
        for (ChessMove tempMove : teamMoves) {
            ChessPosition tempPosition = tempMove.getStartPosition();
            Collection<ChessMove> tempMoves = validMoves(tempPosition);
            if(tempMoves != null) {
                allMoves.addAll(tempMoves);
            }
        }
        return allMoves.isEmpty();
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

//    private boolean castlingValid(ChessMove move) {
//        /* Neither the King nor Rook have moved since the game started */
//        if(((currentTeam == TeamColor.WHITE && move.getStartPosition().getRow() == 1) || (currentTeam == TeamColor.BLACK && move.getStartPosition().getRow() == 8)) && move.getStartPosition().getColumn() == 5) {
//            ChessPiece tempPiece = thisBoard.getPiece(move.getStartPosition());
//            if(tempPiece.getTeamColor() != currentTeam || tempPiece.getPieceType() != ChessPiece.PieceType.KING) {
//                return false;
//            }
//            ChessPosition tempPosition;
//            ChessPosition otherTempPosition;
//            if(currentTeam == TeamColor.WHITE) {
//                tempPosition = new ChessPosition(1, 1);
//                otherTempPosition = new ChessPosition(1, 8);
//            }
//            else {
//                tempPosition = new ChessPosition(8, 1);
//                otherTempPosition = new ChessPosition(8, 8);
//            }
//            boolean moved;
//            tempPiece = thisBoard.getPiece(tempPosition);
//            if(tempPiece.getTeamColor() != currentTeam || tempPiece.getPieceType() != ChessPiece.PieceType.ROOK) {
//                moved = true;
//                tempPiece = thisBoard.getPiece(otherTempPosition);
//                if(tempPiece.getTeamColor() != currentTeam || tempPiece.getPieceType() != ChessPiece.PieceType.ROOK) {
//                    if (moved) {
//                        return false;
//                    }
//                }
//            }
//
//        }
//        /* There are no pieces between the King and the Rook */
//        if(currentTeam == TeamColor.WHITE) {
//
//        }
//
//
//    }

}
