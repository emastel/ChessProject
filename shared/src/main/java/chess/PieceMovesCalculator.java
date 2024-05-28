package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {
    private ChessBoard thisBoard;

    private ChessPosition thisPosition;

    public PieceMovesCalculator(ChessBoard board, ChessPosition position) {
        thisBoard = board;
        thisPosition = position;
    }

    private boolean rangeCheck (int row, int col) {
        return (row<=8 && col<=8 && row>=1 && col>=1);
    }

    private ArrayList<ChessMove> diagonal(ArrayList<ChessMove> validMoves, int tempRow, int tempColumn, int team, int upDown, int leftRight) {
        while(rangeCheck(tempRow, tempColumn)) {
            tempRow -= team * upDown;
            tempColumn += leftRight;
            if(tempRow>8 || tempRow<1 || tempColumn>8 || tempColumn<1) {
                break;
            }
            if(!isTaken(tempRow,tempColumn) || enemyPresent(tempRow,tempColumn))
            {
                validMoves.add(makeNewMove(tempRow,tempColumn, null));
            }
            if(enemyPresent(tempRow,tempColumn) || isTaken(tempRow,tempColumn)) {
                break;
            }
        }
        return validMoves;
    }

    private ChessMove makeNewMove(int row, int col, ChessPiece.PieceType type) {
        ChessPosition newPosition = new ChessPosition(row,col);
        new ChessMove(thisPosition,newPosition,type);
        return new ChessMove(thisPosition,newPosition,type);
    }

    private boolean enemyPresent(int row, int col) {
        ChessPosition newPosition = new ChessPosition(row,col);
        if(thisBoard.getPiece(newPosition) != null)
        {
            ChessPiece newPiece = thisBoard.getPiece(newPosition);
            ChessPiece thisPiece = thisBoard.getPiece(thisPosition);
            return newPiece.getTeamColor() != thisPiece.getTeamColor();
        }
        return false;
    }

    private boolean isTaken(int row, int col) {
        ChessPosition newPosition = new ChessPosition(row,col);
        return thisBoard.getPiece(newPosition) != null;
    }

    private int getDirection(ChessGame.TeamColor teamColor) {
        int direction;
        if(teamColor == ChessGame.TeamColor.WHITE) {
            direction = 1;
        }
        else {
            direction = -1;
        }
        return direction;
    }

    public Collection<ChessMove> bishopMoves() {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int tempRow = thisPosition.getRow();
        int tempColumn = thisPosition.getColumn();
        int direction = getDirection(thisBoard.getPiece(thisPosition).getTeamColor());
        /* right down */
        validMoves.addAll(diagonal(validMoves,tempRow,tempColumn,direction, -1, 1));
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* right up */
        validMoves.addAll(diagonal(validMoves,tempRow,tempColumn,direction, 1, 1));
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* left up */
        validMoves.addAll(diagonal(validMoves,tempRow,tempColumn,direction, 1, -1));
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* left down */
        validMoves.addAll(diagonal(validMoves,tempRow,tempColumn,direction, -1, -1));
        return validMoves;
    }

    public Collection<ChessMove> kingMoves() {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int tempRow = thisPosition.getRow();
        int tempColumn = thisPosition.getColumn();
        /* Up */
        if(tempRow+1<=8&&(!isTaken(tempRow+1,tempColumn) || enemyPresent(tempRow+1,tempColumn))) {
            validMoves.add(makeNewMove(tempRow+1,tempColumn, null));
        }
        /* Left diagonal up */
        if(tempRow-1>=1&&tempColumn-1>=1&&(!isTaken(tempRow-1,tempColumn-1) || enemyPresent(tempRow-1,tempColumn-1))) {
            validMoves.add(makeNewMove(tempRow - 1, tempColumn - 1, null));
        }
        /* Right diagonal up */
        if((tempRow-1>=1&&tempColumn+1<=8)&&(!isTaken(tempRow-1,tempColumn+1) || enemyPresent(tempRow-1,tempColumn+1))) {
            validMoves.add(makeNewMove(tempRow - 1, tempColumn + 1, null));
        }
        /* Left */
        if(tempColumn-1>=1&&(!isTaken(tempRow,tempColumn-1) || enemyPresent(tempRow,tempColumn-1))) {
            validMoves.add(makeNewMove(tempRow, tempColumn - 1, null));
        }
        /* Right */
        if(tempColumn+1<=8&&(!isTaken(tempRow,tempColumn+1) || enemyPresent(tempRow,tempColumn+1))) {
            validMoves.add(makeNewMove(tempRow, tempColumn + 1, null));
        }
        /* Down */
        if(tempRow-1>=1&&(!isTaken(tempRow-1,tempColumn) || enemyPresent(tempRow-1,tempColumn))) {
            validMoves.add(makeNewMove(tempRow - 1, tempColumn, null));
        }
        /* Left diagonal down */
        if((tempColumn-1>=1&&tempRow+1<=8)&&(!isTaken(tempRow+1,tempColumn-1) || enemyPresent(tempRow+1,tempColumn-1))) {
            validMoves.add(makeNewMove(tempRow + 1, tempColumn - 1, null));
        }
        /* Right diagonal down */
        if((tempRow+1<=8&&tempColumn+1<=8)&&(!isTaken(tempRow+1,tempColumn+1) || enemyPresent(tempRow+1,tempColumn+1))) {
            validMoves.add(makeNewMove(tempRow + 1, tempColumn + 1, null));
        }
        return validMoves;
    }

    private  ArrayList<ChessMove> knightPosition(ArrayList<ChessMove> validMoves, int tempRow, int tempColumn, int team, boolean up, int leftRight, boolean high) {
        if(high){
            if(up) {
                tempRow += 2*team;
            }
            else
            {
                tempRow -= 2*team;
            }
            tempColumn += leftRight;
        }
        else {
            if(up) {
                tempRow += team;
            }
            else
            {
                tempRow -= team;
            }
            tempColumn += 2*leftRight;
        }
        if((rangeCheck(tempRow,tempColumn))&&(!isTaken(tempRow,tempColumn) || enemyPresent(tempRow,tempColumn)))
        {
            validMoves.add(makeNewMove(tempRow,tempColumn, null));
        }
        return validMoves;
    }
    public Collection<ChessMove> knightMoves(){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int tempRow = thisPosition.getRow();
        int tempColumn = thisPosition.getColumn();
        int direction = getDirection(thisBoard.getPiece(thisPosition).getTeamColor());
        /* Up Left Highest */
        validMoves.addAll(knightPosition(validMoves,tempRow,tempColumn,direction,true,-1,true));
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* Up Left Lowest */
        validMoves.addAll(knightPosition(validMoves,tempRow,tempColumn,direction,true,-1,false));
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* Up Right Highest */
        validMoves.addAll(knightPosition(validMoves,tempRow,tempColumn,direction,true,1,true));
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* Up Right Lowest */
        validMoves.addAll(knightPosition(validMoves,tempRow,tempColumn,direction,true,1,false));
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* Down Left Lowest */
        validMoves.addAll(knightPosition(validMoves,tempRow,tempColumn,direction,false,-1,true));
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* Down Left Highest */
        validMoves.addAll(knightPosition(validMoves,tempRow,tempColumn,direction,false,-1,false));
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* Down Right Lowest */
        validMoves.addAll(knightPosition(validMoves,tempRow,tempColumn,direction,false,1,true));
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* Down Right Highest */
        validMoves.addAll(knightPosition(validMoves,tempRow,tempColumn,direction,false,1,false));
        return validMoves;
    }

    private ArrayList<ChessMove> pawnPromotion(ArrayList<ChessMove> validMoves, int tempRow, int tempColumn, ChessGame.TeamColor teamColor, boolean diagonal)
    {
        if(rangeCheck(tempRow,tempColumn)){
            if((enemyPresent(tempRow,tempColumn) && diagonal) || (!enemyPresent(tempRow,tempColumn) && !diagonal && !isTaken(tempRow,tempColumn))) {
                if((tempRow==8 && teamColor== ChessGame.TeamColor.WHITE)||(tempRow==1 && teamColor==ChessGame.TeamColor.BLACK)) {
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.QUEEN));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.BISHOP));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.ROOK));
                }
                else{
                    validMoves.add(makeNewMove(tempRow,tempColumn, null));
                }
            }
        }
        return validMoves;
    }

    public Collection<ChessMove> pawnMoves(){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int tempRow = thisPosition.getRow();
        int tempColumn = thisPosition.getColumn();
        ChessPiece newPiece = thisBoard.getPiece(thisPosition);
        ChessGame.TeamColor teamColor = newPiece.getTeamColor();
        int direction = getDirection(teamColor);
        /* Up One */
        tempRow += direction;
        validMoves.addAll(pawnPromotion(validMoves,tempRow,tempColumn,teamColor,false));
        /* Diagonal Right */
        tempColumn += 1;
        validMoves.addAll(pawnPromotion(validMoves,tempRow,tempColumn,teamColor,true));
        tempColumn = thisPosition.getColumn();
        /* Diagonal Left */
        tempColumn -= 1;
        validMoves.addAll(pawnPromotion(validMoves,tempRow,tempColumn,teamColor,true));
        tempColumn = thisPosition.getColumn();
        tempRow = thisPosition.getRow();
        if((tempRow == 2 && teamColor == ChessGame.TeamColor.WHITE) || (tempRow == 7 && teamColor == ChessGame.TeamColor.BLACK)) {
            /* Up Two */
            tempRow += 2 * direction;
            if((!isTaken(tempRow,tempColumn)&&!isTaken(tempRow-direction,tempColumn)) && !enemyPresent(tempRow,tempColumn))
            {
                validMoves.add(makeNewMove(tempRow,tempColumn, null));
            }
        }
        return validMoves;
    }

    public Collection<ChessMove> queenMoves(){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(rookMoves());
        validMoves.addAll(bishopMoves());
        return validMoves;
    }

    private ArrayList<ChessMove> rookPosition(ArrayList<ChessMove> validMoves, int tempRow, int tempColumn, int direction, int upDown)
    {
        while(tempRow<=8 && tempColumn<=8 && tempRow>=1 && tempColumn>=1) {
            tempRow += direction*upDown;
            if(tempRow>8 || tempRow<1) {
                break;
            }
            if(!isTaken(tempRow,tempColumn) || enemyPresent(tempRow,tempColumn))
            {
                validMoves.add(makeNewMove(tempRow,tempColumn, null));
            }
            if(enemyPresent(tempRow,tempColumn)||isTaken(tempRow,tempColumn)) {
                break;
            }
        }
        return validMoves;
    }

    public Collection<ChessMove> rookMoves(){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int tempRow = thisPosition.getRow();
        int tempColumn = thisPosition.getColumn();
        int direction = getDirection(thisBoard.getPiece(thisPosition).getTeamColor());
        /* Up */
        validMoves.addAll(rookPosition(validMoves,tempRow,tempColumn,direction,1));
        tempRow = thisPosition.getRow();
        /* Down */
        validMoves.addAll(rookPosition(validMoves,tempRow,tempColumn,direction,-1));
        tempRow = thisPosition.getRow();
        /* Right */
        while(tempRow<=8 && tempColumn<=8 && tempRow>=1 && tempColumn>=1) {
            ++tempColumn;
            if(tempColumn>8) {
                break;
            }
            if(!isTaken(tempRow,tempColumn) || enemyPresent(tempRow,tempColumn)) {
                validMoves.add(makeNewMove(tempRow,tempColumn, null));
            }
            if(enemyPresent(tempRow,tempColumn)||isTaken(tempRow,tempColumn)) {
                break;
            }
        }
        tempColumn = thisPosition.getColumn();
        /* Left */
        while(tempRow<=8 && tempColumn<=8 && tempRow>=1 && tempColumn>=1) {
            --tempColumn;
            if(tempColumn<1) {
                break;
            }
            if(!isTaken(tempRow,tempColumn) || enemyPresent(tempRow,tempColumn)) {
                validMoves.add(makeNewMove(tempRow,tempColumn, null));
            }
            if(enemyPresent(tempRow,tempColumn)||isTaken(tempRow,tempColumn)) {
                break;
            }
        }
        return validMoves;
    }
}