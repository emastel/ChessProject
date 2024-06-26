package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class ChessBoard implements Cloneable {

    private ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {}

    public Object clone() throws CloneNotSupportedException {
        ChessBoard clone = new ChessBoard();
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                ChessPiece tempPiece = squares[i][j];
                if(tempPiece != null) {
                    ChessPosition tempPosition = new ChessPosition(i+1, j+1);
                    clone.addPiece(tempPosition,tempPiece);
                }
            }
        }
        return clone;
    }

    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    public void removePiece(ChessPosition position) {
        squares[position.getRow()-1][position.getColumn()-1] = null;
    }

    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    public Collection<ChessPosition> getTeamPieces(ChessGame.TeamColor teamColor) {
        ArrayList<ChessPosition> teamPieces = new ArrayList<>();
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                ChessPiece tempPiece = squares[i][j];
                if(tempPiece != null && tempPiece.getTeamColor() == teamColor) {
                    ChessPosition tempPosition = new ChessPosition(i+1, j+1);
                    teamPieces.add(tempPosition);
                }
            }
        }
        return teamPieces;
    }

    public void resetBoard() {
        /* White Rook */
        ChessPiece newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPosition newPosition = new ChessPosition(1,1);
        addPiece(newPosition,newPiece);
        newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        newPosition = new ChessPosition(1,8);
        addPiece(newPosition,newPiece);
        /* White Knight */
        newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        newPosition = new ChessPosition(1,2);
        addPiece(newPosition,newPiece);
        newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        newPosition = new ChessPosition(1,7);
        addPiece(newPosition,newPiece);
        /* White Bishop */
        newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        newPosition = new ChessPosition(1,3);
        addPiece(newPosition,newPiece);
        newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        newPosition = new ChessPosition(1,6);
        addPiece(newPosition,newPiece);
        /* White Queen */
        newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        newPosition = new ChessPosition(1,4);
        addPiece(newPosition,newPiece);
        /* White King */
        newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        newPosition = new ChessPosition(1,5);
        addPiece(newPosition,newPiece);
        /* White Pawns */
        for (int i = 0; i < 8; i++) {
            newPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            newPosition = new ChessPosition(2,i+1);
            addPiece(newPosition,newPiece);
        }
        /* Black Pawns */
        for (int i = 0; i < 8; i++) {
            newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            newPosition = new ChessPosition(7,i+1);
            addPiece(newPosition,newPiece);
        }
        /* Black Rook */
        newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        newPosition = new ChessPosition(8,1);
        addPiece(newPosition,newPiece);
        newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        newPosition = new ChessPosition(8,8);
        addPiece(newPosition,newPiece);
        /* Black Knight */
        newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        newPosition = new ChessPosition(8,2);
        addPiece(newPosition,newPiece);
        newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        newPosition = new ChessPosition(8,7);
        addPiece(newPosition,newPiece);
        /* Black Bishop */
        newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        newPosition = new ChessPosition(8,3);
        addPiece(newPosition,newPiece);
        newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        newPosition = new ChessPosition(8,6);
        addPiece(newPosition,newPiece);
        /* Black Queen */
        newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        newPosition = new ChessPosition(8,4);
        addPiece(newPosition,newPiece);
        /* Black King */
        newPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        newPosition = new ChessPosition(8,5);
        addPiece(newPosition,newPiece);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.deepToString(squares) +
                '}';
    }
}
