package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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
        ChessPosition tempPosition;
        ChessMove tempMove;
        if(thisBoard.getPiece(startPosition) != null) {
            ChessPiece thisPiece = thisBoard.getPiece(startPosition);
            Collection<ChessMove> tempMoves = thisPiece.pieceMoves(thisBoard,startPosition);
            /* en Passant */
            if(thisPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                int direction;
                if(currentTeam == TeamColor.WHITE) {
                    direction = 1;
                }
                else {
                    direction = -1;
                }
                /* Left */
                if(startPosition.getRow()+direction <= 8 && startPosition.getRow()+direction >=1 && startPosition.getColumn()-1 >=1)
                {
                    tempPosition = new ChessPosition(startPosition.getRow()+direction, startPosition.getColumn()-1);
                    tempMove = new ChessMove(startPosition, tempPosition, null);
                    if(enPassantValid(tempMove)) {
                        validMoves.add(tempMove);
                    }
                }
                /* Right */
                if(startPosition.getRow()+direction <= 8 && startPosition.getRow()+direction >=1 && startPosition.getColumn()+1 <=8) {
                    tempPosition = new ChessPosition(startPosition.getRow()+direction, startPosition.getColumn()+1);
                    tempMove = new ChessMove(startPosition, tempPosition, null);
                    if(enPassantValid(tempMove)) {
                        validMoves.add(tempMove);
                    }
                }
            }
            /* Castling */
            if(thisPiece.getPieceType() == ChessPiece.PieceType.KING) {
                boolean valid = true;
                if(currentTeam == TeamColor.WHITE) {
                    if(kingMovedWhite || (startPosition.getRow() != 1 && startPosition.getColumn() != 5)) {
                        valid = false;
                    }
                }
                else {
                    if(kingMovedBlack || (startPosition.getRow() != 8 && startPosition.getColumn() != 5)) {
                        valid = false;
                    }
                }
                if(valid)
                {
                    /* Left */
                    tempPosition = new ChessPosition(startPosition.getRow(),startPosition.getColumn()-2);
                    tempMove = new ChessMove(startPosition, tempPosition, null);
                    if(castlingValid(tempMove)) {
                        validMoves.add(tempMove);
                    }
                    /* Right */
                    tempPosition = new ChessPosition(startPosition.getRow(),startPosition.getColumn()+2);
                    tempMove = new ChessMove(startPosition, tempPosition, null);
                    if(castlingValid(tempMove)) {
                        validMoves.add(tempMove);
                    }
                }

            }
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
                    /* en Passant */
                    if(tempPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                        ChessPosition oppPosition;
                        ChessPiece oppPiece;
                        if(currentTeam == TeamColor.WHITE) {
                            doubleMove = move.getStartPosition().getRow() + 2 == move.getEndPosition().getRow();
                        }
                        else {
                            doubleMove = move.getStartPosition().getRow() - 2 == move.getEndPosition().getRow();
                        }
                        /* Left */
                        if(move.getStartPosition().getColumn()-1 >= 1) {
                            oppPosition = new ChessPosition(move.getStartPosition().getRow(), move.getStartPosition().getColumn()-1);
                            oppPiece = thisBoard.getPiece(oppPosition);
                            if(oppPiece != null && oppPiece.getTeamColor() != currentTeam) {
                                thisBoard.removePiece(oppPosition);
                            }
                        }
                        /* Right */
                        if(move.getStartPosition().getColumn()+1 <= 8) {
                            oppPosition = new ChessPosition(move.getStartPosition().getRow(), move.getStartPosition().getColumn()+1);
                            oppPiece = thisBoard.getPiece(oppPosition);
                            if(oppPiece != null && oppPiece.getTeamColor() != currentTeam) {
                                thisBoard.removePiece(oppPosition);
                            }
                        }
                    }
                    else {
                        doubleMove = false;
                    }
                    /* Castling */
                    if(tempPiece.getPieceType() == ChessPiece.PieceType.KING) {
                        if(currentTeam == TeamColor.WHITE) {
                            if(move.getEndPosition().getColumn() != 7 && move.getEndPosition().getColumn() != 3) {
                                kingMovedWhite = true;
                            }
                            if(!kingMovedWhite) {
                                if(move.getEndPosition().getColumn() == 7) {
                                    ChessPosition start = new ChessPosition(1,8);
                                    ChessPosition end = new ChessPosition(1,6);
                                    ChessPiece rook = thisBoard.getPiece(start);
                                    thisBoard.removePiece(start);
                                    thisBoard.addPiece(end,rook);
                                }
                                else if (move.getEndPosition().getColumn() == 3) {
                                    ChessPosition start = new ChessPosition(1,1);
                                    ChessPosition end = new ChessPosition(1,4);
                                    ChessPiece rook = thisBoard.getPiece(start);
                                    thisBoard.removePiece(start);
                                    thisBoard.addPiece(end,rook);
                                }
                            }
                        }
                        else {
                            if(move.getEndPosition().getColumn() != 7 && move.getEndPosition().getColumn() != 3) {
                                kingMovedBlack = true;
                            }
                            if(!kingMovedBlack) {
                                if(move.getEndPosition().getColumn() == 7) {
                                    ChessPosition start = new ChessPosition(8,8);
                                    ChessPosition end = new ChessPosition(8,6);
                                    ChessPiece rook = thisBoard.getPiece(start);
                                    thisBoard.removePiece(start);
                                    thisBoard.addPiece(end,rook);
                                }
                                else if (move.getEndPosition().getColumn() == 3) {
                                    ChessPosition start = new ChessPosition(8,1);
                                    ChessPosition end = new ChessPosition(8,4);
                                    ChessPiece rook = thisBoard.getPiece(start);
                                    thisBoard.removePiece(start);
                                    thisBoard.addPiece(end,rook);
                                }
                            }
                        }
                    }
                }
            }
            if(thisBoard.getPiece(move.getStartPosition()) != null) {
                throw new InvalidMoveException();
            }
        }
        else {
            throw new InvalidMoveException();
        }
        if(tempPiece.getPieceType() == ChessPiece.PieceType.ROOK) {
            if(currentTeam == TeamColor.WHITE) {
                if(move.getStartPosition().getRow() == 1 && move.getStartPosition().getColumn() == 1) {
                    leftRookMovedWhite = true;
                }
                else if(move.getStartPosition().getRow() == 1 && move.getStartPosition().getColumn() == 8) {
                    rightRookMovedWhite = true;
                }
            }
            else {
                if(move.getStartPosition().getRow() == 8 && move.getStartPosition().getColumn() == 1) {
                    leftRookMovedBlack = true;
                }
                else if(move.getStartPosition().getRow() == 8 && move.getStartPosition().getColumn() == 8) {
                    rightRookMovedBlack = true;
                }
            }
        }
        currentTeam = getOppositeTeam(currentTeam);
    }

    private ChessBoard thisBoard;

    private TeamColor currentTeam;

    private ChessBoard testBoard;

    private boolean rightRookMovedWhite;

    private boolean leftRookMovedWhite;

    private boolean rightRookMovedBlack;

    private boolean leftRookMovedBlack;

    private boolean kingMovedWhite;

    private boolean kingMovedBlack;

    private boolean doubleMove;

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

    private boolean castlingValid(ChessMove move) {
        ChessPiece thisPiece = thisBoard.getPiece(move.getStartPosition());
        TeamColor teamColor = thisPiece.getTeamColor();
        if(isInCheck(teamColor)) {
            return false;
        }
        boolean leftValid = true;
        boolean rightValid = true;
        int range;
        ChessPiece king = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        ChessPiece rook = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
        if(teamColor == TeamColor.WHITE) {
            range = 1;
            if(kingMovedWhite || (move.getStartPosition().getRow() != 1 || move.getStartPosition().getColumn() != 5)) {
                return false;
            }
        }
        else {
            range = 8;
            if(kingMovedBlack || (move.getStartPosition().getRow() != 8 || move.getStartPosition().getColumn() != 5)) {
                return false;
            }
        }
        /* Neither the King nor Rook have moved since the game started */
        if(((teamColor == TeamColor.WHITE && move.getStartPosition().getRow() == 1) || (teamColor == TeamColor.BLACK && move.getStartPosition().getRow() == 8)) && move.getStartPosition().getColumn() == 5) {
            king = thisBoard.getPiece(move.getStartPosition());
            if(king.getTeamColor() != teamColor || king.getPieceType() != ChessPiece.PieceType.KING) {
                return false;
            }
            if(teamColor == TeamColor.WHITE) {
                ChessPosition tempPosition = new ChessPosition(1,8);
                ChessPiece tempRook = thisBoard.getPiece(tempPosition);
                if(rightRookMovedWhite || (tempRook != null && tempRook.getPieceType() != ChessPiece.PieceType.ROOK)) {
                    rightValid = false;
                }
                tempPosition = new ChessPosition(1,1);
                tempRook = thisBoard.getPiece(tempPosition);
                if(leftRookMovedWhite || (tempRook != null && tempRook.getPieceType() != ChessPiece.PieceType.ROOK)) {
                    leftValid = false;
                }
            }
            else {
                ChessPosition tempPosition = new ChessPosition(8,8);
                ChessPiece tempRook = thisBoard.getPiece(tempPosition);
                if(rightRookMovedBlack || (tempRook != null && tempRook.getPieceType() != ChessPiece.PieceType.ROOK)) {
                    rightValid = false;
                }
                tempPosition = new ChessPosition(8,1);
                tempRook = thisBoard.getPiece(tempPosition);
                if(leftRookMovedBlack || (tempRook != null && tempRook.getPieceType() != ChessPiece.PieceType.ROOK)) {
                    leftValid = false;
                }
            }
            if(!leftValid && !rightValid) {
                return false;
            }
        }
        /* There are no pieces between the King and the Rook */
        if(leftValid){
            for(int i=2; i<5; ++i) {
                ChessPiece tempPiece = thisBoard.getPiece(new ChessPosition(range, i));
                if(tempPiece != null) {
                    return false;
                }
            }
        }
        if(rightValid){
            for(int i=6; i<8; ++i) {
                ChessPiece tempPiece = thisBoard.getPiece(new ChessPosition(range, i));
                if(tempPiece != null) {
                    return false;
                }
            }
        }
        /* King can't move towards moved rook */
        if(move.getEndPosition().getColumn() == 3){
            if(!leftValid) {
                return false;
            }
        }
        if(move.getEndPosition().getColumn() == 7){
            if(!rightValid) {
                return false;
            }
        }
        /* Both Rook and King are safe after move */
        try {
            testBoard = (ChessBoard) thisBoard.clone();
            ChessPosition rookStart = null;
            ChessPosition rookEnd = null;
            /* Left */
            if(leftValid) {
                testBoard.addPiece(move.getEndPosition(), king);
                testBoard.removePiece(move.getStartPosition());
                rookStart = new ChessPosition(range, 1);
                rookEnd = new ChessPosition(range, move.getEndPosition().getColumn()-1);
                testBoard.addPiece(rookEnd, rook);
                testBoard.removePiece(rookStart);
                if (isInCheck(teamColor)) {
                    return false;
                }
            }
            /* Right */
            if(rightValid) {
                testBoard.addPiece(move.getEndPosition(), king);
                testBoard.removePiece(move.getStartPosition());
                rookStart = new ChessPosition(range, 8);
                rookEnd = new ChessPosition(range, move.getEndPosition().getColumn()+1);
                testBoard.addPiece(rookEnd, rook);
                testBoard.removePiece(rookStart);
                if (isInCheck(teamColor)) {
                    return false;
                }
            }
            Collection<ChessMove> oppMoves = getTeamMoves(getOppositeTeam(teamColor));
            for (ChessMove oppMove : oppMoves) {
                if(oppMove.getEndPosition() == rookEnd) {
                    return false;
                }
            }
        } catch (CloneNotSupportedException ex){}
        return true;
    }

    private boolean enPassantValid(ChessMove move){
        if(doubleMove) {
            ChessPosition tempPosition = new ChessPosition(move.getStartPosition().getRow(), move.getEndPosition().getColumn());
            ChessPiece enemyPiece = thisBoard.getPiece(tempPosition);
            return enemyPiece != null;
        }
        return false;
    }
}


