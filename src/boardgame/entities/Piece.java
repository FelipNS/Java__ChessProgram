package boardgame.entities;

public abstract class Piece {
    
    protected Position position;
    private Board board;

    public Piece() {
    }

    public Piece(Board board) {
        this.board = board;
    }

    protected Board getBoard() {
        return board;
    }

    public abstract boolean[][] possibleMoves();
    
    public Boolean possibleMove(Position pos) {
        return  possibleMoves()[pos.getRow()][pos.getColumn()];
    }
    
    public boolean isThereAnyPossibleMove() {
        boolean[][] matrix = possibleMoves();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }
}
