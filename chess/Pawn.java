package chess;

public class Pawn extends Piece {

    public Pawn(String type, String position){
        this.type = type;
        this.position = position;
        moved = false;
        toBePromoted = false;
        passant = false;
        timesmoved = 0;
        
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

        // en passant handling
        int[] destination_array = Piece.coordinateToInteger(destination);
        int[] start_array = Piece.coordinateToInteger(position);
        int[] left = {start_array[0], start_array[1]-1};
        int[] right = {start_array[0], start_array[1]+1};
        boolean passantmovemade = false;

        if (type == "bp"){
            if (Piece.inBounds(left) && board.getSpace(left).type == "wp" && board.getSpace(left).passant == true && board.getSpace(left).timesmoved == 1){
                if (destination_array[0] == start_array[0]+1 && destination_array[1] == start_array[1]-1 && board.getSpace(destination_array).type == "ES" && board.getSpace(left).whenPassantMoved == board.getTotalMoveCount()-1){
                    Piece piece = board.getSpace(left);
                    takePiece(position, Piece.coordinateToString(left), board);
                    takePiece(Piece.coordinateToString(left), destination, board);
                    if (board.kingInCheck() == 1){
                        takePiece(destination, Piece.coordinateToString(start_array), board);
                        board.setSpace(left, piece);
                        System.out.println("ILLEGAL_MOVE, move puts you in check");
                        return "ILLEGAL_MOVE";
                    } else {
                        timesmoved++;
                        passantmovemade = true;
                        board.incrementTotalMoveCount();                        
                        moved = true;
                    } 
                }
            }
            if (Piece.inBounds(right) && board.getSpace(right).type == "wp" && board.getSpace(right).passant == true && board.getSpace(right).timesmoved == 1){
                if (destination_array[0] == start_array[0]+1 && destination_array[1] == start_array[1]+1 && board.getSpace(destination_array).type == "ES" && board.getSpace(right).whenPassantMoved == board.getTotalMoveCount()-1){
                    Piece piece = board.getSpace(right);
                    takePiece(position, Piece.coordinateToString(right), board);
                    takePiece(Piece.coordinateToString(right), destination, board);
                    if (board.kingInCheck() == 1){
                        takePiece(destination, Piece.coordinateToString(start_array), board);
                        board.setSpace(right, piece);
                        System.out.println("ILLEGAL_MOVE, move puts you in check");
                        return "ILLEGAL_MOVE";
                    } else {
                        timesmoved++;
                        passantmovemade = true;
                        board.incrementTotalMoveCount();                        
                        moved = true;
                    } 
                }
            }
        } else {
            if (Piece.inBounds(left) && board.getSpace(left).type == "bp" && board.getSpace(left).passant == true && board.getSpace(left).timesmoved == 1){
                if (destination_array[0] == start_array[0]-1 && destination_array[1] == start_array[1]-1 && board.getSpace(destination_array).type == "ES" && board.getSpace(left).whenPassantMoved == board.getTotalMoveCount()-1){
                    Piece piece = board.getSpace(left);
                    takePiece(position, Piece.coordinateToString(left), board);
                    takePiece(Piece.coordinateToString(left), destination, board);
                    if (board.kingInCheck() == -1){
                        takePiece(destination, Piece.coordinateToString(start_array), board);
                        board.setSpace(left, piece);
                        System.out.println("ILLEGAL_MOVE, move puts you in check");
                        return "ILLEGAL_MOVE";
                    } else {
                        timesmoved++;
                        passantmovemade = true;
                        board.incrementTotalMoveCount();                        
                        moved = true;
                    } 
                }
            }
            if (Piece.inBounds(right) && board.getSpace(right).type == "bp" && board.getSpace(right).passant == true && board.getSpace(right).timesmoved == 1){
                if (destination_array[0] == start_array[0]-1 && destination_array[1] == start_array[1]+1 && board.getSpace(destination_array).type == "ES" && board.getSpace(right).whenPassantMoved == board.getTotalMoveCount()-1){
                    Piece piece = board.getSpace(right);
                    takePiece(position, Piece.coordinateToString(right), board);
                    takePiece(Piece.coordinateToString(right), destination, board);
                    if (board.kingInCheck() == -1){
                        takePiece(destination, Piece.coordinateToString(start_array), board);
                        board.setSpace(right, piece);
                        System.out.println("ILLEGAL_MOVE, move puts you in check");
                        return "ILLEGAL_MOVE";
                    } else {
                        timesmoved++;
                        passantmovemade = true;
                        board.incrementTotalMoveCount();
                        moved = true;
                    } 
                }
            }
        }

        if (passantmovemade == false && !moveValid(destination, board)){
            System.out.println("ILLEGAL_MOVE, move not valid"); 
            return "ILLEGAL_MOVE"; 
            // check to see if move is valid. if not, print illegal move and return.
        } 

        if (passantmovemade == false){
            if (position.equals(destination)){
                System.out.println("ILLEGAL_MOVE, move doesn't take you anywhere");
                return "ILLEGAL_MOVE"; 
            } else {
                takePiece(position, destination, board);
                board.incrementTotalMoveCount();                
                moved = true;
                timesmoved++;
            }
        }

        char test = destination.charAt(1);
        if (test == '8' || test == '1'){
            toBePromoted = true; // if pawn reaches opposite side of board, promote it.
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

       if (Piece.inBounds(destination_array) == false) return false; // if move destination is out of bounds, return false

       if (friendlyPiece(destination, board)) return false;  // friedly piece is blocking the desired path, return false.

       if (start_array[1] == destination_array[1]){
            if (type.equals("wp")){
                if (start_array[0] < destination_array[0]) return false; // ensure that pawn is moving forward, if not return false.
            } else{
                if (start_array[0] > destination_array[0]) return false;
            }

            if (Math.abs(start_array[0] - destination_array[0]) > 2) return false; // if move is longer than 2 spaces, return false

            if (Math.abs(start_array[0] - destination_array[0]) == 2){
                if (moved == true){
                    return false; // if pawn has moved already and desired move is 2 spaces long, return false
                } else if (!board.getSpace(Piece.coordinateToInteger(destination)).type.equals("ES")){
                    return false;
                } else {
                    if (!clearPath(destination, board)) return false; // if piece is blocking its first jump 2 space, it cannot do it.
                }
                passant = true; // on pawns first move 2 space move, make passant true
                whenPassantMoved = board.getTotalMoveCount();
            } 
       } else if (type == "wp" && start_array[0]-1 == destination_array[0] && start_array[1]-1 == destination_array[1]){
            // if the destination is a valid diagonal and is filled with an enemy space continue, else return false
            if (board.getSpace(Piece.coordinateToInteger(destination)).type.charAt(0) == type.charAt(0) || board.getSpace(Piece.coordinateToInteger(destination)).type.charAt(0) == 'E') return false;
       } else if (type == "wp" && start_array[0]-1 == destination_array[0] && start_array[1]+1 == destination_array[1]){
            // if the destination is a valid diagonal and is filled with an enemy space continue, else return false
            if (board.getSpace(Piece.coordinateToInteger(destination)).type.charAt(0) == type.charAt(0) || board.getSpace(Piece.coordinateToInteger(destination)).type.charAt(0) == 'E') return false;
       } else if (type == "bp" && start_array[0]+1 == destination_array[0] && start_array[1]-1 == destination_array[1]){
            // if the destination is a valid diagonal and is filled with an enemy space continue, else return false
            if (board.getSpace(Piece.coordinateToInteger(destination)).type.charAt(0) == type.charAt(0) || board.getSpace(Piece.coordinateToInteger(destination)).type.charAt(0) == 'E') return false;
        } else if (type == "bp" && start_array[0]+1 == destination_array[0] && start_array[1]+1 == destination_array[1]){
            // if the destination is a valid diagonal and is filled with an enemy space continue, else return false
            if (board.getSpace(Piece.coordinateToInteger(destination)).type.charAt(0) == type.charAt(0) || board.getSpace(Piece.coordinateToInteger(destination)).type.charAt(0) == 'E') return false;
        } else {
            return false; // if move is not in straight line, not a move to take an enemy piece directly diagonal to the pawn
       }

       // check to see if this move puts your king into check. If it does print illegal move and return false.
       if (movePutsYouInCheck(destination, board)){
            System.out.println("move puts you in check"); 
            return false; 
        }
       
       return true;
    }

    public boolean clearPath(String destination, Board board){
        if (moved == true) return true;

        int [] start_array = Piece.coordinateToInteger(position);
        int y = 0;
        if (type.charAt(0) == 'b') {
            y = start_array[0]+1;
        } else {
            y = start_array[0]-1;
        }
        int x = start_array[1];
        int [] oneAhead = {y,x};

        if (board.getSpace(oneAhead).type == "ES") return true;

        return false;
    }

    public static void promote(char color, String position, char desiredPromote, Board board){
        if (desiredPromote == 'Q'){
            board.setSpace(Piece.coordinateToInteger(position), new Queen(color+"Q", position)); // when promote is called, pawn is turned into a queen.
        } else if (desiredPromote == 'R'){
            board.setSpace(Piece.coordinateToInteger(position), new Rook(color+"R", position)); // when promote is called, pawn is turned into a rook.
        } else if (desiredPromote == 'B'){
            board.setSpace(Piece.coordinateToInteger(position), new Bishop(color+"B", position)); // when promote is called, pawn is turned into a bishop.
        } else {
            board.setSpace(Piece.coordinateToInteger(position), new Knight(color+"N", position)); // when promote is called, pawn is turned into a knight.
        }
    }
}
