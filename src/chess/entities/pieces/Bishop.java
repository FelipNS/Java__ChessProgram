package chess.entities.pieces;

import boardgame.entities.Board;
import boardgame.entities.Position;
import chess.entities.ChessPiece;
import chess.enums.Color;

public class Bishop extends ChessPiece {

    public Bishop(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "B";
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
        
        return matrix;
    }    
}
