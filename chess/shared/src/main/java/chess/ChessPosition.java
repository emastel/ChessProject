package chess;

public class ChessPosition {
    private int thisRow;
    private int thisColumn;

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
