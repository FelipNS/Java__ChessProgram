package chess.entities.pieces;

import boardgame.entities.Board;
import boardgame.entities.Position;
import chess.entities.ChessPiece;
import chess.enums.Color;

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

        if (getMoveCount() == 0) matrix = castling(matrix);
        return matrix;
    }

    private boolean[][] castling(boolean[][] matrix) {
        Board board = getBoard();
        ChessPiece castlingShort = (ChessPiece) board.piece(position.getRow(), 7);
        ChessPiece castlingLong = (ChessPiece) board.piece(position.getRow(), 0);
        
        if (castlingShort instanceof Rook && castlingShort.getMoveCount() == 0) {
            if (isFreeWay(position.getColumn(), castlingShort.getChessPosition().toPosition().getColumn())) {
                matrix[position.getRow()][6] = true;
            };
        }
        if (castlingLong instanceof Rook && castlingLong.getMoveCount() == 0) {
            if (isFreeWay(position.getColumn(), castlingLong.getChessPosition().toPosition().getColumn())) {
                matrix[position.getRow()][2] = true;
            };
        }

        return matrix;
    }

    private boolean isFreeWay(int kingColumn, int rookColumn) {
        int first, last;

        if (kingColumn > rookColumn) {
            first =  rookColumn;
            last =  kingColumn;            
        } else {
            first =  kingColumn;
            last =  rookColumn;
        }
        for (int i = first + 1; i < last; i++) {
            if (getBoard().thereIsAPiece(new Position(position.getRow(), i))) return false;
        }
        return true;
    }
}
