package chess.entities;

import java.security.InvalidParameterException;

import boardgame.entities.Position;
import chess.entities.pieces.Bishop;
import chess.entities.pieces.King;
import chess.entities.pieces.Knight;
import chess.entities.pieces.Pawn;
import chess.entities.pieces.Queen;
import chess.entities.pieces.Rook;
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
        if (match.getEnPassantVulnerable().equals(piece)) {
            return (ChessPiece) match.board.removePiece(piece.getChessPosition().toPosition());
        }
        return null;
    }

    public static void undoEnPassant(ChessMatch match, Position source, ChessPiece capturedPawn, Position target) {
        Position initialPos = capturedPawn.getChessPosition().toPosition(); 
        if (capturedPawn instanceof Pawn) {
            match.board.placePiece(capturedPawn, initialPos);
            if (match.board.piece(target) instanceof Pawn) {
                match.board.placePiece(match.board.piece(target), source);
            }
        }
    }

    public static void pawnToPromotion(ChessMatch match, Position target) {
        ChessPiece movedPiece = (ChessPiece) match.board.piece(target);

        match.promoted = null;
        if (movedPiece instanceof Pawn) {
            if ((movedPiece.getChessPosition().getRow() == 1) || (movedPiece.getChessPosition().getRow() == 8)) {
                match.promoted = movedPiece;
                match.board.removePiece(movedPiece.getChessPosition().toPosition());
                match.piecesOnTheBoard.remove(movedPiece);
            }
        }
    }

    public static void replacePromotedPiece(ChessMatch match, String type, ChessPosition target) {
        
        ChessPiece pawn = match.getPromoted();
        ChessPiece newPiece = null;
        match.board.removePiece(pawn.getChessPosition().toPosition());
        switch (type.toUpperCase()) {
            case "Q":
                newPiece = new Queen(match.board, pawn.getColor());
                break;
            case "N":
                newPiece = new Knight(match.board, pawn.getColor());
                break;
            case "R":
                newPiece = new Rook(match.board, pawn.getColor());
                break;
            case "B":
                newPiece = new Bishop(match.board, pawn.getColor());
                break;
            default:
                throw new InvalidParameterException("This isn't valid piece");
        }
        match.board.placePiece(newPiece, target.toPosition());
        match.piecesOnTheBoard.add(newPiece);
    }
}
