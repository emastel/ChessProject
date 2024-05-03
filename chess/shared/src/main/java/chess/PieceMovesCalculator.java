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
    public ChessMove makeNewMove(int row, int col, ChessPiece.PieceType type)
    {
        ChessPosition newPosition = new ChessPosition(row,col);
        if(thisBoard.getPiece(newPosition) == null)
        {
            new ChessMove(thisPosition,newPosition,type);
            if(type != ChessPiece.PieceType.PAWN)
            {
                return new ChessMove(thisPosition,newPosition,type);
            }
        }
        return null;
    }
    //ToDo: check if enemy piece is present
    public Collection<ChessMove> BishopMoves()
    {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int tempRow = thisPosition.getRow();
        int tempColumn = thisPosition.getColumn();
        /* right down */
        while(tempRow<8 && tempColumn<8 && tempRow>1 && tempColumn>1) {
            ++tempRow;
            ++tempColumn;
            validMoves.add(makeNewMove(tempRow,tempColumn, null));
        }
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* right up */
        while(tempRow<8 && tempColumn<8 && tempRow>1 && tempColumn>1) {
            --tempRow;
            ++tempColumn;
            validMoves.add(makeNewMove(tempRow,tempColumn,null));
        }
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* left up */
        while(tempRow<8 && tempColumn<8 && tempRow>1 && tempColumn>1) {
            --tempRow;
            --tempColumn;
            validMoves.add(makeNewMove(tempRow,tempColumn,null));
        }
        tempRow = thisPosition.getRow();
        tempColumn = thisPosition.getColumn();
        /* left down */
        while(tempRow<8 && tempColumn<8 && tempRow>1 && tempColumn>1) {
            ++tempRow;
            --tempColumn;
            validMoves.add(makeNewMove(tempRow,tempColumn, null));
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