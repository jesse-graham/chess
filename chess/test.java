package chess;

import java.util.Scanner;

public class test {
public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Board board = new Board();
        board.fillEmptyBoard();
        board.setSpace(Piece.coordinateToInteger("e1"), new King("wK", "e1"));
        board.updateWhiteKing("e1");
        board.setSpace(Piece.coordinateToInteger("e8"), new King("bK", "e8"));
        board.updateBlackKing("e8");
        //board.setSpace(Piece.coordinateToInteger("c6"), new Queen("bQ", "c6"));
        board.setSpace(Piece.coordinateToInteger("c8"), new Bishop("bB", "c8"));
        board.setSpace(Piece.coordinateToInteger("a1"), new Rook("wR", "a1"));
        board.setSpace(Piece.coordinateToInteger("h1"), new Rook("wR", "h1"));
        board.setSpace(Piece.coordinateToInteger("a8"), new Rook("bR", "a8"));
        board.setSpace(Piece.coordinateToInteger("h8"), new Rook("bR", "h8"));
        // board.setSpace(Piece.coordinateToInteger("e1"), new King("wK", "e1"));
        // board.updateWhiteKing("e1");
        // board.setSpace(Piece.coordinateToInteger("e8"), new King("bK", "e8"));
        // board.updateBlackKing("e8");
        // board.setSpace(Piece.coordinateToInteger("e4"), new Pawn("bp", "e4"));
        // board.setSpace(Piece.coordinateToInteger("d2"), new Pawn("wp", "d2"));
        // board.setSpace(Piece.coordinateToInteger("e2"), new Rook("wR", "e2"));
        board.printBoard();
        System.out.println();

        while (true) {
            System.out.print("Enter your move (type 'stop' to end): ");
            String input = scanner.nextLine();
            
            if (input.equalsIgnoreCase("stop")) {
                break; // Exit the loop if user types 'stop'
            }
            
            // Process the move (you can call your method here)
            if (input.length() == 5){
                // if just simple move with no extra modifiers
                String start = input.substring(0,2);
                String end = input.substring(3,5);

                board.getSpace(Piece.coordinateToInteger(start)).move(end, board);
                if (board.getSpace(Piece.coordinateToInteger(end)).toBePromoted == true){
                    Pawn.promote((board.getSpace(Piece.coordinateToInteger(end)).type.charAt(0)), end, 'Q', board);
                }
            } else if (input.length() == 7){
                // move that involves pawn promotion
                String start = input.substring(0,2);
                String end = input.substring(3,5);
                char desiredPromote = input.charAt(6);

                board.getSpace(Piece.coordinateToInteger(start)).move(end, board);
                if (board.getSpace(Piece.coordinateToInteger(end)).toBePromoted == true){
                    Pawn.promote((board.getSpace(Piece.coordinateToInteger(end)).type.charAt(0)), end, desiredPromote, board);
                }
            }

            System.out.println();
            board.printBoard();
            System.out.println();
        }
        
        scanner.close(); // Close the scanner when done
    }
        
}

