package chess;

public class Rook extends Piece {
    public Rook(String type, String position){
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
            moved = true;
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

        if (start_array[1] != destination_array[1] && destination_array[0] != start_array[0]) return false; // if move is not in straight line, return false;

        if (!clearPath(destination, board)) return false; // if path is not clear return false.

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
        
        if (start_array[0] == destination_array[0] && start_array[1] == destination_array[1]) return false;

        if (start_array[0] == destination_array[0]){
            if(start_array[1] > destination_array[1]){
                for (int i = start_array[1]-1; i > destination_array[1]; i--){
                    int[] test = {start_array[0],i};
                    if (board.getSpace(test).type != "ES") return false;
                }
            } else{
                for (int i = start_array[1]+1; i < destination_array[1]; i++){
                    int[] test = {start_array[0],i};
                    if (board.getSpace(test).type != "ES") return false;
                }
            }
        } else {
            if(start_array[0] > destination_array[0]){
                for (int i = start_array[0]-1; i > destination_array[0]; i--){
                    int[] test = {i,start_array[1]};
                    if (board.getSpace(test).type != "ES") return false;
                }
            } else {
                for (int i = start_array[0]+1; i < destination_array[0]; i++){
                    int[] test = {i,start_array[1]};
                    if (board.getSpace(test).type != "ES") return false;
                }
            }
        }
        return true;
    }
}