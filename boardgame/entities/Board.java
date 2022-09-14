package ChessProgram.boardgame.entities;

import ChessProgram.boardgame.exceptions.BoardException;

public class Board {
    
    private Integer rows;
    private Integer columns;
    private Piece[][] pieces;


    public Board() {
    }

    public Board(Integer rows, Integer columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Error creanting board: There must be at least 1 row and 1 column");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public Integer getRows() {
        return rows;
    }
    public Integer getColumns() {
        return columns;
    }

    public Piece piece(int row, int column) {
        if (!positionExists(row, column)) {
            throw new BoardException("Position Error: The position " + new Position(row, column) + " does't exist on the board");
        }
        return pieces[row][column];
    }
    public Piece piece(Position pos) {
        return piece(pos.getRow(), pos.getColumn());
    }

    public void placePiece(Piece piece, Position pos) {
        if (thereIsAPiece(pos)) {
            throw new BoardException("Placement Piece Error: There is a piece on this position");
        }
        pieces[pos.getRow()][pos.getColumn()] = piece;
        piece.position = pos;
    }

    public Piece removePiece(Position pos) {
        if(!positionExists(pos)) {
            throw new BoardException("Position Error: The position " + pos + " does't exist on the board");
        }
        if (piece(pos) == null) {
            return null;
        }
        Piece auxPiece = piece(pos);
        auxPiece.position = null;
        pieces[pos.getRow()][pos.getColumn()] = null;
        return auxPiece;
    }

    public boolean positionExists(Position pos) {
        return positionExists(pos.getRow(), pos.getColumn());
    }
    private boolean positionExists(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    public boolean thereIsAPiece(Position pos) {
        if (!positionExists(pos)) {
            throw new BoardException("Position Error: The position " + pos + " does't exist on the board");
        }
        return piece(pos) != null;
    }
}
