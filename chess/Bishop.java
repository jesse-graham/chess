package chess;

public class Bishop extends Piece {
    public Bishop(String type, String position){
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
                return "Checkmate, black wins!";
            }
        }

        if (board.kingInCheck() != 0) return "Check";

        return null;
    }

    public boolean moveValid(String destination, Board board){
        int [] start_array = Piece.coordinateToInteger(position);
        int [] destination_array = Piece.coordinateToInteger(destination);

        if (!inBounds(destination_array)) return false; // if destination is out of the bounds of the board, return false.

        if (friendlyPiece(destination, board)) return false;  // friedly piece is blocking the desired path, return false.

        if (!isDiagonal(start_array, destination_array)) return false; // check to see if move is a diagonal one, if so make valid true

        if (!clearPath(destination, board)) return false; // check to see if path is clear

        // check to see if this move puts your king into check. If it does print illegal move and return false.
        if (movePutsYouInCheck(destination, board)){
            System.out.println("move puts you in check"); 
            return false; 
        }

        return true;
    }

    public boolean clearPath(String destination, Board board){
        int[] start_array = Piece.coordinateToInteger(position);
        int[] destination_array = Piece.coordinateToInteger(destination);
        
        // Points A and B
        int rowA = start_array[0], colA = start_array[1];
        int rowB = destination_array[0], colB = destination_array[1];
        
        // Determine the direction of the diagonal
        int rowIncrement = (rowA < rowB) ? 1 : -1;
        int colIncrement = (colA < colB) ? 1 : -1;
        
        // Iterate over the diagonal
        int row = rowA + rowIncrement;
        int col = colA + colIncrement;
        while (row != rowB && col != colB) {
            int[] currentSpace = {row, col};
            
            if (board.getSpace(currentSpace).type != "ES") return false;

            row += rowIncrement;
            col += colIncrement;
        }
        return true;
    }
}
