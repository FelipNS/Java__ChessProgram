package chess.entities;

import boardgame.entities.Position;
import chess.entities.pieces.King;
import chess.entities.pieces.Pawn;
import chess.enums.Color;

public class ChessSpecialMoves {

    public static void makeCastling(ChessMatch match, ChessPiece piece, Position target) {
        if (piece instanceof King && piece.getMoveCount() == 0 && !match.isCheck()){
            if (target.equals(new Position(target.getRow(), 2))) {
                ChessPiece rook = (ChessPiece) match.board.piece(target.getRow(), 0);
                match.board.removePiece(rook.getChessPosition().toPosition());
                match.board.placePiece(rook, new Position(target.getRow(), 3));
                rook.increaseMoveCount();
            }
            if (target.equals(new Position(target.getRow(), 6))) {
                ChessPiece rook = (ChessPiece) match.board.piece(target.getRow(), 7);
                match.board.removePiece(rook.getChessPosition().toPosition());
                match.board.placePiece(rook, new Position(target.getRow(), 5));
                rook.increaseMoveCount();
            }
        }
    }

    public static void undoCastling(ChessMatch match, ChessPiece piece, Position target) {
        if (piece instanceof King && piece.getMoveCount() == 0){
            if (target.equals(new Position(target.getRow(), 2))) {
                ChessPiece rook = (ChessPiece) match.board.piece(target.getRow(), 3);
                match.board.removePiece(rook.getChessPosition().toPosition());
                match.board.placePiece(rook, new Position(target.getRow(), 0));
                rook.decreaseMoveCount();
            }
            if (target.equals(new Position(target.getRow(), 6))) {
                ChessPiece rook = (ChessPiece) match.board.piece(target.getRow(), 5);
                match.board.removePiece(rook.getChessPosition().toPosition());
                match.board.placePiece(rook, new Position(target.getRow(), 7));
                rook.decreaseMoveCount();
            }
        }
    }

    public static ChessPiece getEnPassantVulnerablePiece(ChessMatch match, Position target) {
        ChessPiece piece = (ChessPiece) match.board.piece(target);

        if (piece instanceof Pawn) {
            int enPassantRow = piece.getColor() == Color.WHITE ? 4 : 3;
            Position pawnPosition = piece.getChessPosition().toPosition();
            for (int side : new int[] {-1, 1}) { // -1->LEFT && 1->RIGHT          
                Position asideColumn = new Position(pawnPosition.getRow(), pawnPosition.getColumn() + side);
                if (pawnPosition.getRow() == enPassantRow) {
                    if (match.board.positionExists(asideColumn) && match.board.piece(asideColumn) instanceof Pawn) {
                        return piece;
                    }
            }
            }
        }
        return null;
    }

    public static ChessPiece makeEnPassant(ChessMatch match, Position target) {
        int rowToCapture = target.getRow() == 5 ? 4 : 3;
        ChessPiece piece = (ChessPiece) match.board.piece(rowToCapture, target.getColumn());
        if (match.getEnPassantVulnerable().equals(piece)) return (ChessPiece) match.board.removePiece(piece.getChessPosition().toPosition());
        ;

        return null;
    }
}
