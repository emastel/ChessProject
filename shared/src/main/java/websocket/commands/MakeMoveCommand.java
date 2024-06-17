package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {

    ChessMove move;

    public MakeMoveCommand(String auth, int id, ChessMove move) {
        super(CommandType.MAKE_MOVE,auth,id);
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }
}
