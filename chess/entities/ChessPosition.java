package ChessProgram.chess.entities;

import ChessProgram.boardgame.entities.Position;
import ChessProgram.chess.exceptions.ChessException;

public class ChessPosition {
    
    private final Integer LENGTH_ROW_AND_COLUMN = 8;
    private Character column;
    private Integer row;

    
    public ChessPosition() {
    }

    public ChessPosition(Character column, Integer row) {
        if (column < 'a' || column > 'h' || row < 1 || row > 8) {
            throw new ChessException("Error: Invalid value. Valid values are from a1 to h8");
        }
        this.column = column;
        this.row = row;
    }
    
    public Character getColumn() {
        return column;
    }
    public Integer getRow() {
        return row;
    }

    @Override
    public String toString() {
        return column + String.valueOf(row);
    }

    public Position toPosition() {
        return new Position(LENGTH_ROW_AND_COLUMN - row, column - 'a');
    }

    public static ChessPosition fromPosition(Position pos) {
        return new ChessPosition((char) ('a' + pos.getColumn()), 8 - pos.getRow());
    }

    
}
