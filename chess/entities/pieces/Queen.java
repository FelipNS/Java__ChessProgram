package ChessProgram.chess.entities.pieces;

import ChessProgram.boardgame.entities.Board;
import ChessProgram.boardgame.entities.Position;
import ChessProgram.chess.entities.ChessPiece;
import ChessProgram.chess.enums.Color;

public class Queen extends ChessPiece {

    public Queen(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "Q";
    }

    @Override
    public boolean[][] possibleMoves() {
        Board board = getBoard();
        boolean[][] matrix = new boolean[board.getRows()][board.getColumns()];
        Position auxPos = new Position();

        for (int rDirection : new int[] {-1, 1}) {
            for (int cDirection : new int[] {-1, 1}) {
                auxPos.setValues(position.getRow() + rDirection, position.getColumn() + cDirection);
                while (board.positionExists(auxPos)) {
                    if (board.thereIsAPiece(auxPos)) {
                        if (isThereAnOpponentPiece(auxPos)) {
                            matrix[auxPos.getRow()][auxPos.getColumn()] = true;
                            break;
                        }
                        break;
                    }
                    matrix[auxPos.getRow()][auxPos.getColumn()] = true;
                    auxPos.setValues(auxPos.getRow() + rDirection, auxPos.getColumn() + cDirection);
                }
            }
        }

        for (int direction : new int[] {-1, 1}) {
            // Running through the columns
            auxPos.setValues(position.getRow(), position.getColumn() + direction);
            while (board.positionExists(auxPos) && !board.thereIsAPiece(auxPos)) {                
                matrix[auxPos.getRow()][auxPos.getColumn()] = true;
                auxPos.setValues(position.getRow(), auxPos.getColumn() + direction);
            }
            if (board.positionExists(auxPos) && isThereAnOpponentPiece(auxPos)) {
                matrix[auxPos.getRow()][auxPos.getColumn()] = true;
            }
            
            // Running through the rows
            auxPos.setValues(position.getRow() + direction, position.getColumn());
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
