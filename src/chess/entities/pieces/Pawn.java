package chess.entities.pieces;

import boardgame.entities.Board;
import boardgame.entities.Position;
import chess.entities.ChessMatch;
import chess.entities.ChessPiece;
import chess.enums.Color;

public class Pawn  extends ChessPiece {

    private ChessMatch match;

    public Pawn(Board board, Color color, ChessMatch match) {
        super(board, color);
        this.match = match;
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
        
        matrix = enPassant(matrix);

        return matrix;
    }

    private boolean[][] enPassant(boolean[][] matrix) {
        Board board = getBoard();
        int enPassantRow = getColor() == Color.WHITE ? 3 : 4;
        Position pawnPosition = getChessPosition().toPosition();
        for (int side : new int[] {-1, 1}) { // -1->LEFT && 1->RIGHT          
            Position asidePosition = new Position(pawnPosition.getRow(), pawnPosition.getColumn() + side);
            if (pawnPosition.getRow() == enPassantRow) {
                if(board.positionExists(asidePosition) && board.piece(asidePosition) instanceof Pawn &&
                   (ChessPiece) board.piece(asidePosition) == match.getEnPassantVulnerable()) {
                    int rowToCapture = asidePosition.getRow() == 3 ? 2 : 5;
                    matrix[rowToCapture][asidePosition.getColumn()] = true;
                }
            }
        }

        return matrix;
    }


}
