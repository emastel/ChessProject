package chess;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return thisRow == that.thisRow && thisColumn == that.thisColumn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(thisRow, thisColumn);
    }

    @Override
    public String toString() {
        return "ChessPosition{" +
                "thisRow=" + thisRow +
                ", thisColumn=" + thisColumn +
                '}';
    }
}
