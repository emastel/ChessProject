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

    public Collection<ChessMove> BishopMoves() {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int tempRow = thisPosition.getRow();
        int tempColumn = thisPosition.getColumn();
        /* right down */
        while(tempRow<8 && tempColumn<8 && tempRow>1 && tempColumn>1) {
            ++tempRow;
            ++tempColumn;
            if(!isTaken(tempRow,tempColumn) || enemyPresent(tempRow,tempColumn))
            {
                validMoves.add(makeNewMove(tempRow,tempColumn, null));
            }
            if(enemyPresent(tempRow,tempColumn)) {
                break;
            }
        }
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* right up */
        while(tempRow<8 && tempColumn<8 && tempRow>1 && tempColumn>1) {
            --tempRow;
            ++tempColumn;
            if(!isTaken(tempRow,tempColumn) || enemyPresent(tempRow,tempColumn))
            {
                validMoves.add(makeNewMove(tempRow,tempColumn, null));
            }
            if(enemyPresent(tempRow,tempColumn)) {
                break;
            }
        }
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* left up */
        while(tempRow<8 && tempColumn<8 && tempRow>1 && tempColumn>1) {
            --tempRow;
            --tempColumn;
            if(!isTaken(tempRow,tempColumn) || enemyPresent(tempRow,tempColumn))
            {
                validMoves.add(makeNewMove(tempRow,tempColumn, null));
            }
            if(enemyPresent(tempRow,tempColumn)) {
                break;
            }
        }
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* left down */
        while(tempRow<8 && tempColumn<8 && tempRow>1 && tempColumn>1) {
            ++tempRow;
            --tempColumn;
            if(!isTaken(tempRow,tempColumn) || enemyPresent(tempRow,tempColumn))
            {
                validMoves.add(makeNewMove(tempRow,tempColumn, null));
            }
            if(enemyPresent(tempRow,tempColumn)) {
                break;
            }
        }
        return validMoves;
    }

    public Collection<ChessMove> KingMoves() {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int tempRow = thisPosition.getRow();
        int tempColumn = thisPosition.getColumn();
        /* Up */
        if(tempRow+1<=8&&(!isTaken(tempRow+1,tempColumn) || enemyPresent(tempRow+1,tempColumn))) {
            validMoves.add(makeNewMove(tempRow+1,tempColumn, null));
        }
        /* Left diagonal up */
        if(tempRow-1>=1&&(!isTaken(tempRow-1,tempColumn-1) || enemyPresent(tempRow-1,tempColumn-1))) {
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

    public Collection<ChessMove> KnightMoves(){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int tempRow = thisPosition.getRow();
        int tempColumn = thisPosition.getColumn();
        /* Up Left Highest */
        tempRow += 2;
        tempColumn -= 1;
        if((tempRow<=8&&tempColumn>=1)&&(!isTaken(tempRow,tempColumn) || enemyPresent(tempRow,tempColumn)))
        {
            validMoves.add(makeNewMove(tempRow,tempColumn, null));
        }
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* Up Left Lowest */
        tempRow += 1;
        tempColumn -= 2;
        if((tempRow<=8&&tempColumn>=1)&&(!isTaken(tempRow,tempColumn) || enemyPresent(tempRow,tempColumn)))
        {
            validMoves.add(makeNewMove(tempRow,tempColumn, null));
        }
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* Up Right Highest */
        tempRow += 2;
        tempColumn += 1;
        if((tempRow<=8&&tempColumn<=8)&&(!isTaken(tempRow,tempColumn) || enemyPresent(tempRow,tempColumn)))
        {
            validMoves.add(makeNewMove(tempRow,tempColumn, null));
        }
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* Up Right Lowest */
        tempRow += 1;
        tempColumn += 2;
        if((tempRow<=8&&tempColumn<=8)&&(!isTaken(tempRow,tempColumn) || enemyPresent(tempRow,tempColumn)))
        {
            validMoves.add(makeNewMove(tempRow,tempColumn, null));
        }
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* Down Left Lowest */
        tempRow -= 2;
        tempColumn -= 1;
        if((tempRow>=1&&tempColumn>=1)&&(!isTaken(tempRow,tempColumn) || enemyPresent(tempRow,tempColumn)))
        {
            validMoves.add(makeNewMove(tempRow,tempColumn, null));
        }
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* Down Left Highest */
        tempRow -= 1;
        tempColumn -= 2;
        if((tempRow>=1&&tempColumn>=1)&&(!isTaken(tempRow,tempColumn) || enemyPresent(tempRow,tempColumn)))
        {
            validMoves.add(makeNewMove(tempRow,tempColumn, null));
        }
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* Down Right Lowest */
        tempRow -= 2;
        tempColumn += 1;
        if((tempRow>=1&&tempColumn<=8)&&(!isTaken(tempRow,tempColumn) || enemyPresent(tempRow,tempColumn)))
        {
            validMoves.add(makeNewMove(tempRow,tempColumn, null));
        }
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* Down Right Highest */
        tempRow -= 1;
        tempColumn += 2;
        if((tempRow>=1&&tempColumn<=8)&&(!isTaken(tempRow,tempColumn) || enemyPresent(tempRow,tempColumn)))
        {
            validMoves.add(makeNewMove(tempRow,tempColumn, null));
        }
        return validMoves;
    }
    public Collection<ChessMove> PawnMoves(){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int tempRow = thisPosition.getRow();
        int tempColumn = thisPosition.getColumn();
        ChessPiece newPiece = thisBoard.getPiece(thisPosition);
        /* White pieces */
        if(newPiece.getTeamColor() == ChessGame.TeamColor.WHITE)
        {
            /* Up One */
            tempRow += 1;
            if((tempRow<=8)&&(!isTaken(tempRow,tempColumn) && !enemyPresent(tempRow,tempColumn)))
            {
                if(tempRow==8) {
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.QUEEN));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.BISHOP));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.ROOK));
                }
                else{
                    validMoves.add(makeNewMove(tempRow,tempColumn, null));
                }
            }
            /* Diagonal Right */
            tempColumn += 1;
            if(tempColumn<=8&&enemyPresent(tempRow,tempColumn)) {
                if(tempRow==8) {
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.QUEEN));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.BISHOP));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.ROOK));
                }
                else{
                    validMoves.add(makeNewMove(tempRow,tempColumn, null));
                }
            }
            tempColumn = thisPosition.getColumn();
            /* Diagonal Left */
            tempColumn -= 1;
            if(tempColumn>=1&&enemyPresent(tempRow,tempColumn)) {
                if(tempRow==8) {
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.QUEEN));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.BISHOP));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.ROOK));
                }
                else{
                    validMoves.add(makeNewMove(tempRow,tempColumn, null));
                }
            }
            tempColumn = thisPosition.getColumn();
            tempRow = thisPosition.getRow();
            if(tempRow == 2) {
                /* Up Two */
                tempRow += 2;
                if((!isTaken(tempRow,tempColumn)&&!isTaken(tempRow-1,tempColumn)) && !enemyPresent(tempRow,tempColumn))
                {
                    validMoves.add(makeNewMove(tempRow,tempColumn, null));
                }
            }
        }
        /* Black Pieces */
        else
        {
            /* Up One */
            tempRow -= 1;
            if((tempRow>=1)&&(!isTaken(tempRow,tempColumn) && !enemyPresent(tempRow,tempColumn)))
            {
                if(tempRow==1) {
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.QUEEN));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.BISHOP));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.ROOK));
                }
                else{
                    validMoves.add(makeNewMove(tempRow,tempColumn,null));
                }
            }
            /* Diagonal Right */
            tempColumn += 1;
            if(tempColumn<=8&&enemyPresent(tempRow,tempColumn)) {
                if(tempRow==1) {
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.QUEEN));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.BISHOP));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.ROOK));
                }
                else{
                    validMoves.add(makeNewMove(tempRow,tempColumn, null));
                }
            }
            tempColumn = thisPosition.getColumn();
            /* Diagonal Left */
            tempColumn -= 1;
            if(tempColumn>=1&&enemyPresent(tempRow,tempColumn)) {
                if(tempRow==1) {
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.QUEEN));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.BISHOP));
                    validMoves.add(makeNewMove(tempRow,tempColumn, ChessPiece.PieceType.ROOK));
                }
                else{
                    validMoves.add(makeNewMove(tempRow,tempColumn, null));
                }
            }
            tempColumn = thisPosition.getColumn();
            tempRow = thisPosition.getRow();
            if(tempRow == 7) {
                /* Up Two */
                tempRow -= 2;
                if((!isTaken(tempRow,tempColumn)&&!isTaken(tempRow+1,tempColumn)) && !enemyPresent(tempRow,tempColumn))
                {
                    validMoves.add(makeNewMove(tempRow,tempColumn,null));
                }
            }
        }
        return validMoves;
    }

    public Collection<ChessMove> QueenMoves(){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int tempRow = thisPosition.getRow();
        int tempColumn = thisPosition.getColumn();
        /* Up */
        while(tempRow<=8 && tempColumn<=8 && tempRow>=1 && tempColumn>=1) {
            ++tempRow;
            if(tempRow>8 || tempRow<1)
            {
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
        tempRow = thisPosition.getRow();
        /* Down */
        while(tempRow<=8 && tempColumn<=8 && tempRow>=1 && tempColumn>=1) {
            --tempRow;
            if(tempRow>8 || tempRow<1)
            {
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
        tempRow = thisPosition.getRow();
        /* Right */
        while(tempRow<=8 && tempColumn<=8 && tempRow>=1 && tempColumn>=1) {
            ++tempColumn;
            if(tempColumn>8 || tempColumn<1)
            {
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
        tempColumn = thisPosition.getColumn();
        /* Left */
        while(tempRow<=8 && tempColumn<=8 && tempRow>=1 && tempColumn>=1) {
            --tempColumn;
            if(tempColumn>8 || tempColumn<1)
            {
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
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* right down */
        while(tempRow<=8 && tempColumn<=8 && tempRow>=1 && tempColumn>=1) {
            ++tempRow;
            ++tempColumn;
            if(tempRow>8 || tempColumn>8){
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
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* right up */
        while(tempRow<=8 && tempColumn<=8 && tempRow>=1 && tempColumn>=1) {
            --tempRow;
            ++tempColumn;
            if(tempRow<1 || tempColumn>8){
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
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* left up */
        while(tempRow<=8 && tempColumn<=8 && tempRow>=1 && tempColumn>=1) {
            --tempRow;
            --tempColumn;
            if(tempRow<1 || tempColumn<1){
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
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* left down */
        while(tempRow<=8 && tempColumn<=8 && tempRow>=1 && tempColumn>=1) {
            ++tempRow;
            --tempColumn;
            if(tempRow>8 || tempColumn<1){
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

    public Collection<ChessMove> RookMoves(){
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int tempRow = thisPosition.getRow();
        int tempColumn = thisPosition.getColumn();
        /* Up */
        while(tempRow<=8 && tempColumn<=8 && tempRow>=1 && tempColumn>=1) {
            ++tempRow;
            if(tempRow>8 || tempRow<1)
            {
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
        tempRow = thisPosition.getRow();
        /* Down */
        while(tempRow<=8 && tempColumn<=8 && tempRow>=1 && tempColumn>=1) {
            --tempRow;
            if(tempRow>8 || tempRow<1)
            {
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
        tempRow = thisPosition.getRow();
        /* Right */
        while(tempRow<=8 && tempColumn<=8 && tempRow>=1 && tempColumn>=1) {
            ++tempColumn;
            if(tempColumn>8 || tempColumn<1)
            {
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
        tempColumn = thisPosition.getColumn();
        /* Left */
        while(tempRow<=8 && tempColumn<=8 && tempRow>=1 && tempColumn>=1) {
            --tempColumn;
            if(tempColumn>8 || tempColumn<1)
            {
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

}