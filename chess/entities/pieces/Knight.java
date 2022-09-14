package ChessProgram.chess.entities.pieces;

import ChessProgram.boardgame.entities.Board;
import ChessProgram.boardgame.entities.Position;
import ChessProgram.chess.entities.ChessPiece;
import ChessProgram.chess.enums.Color;

public class Knight extends ChessPiece {

    public Knight(Board board, Color color) {
        super(board, color);
    }
    
    @Override
    public String toString() {
        return "N";
    }

    @Override
    public boolean[][] possibleMoves() {
        Board board = getBoard();
        boolean[][] matrix = new boolean[board.getRows()][board.getColumns()];
        Position auxPos = new Position();
        
        for (int rDirection : new int[] {-2, 2}) {
            for (int cDirection : new int[] {-1, 1}) {
                auxPos.setValues(position.getRow() + rDirection, position.getColumn() + cDirection);
                if (board.positionExists(auxPos) && (!board.thereIsAPiece(auxPos) || isThereAnOpponentPiece(auxPos))) {
                    matrix[auxPos.getRow()][auxPos.getColumn()] = true;
                }
                
                auxPos.setValues(position.getRow() + cDirection, position.getColumn() + rDirection);
                if (board.positionExists(auxPos) && (!board.thereIsAPiece(auxPos) || isThereAnOpponentPiece(auxPos))) {
                    matrix[auxPos.getRow()][auxPos.getColumn()] = true;
                }
            }
        }

        return matrix;
    }
}
