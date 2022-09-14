package ChessProgram.app;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import ChessProgram.chess.entities.ChessMatch;
import ChessProgram.chess.entities.ChessPiece;
import ChessProgram.chess.entities.ChessPosition;
import ChessProgram.chess.enums.Color;

public class UI {
    
    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static ChessPosition readChessPosition(Scanner sc) {
        try {
            String pos = sc.nextLine().toLowerCase();
            char column = pos.charAt(0);
            int row =Integer.parseInt(String.valueOf(pos.charAt(1)));
            return new ChessPosition(column, row);
        } catch (RuntimeException e) {
            throw new InputMismatchException("Error reading input: Valid position values from a1 to h8");
        }        
    }

    public static void printMatch(ChessMatch match, List<ChessPiece> capturedPieces) {
        String textColor = match.getCurrentPlayer() == Color.WHITE ? ANSI_WHITE : ANSI_YELLOW;
        System.out.println(textColor + "=================");
        if (!match.isCheckMate()){
            System.out.println("     " + "TURN: " + match.getTurn());
            System.out.println("   " + match.getCurrentPlayer() + "'s TURN");
            if (match.isCheck()) System.out.println("     CHECK!");
        } else {
            System.out.println("   CHECK MATE!");
            System.out.println("    " + match.getCurrentPlayer() + " WIN");
        }
        System.out.println("=================" + ANSI_RESET);
        printBoard(match.getPieces());
        printCapturedPieces(capturedPieces);
    }

    public static void printBoard(ChessPiece[][] pieces) {
        for (int i = 0; i < pieces.length; i++) {
            System.out.print(8 - i + " ");
            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j], false);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
        for (int i = 0; i < pieces.length; i++) {
            System.out.print(8 - i + " ");
            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j], possibleMoves[i][j]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }
    
    public static void printPiece(ChessPiece piece, boolean isPossibleMove) {
        if (isPossibleMove) System.out.print(ANSI_RED_BACKGROUND);
        if (piece == null) {
            System.out.print("-" + ANSI_RESET);
        } else {
            if (piece.getColor() == Color.WHITE) {
                System.out.print(ANSI_WHITE + piece + ANSI_RESET);
            }
            else {
                System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
            }
        }
        System.out.print(" ");
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void printCapturedPieces(List<ChessPiece> capturedPieces) {
        List<ChessPiece> whites = capturedPieces.stream()
                                                .filter(p -> p.getColor() == Color.WHITE)
                                                .toList();
        List<ChessPiece> blacks = capturedPieces.stream()
                                                .filter(p -> p.getColor() == Color.BLACK)
                                                .toList();

        System.out.println("CAPTURED PIECES:");                                        
        System.out.println(ANSI_WHITE + "White: " + Arrays.toString(whites.toArray()) + ANSI_RESET);                                        
        System.out.println(ANSI_YELLOW + "Black: " + Arrays.toString(blacks.toArray()) + ANSI_RESET);                                        
    }

}
