package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {
    private ChessBoard thisBoard;
    private ChessPosition thisPosition;
    public PieceMovesCalculator(ChessBoard board, ChessPosition position)
    {
        thisBoard = board;
        thisPosition = position;
    }
    private ChessMove makeNewMove(int row, int col, ChessPiece.PieceType type)
    {
        ChessPosition newPosition = new ChessPosition(row,col);
        new ChessMove(thisPosition,newPosition,type);
        return new ChessMove(thisPosition,newPosition,type);
    }
    private boolean enemyPresent(int row, int col)
    {
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
    public Collection<ChessMove> BishopMoves()
    {
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
        validMoves.add(makeNewMove(tempRow+1,tempColumn, null));
        /* Left diagonal up */
        validMoves.add(makeNewMove(tempRow-1,tempColumn-1, null));
        /* Right diagonal up */
        validMoves.add(makeNewMove(tempRow-1,tempColumn+1, null));
        /* Left */
        validMoves.add(makeNewMove(tempRow,tempColumn-1, null));
        /* Right */
        validMoves.add(makeNewMove(tempRow,tempColumn+1, null));
        /* Down */
        validMoves.add(makeNewMove(tempRow-1,tempColumn, null));
        /* Left diagonal down */
        validMoves.add(makeNewMove(tempRow+1,tempColumn-1, null));
        /* Right diagonal down */
        validMoves.add(makeNewMove(tempRow+1,tempColumn+1, null));
        return validMoves;
    }
}