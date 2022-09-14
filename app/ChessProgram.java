package ChessProgram.app;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import ChessProgram.chess.entities.ChessMatch;
import ChessProgram.chess.entities.ChessPiece;
import ChessProgram.chess.entities.ChessPosition;
import ChessProgram.chess.exceptions.ChessException;

public class ChessProgram {
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        ChessMatch match = new ChessMatch();
        List<ChessPiece> capturedPieces = new ArrayList<>();

        while (!match.isCheckMate()) {
            try {
                ChessPiece[][] pieces = match.getPieces();
                UI.clearScreen();
                UI.printMatch(match, capturedPieces);
                System.out.println();
            
                System.out.print("Source: ");
                ChessPosition source = UI.readChessPosition(sc);
                
                boolean[][] possibleMoves = match.possibleMoves(source);
                UI.clearScreen();
                UI.printBoard(pieces, possibleMoves);

                System.out.print("Target: ");
                ChessPosition target = UI.readChessPosition(sc);
                
                ChessPiece capturedPiece = match.performChessMove(source, target);
                if (capturedPiece != null) { capturedPieces.add(capturedPiece); }
                System.out.println();
            } catch (ChessException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
        UI.clearScreen();
        UI.printMatch(match, capturedPieces);
    }
}
