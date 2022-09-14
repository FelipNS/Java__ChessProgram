package ChessProgram.chess.entities.pieces;

import ChessProgram.boardgame.entities.Board;
import ChessProgram.boardgame.entities.Position;
import ChessProgram.chess.entities.ChessPiece;
import ChessProgram.chess.enums.Color;

public class Pawn  extends ChessPiece {

    public Pawn(Board board, Color color) {
        super(board, color);
    }
    
    @Override
    public String toString() {
        return "P";
    }

    
    @Override
    public boolean[][] possibleMoves() {
        Board board = getBoard();
        boolean[][] matrix = new boolean[board.getRows()][board.getColumns()];
        int rowDirection = getColor() == Color.WHITE ? -1 : 1;

        Position auxPos = new Position(position.getRow() + rowDirection, position.getColumn());
        if (board.positionExists(auxPos) && !isThereAnOpponentPiece(auxPos)) {
            matrix[auxPos.getRow()][auxPos.getColumn()] = true;
        }

        for (int i : new int[] {-1, 1}) {
            auxPos.setValues(auxPos.getRow(), position.getColumn() + i);
            if (board.positionExists(auxPos) && isThereAnOpponentPiece(auxPos)) {
                matrix[auxPos.getRow()][auxPos.getColumn()] = true;
            }
        }

        if (getMoveCount() == 0) {
            auxPos.setValues(auxPos.getRow() + rowDirection, position.getColumn());
            if (board.positionExists(auxPos) && !isThereAnOpponentPiece(auxPos)) {
                matrix[auxPos.getRow()][auxPos.getColumn()] = true;
            }
        }
            
        return matrix;
    }


}
