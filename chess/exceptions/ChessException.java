package ChessProgram.chess.exceptions;

import ChessProgram.boardgame.exceptions.BoardException;

public class ChessException extends BoardException {

    public ChessException(String msg) {
        super(msg);
    }
}