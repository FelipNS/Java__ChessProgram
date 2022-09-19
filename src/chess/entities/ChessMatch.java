package chess.entities;

import java.util.ArrayList;
import java.util.List;

import boardgame.entities.Board;
import boardgame.entities.Position;
import chess.entities.pieces.*;
import chess.enums.Color;
import chess.exceptions.ChessException;

public class ChessMatch {
    
    private Board board;
    private Color currentPlayer;
    private int turn;
    private boolean check;
    private boolean checkMate;

    private List<ChessPiece> piecesOnTheBoard = new ArrayList<>();
    private List<ChessPiece> capturedPieces = new ArrayList<>();
    
    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
    }
    
    public int getTurn() {
        return turn;
    }
    
    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isCheck() {
        return check;
    }

    public boolean isCheckMate() {
        return checkMate;
    }

    private void nextTurn() {
        turn++;
        currentPlayer = currentPlayer == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePos) {
        Position pos = sourcePos.toPosition();
        validateSourcePosition(pos);
        return board.piece(pos).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("The target position don't is a valid position to this piece");
        }
        ChessPiece capturedPiece = makeMove(source, target);
        
        if (isCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check!");
        }
        check = isCheck(opponent(currentPlayer)) ? true : false;
        if (isCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn();
        }

        return (ChessPiece) capturedPiece;
    }

    public void validateSourcePosition(Position pos) {
        ChessPiece piece = (ChessPiece) board.piece(pos); 
        if (!board.thereIsAPiece(pos)) {
            throw new ChessException("Selecting Piece Error: There is not a piece on " + ChessPosition.fromPosition(pos)  + " position");
        }
        if (!piece.isThereAnyPossibleMove()) {
            throw new ChessException("This piece don't have any possible moving");
        }
        if (piece.getColor() != currentPlayer) {
            throw new ChessException("This piece isn't yours. Please select a your piece!");
        }
    }

    private ChessPiece makeMove(Position source, Position target) {
        ChessPiece currentPiece = (ChessPiece) board.removePiece(source);
        ChessPiece capturedPiece = (ChessPiece) board.removePiece(target);

        //SPECIAL MOVE - CASTLING
        ChessSpecialMoves.makeCastling(this, currentPiece, target);

        //SPECIAL MOVE - EN PASSANT
        if (enPassantVulnerable != null) {
            capturedPiece = ChessSpecialMoves.makeEnPassant(this, target);
        }

        board.placePiece(currentPiece, target);
        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        currentPiece.increaseMoveCount();
        return capturedPiece;
    }

    private void undoMove(Position source, Position target, ChessPiece capturedPiece) {
        ChessPiece currentPiece = (ChessPiece) board.removePiece(target);
        currentPiece.decreaseMoveCount();
        board.placePiece(currentPiece, source);
        
        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        //SPECIAL MOVE - CASTLING
        ChessSpecialMoves.undoCastling(this, currentPiece, target);
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private Color opponent(Color color) {
        return color == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color) {
        List<ChessPiece> pieces = piecesOnTheBoard.stream()
                                                  .filter(p -> p.getColor() == color)
                                                  .toList();
        for (ChessPiece piece : pieces) {
            if (piece instanceof King) {
                return piece;
            }
        }
        throw new IllegalStateException("There is no " + color + "King on the board");
    }

    private boolean isCheck(Color color) {
        Position pos = king(color).getChessPosition().toPosition();
        List<ChessPiece> opponentPieces = piecesOnTheBoard.stream()
                                                          .filter(p -> p.getColor() == opponent(color))
                                                          .toList();
        for (ChessPiece piece : opponentPieces) {
            boolean[][] possibleMoves = piece.possibleMoves();
            if (possibleMoves[pos.getRow()][pos.getColumn()]) return true;
        }
        return false;
    }
    
    private boolean isCheckMate(Color color) {
        if (!isCheck(color)) return false;

        List<ChessPiece> opponentPieces = piecesOnTheBoard.stream()
                                                          .filter(p -> p.getColor() == opponent(color))
                                                          .toList();
        boolean[][] kingPossibleMoves = king(color).possibleMoves();
        for (ChessPiece piece : opponentPieces) {
            boolean[][] opponentPossibleMoves = piece.possibleMoves();
            for (int i = 0; i < kingPossibleMoves.length; i++) {
                for (int j = 0; j < kingPossibleMoves.length; j++) {
                    if (kingPossibleMoves[i][j]) {
                        kingPossibleMoves[i][j] = opponentPossibleMoves[i][j] ? false : true;
                    }
                }
            }
        }
        return !isThereAnyPossibleMove(kingPossibleMoves);
    }

    private boolean isThereAnyPossibleMove(boolean[][] possibleMoves) {
        for (int i = 0; i < possibleMoves.length; i++) {
            for (int j = 0; j < possibleMoves.length; j++) {
                if (possibleMoves[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

    private void initialSetup() {
        /*{
            placeNewPiece('a', 1, new Rook(board, Color.WHITE));
            placeNewPiece('b', 1, new Knight(board, Color.WHITE));
            placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
            placeNewPiece('e', 1, new King(board, Color.WHITE));
            placeNewPiece('d', 1, new Queen(board, Color.WHITE));
            placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
            placeNewPiece('g', 1, new Knight(board, Color.WHITE));
            placeNewPiece('h', 1, new Rook(board, Color.WHITE));
            
            placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
            placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
            placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
            placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
            placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
            placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
            placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
            placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));
            
            
            placeNewPiece('a', 8, new Rook(board, Color.BLACK));
            placeNewPiece('b', 8, new Knight(board, Color.BLACK));
            placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
            placeNewPiece('e', 8, new King(board, Color.BLACK));
            placeNewPiece('d', 8, new Queen(board, Color.BLACK));
            placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
            placeNewPiece('g', 8, new Knight(board, Color.BLACK));
            placeNewPiece('h', 8, new Rook(board, Color.BLACK));

            placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
            placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
            placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
            placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
            placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
            placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
            placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
            placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
        }*/
    
        placeNewPiece('a', 1, new King(board, Color.WHITE));
        placeNewPiece('a', 5, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 5, new Pawn(board, Color.WHITE, this));

        placeNewPiece('a', 8, new King(board, Color.BLACK));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 4, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
    }    
}
