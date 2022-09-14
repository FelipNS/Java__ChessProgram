package ChessProgram.chess.entities.pieces;

import ChessProgram.boardgame.entities.Board;
import ChessProgram.boardgame.entities.Position;
import ChessProgram.chess.entities.ChessPiece;
import ChessProgram.chess.enums.Color;

public class Rook extends ChessPiece {

    public Rook(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "R";
    }

    @Override
    public boolean[][] possibleMoves() {
        Board board = getBoard();
        boolean[][] matrix = new boolean[board.getRows()][board.getColumns()];
        Position auxPos = new Position(position.getRow(), position.getColumn());

        for (int direction : new int[] {-1, 1}) {
            // Running through the columns
            auxPos.setValues(position.getRow(), auxPos.getColumn() + direction);
            while (board.positionExists(auxPos) && !board.thereIsAPiece(auxPos)) {                
                matrix[auxPos.getRow()][auxPos.getColumn()] = true;
                auxPos.setValues(position.getRow(), auxPos.getColumn() + direction);
            }
            if (board.positionExists(auxPos) && isThereAnOpponentPiece(auxPos)) {
                matrix[auxPos.getRow()][auxPos.getColumn()] = true;
            }
            
            // Running through the rows
            auxPos.setValues(auxPos.getRow() + direction, position.getColumn());
            while (board.positionExists(auxPos) && !board.thereIsAPiece(auxPos)) {
                matrix[auxPos.getRow()][auxPos.getColumn()] = true;
                auxPos.setValues(auxPos.getRow() + direction, position.getColumn());
            }
            if (board.positionExists(auxPos) && isThereAnOpponentPiece(auxPos)) {
                matrix[auxPos.getRow()][auxPos.getColumn()] = true;
            }
        }     
        return matrix;
    }
}
