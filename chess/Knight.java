package chess;

public class Knight extends Piece {
    public Knight(String type, String position){
        this.type = type;
        this.position = position;
        moved = false;
    }

    public String move(String destination, Board board){
        if (board.isGameOver()) return "ILLEGAL_MOVE";

        if (type.charAt(0) == 'b'){
            if (board.getTotalMoveCount() % 2 == 0) {
                System.out.println("ILLEGAL_MOVE, not your turn");
                return "ILLEGAL_MOVE";
            } 
        } else {
            if (board.getTotalMoveCount() % 2 != 0){
                System.out.println("ILLEGAL_MOVE, not your turn");
                return "ILLEGAL_MOVE";
            } 
        }

        if (!moveValid(destination, board)){
            System.out.println("ILLEGAL_MOVE"); 
            return "ILLEGAL_MOVE"; 
            // check to see if move is valid. if not, print illegal move, print board, and return.
        }

        if (!position.equals(destination)){
            takePiece(position, destination, board);
            board.incrementTotalMoveCount();
        } else {
            System.out.println("ILLEGAL_MOVE, move doesn't take you anywhere");
            return "ILLEGAL_MOVE";
        }
        
        if (type.charAt(0) == 'w'){
            if (board.blackCheckMate()){
                System.out.println("Checkmate, white wins!");
                board.endGame();
                return "Checkmate, white wins!";
            }
        } else {
            if (board.whiteCheckMate()){
                System.out.println("Checkmate, black wins!");
                board.endGame();
                return "Checkmate, white wins!";
            }
        }

        if (board.kingInCheck() != 0) return "Check";
        
        return null;
    }

    public boolean moveValid(String destination, Board board){
        int[] start_array = Piece.coordinateToInteger(position);
        int[] destination_array = Piece.coordinateToInteger(destination);

        if (!Piece.inBounds(destination_array)) return false; // if move is not in bounds, return false.

        if (friendlyPiece(destination, board)) return false;  // friedly piece is blocking the desired path, return false.

        int[][] possible_moves = {
            {start_array[0]-1, start_array[1]-2},   // create an array of all possible moves.
            {start_array[0]-2, start_array[1]-1},
            {start_array[0]-2, start_array[1]+1},
            {start_array[0]-1, start_array[1]+2},
            {start_array[0]+1, start_array[1]+2},
            {start_array[0]+2, start_array[1]+1},
            {start_array[0]+2, start_array[1]-1},
            {start_array[0]+1, start_array[1]-2},
        };
        if (!possibleMove(possible_moves, destination_array)) return false; // see if move is possible.

        // check to see if this move puts your king into check. If it does print illegal move and return false.
        if (movePutsYouInCheck(destination, board)){
            System.out.println("move puts you in check"); 
            return false; 
        }

        return true;
    }
}
