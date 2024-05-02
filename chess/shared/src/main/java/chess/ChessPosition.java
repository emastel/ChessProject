package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private:
    int thisRow;
    int thisColumn;

    public ChessPosition(int row, int col) {
        thisRow = row;
        thisColumn = col;
    }
    public int getRow() {
        return thisRow;
    }

    public int getColumn() {
        return thisColumn;
    }
}
