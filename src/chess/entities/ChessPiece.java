package chess.entities;

import boardgame.entities.Board;
import boardgame.entities.Piece;
import boardgame.entities.Position;
import chess.enums.Color;

public abstract class ChessPiece extends Piece {

    private Color color;
    private int moveCount;

    public ChessPiece() {
        super();
    }

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }
    
    public Color getColor() {
        return color;
    }
    public Integer getMoveCount() {
        return moveCount;
    }
    public ChessPosition getChessPosition() {
        return ChessPosition.fromPosition(position);
    }
    
    public void increaseMoveCount() {
        moveCount++;
    }
    public void decreaseMoveCount() {
        moveCount--;
    }

    protected boolean isThereAnOpponentPiece(Position pos) {
        ChessPiece piece = (ChessPiece) getBoard().piece(pos);
        return piece != null && piece.getColor() != color;
    }
}
