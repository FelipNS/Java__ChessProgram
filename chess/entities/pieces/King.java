package ChessProgram.chess.entities.pieces;

import ChessProgram.boardgame.entities.Board;
import ChessProgram.boardgame.entities.Position;
import ChessProgram.chess.entities.ChessPiece;
import ChessProgram.chess.enums.Color;

public class King extends ChessPiece {

    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "K";
    }

    @Override
    public boolean[][] possibleMoves() {
        Board board = getBoard();
        boolean[][] matrix = new boolean[board.getRows()][board.getColumns()];

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                Position auxPos = new Position(position.getRow() + i, position.getColumn() + j);
                if (board.positionExists(auxPos) && (!board.thereIsAPiece(auxPos) || isThereAnOpponentPiece(auxPos))) {
                    matrix[auxPos.getRow()][auxPos.getColumn()] = true;
                }
            }
        }

        return matrix;
    }

}
