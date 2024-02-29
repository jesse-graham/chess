package chess;

public class King extends Piece {
    public King(String type, String position){
        this.type = type;
        this.position = position;
        moved = false;
    }

    public String move(String destination, Board board){
        if (board.isGameOver()) return "ILLEGAL_MOVE";
    
        if (type.charAt(0) == 'b'){
            if (board.getTotalMoveCount() % 2 == 0) {
                System.out.println("ILLEGAL_MOVE, not your turn");
                return "Not Your Turn";
            } 
        } else {
            if (board.getTotalMoveCount() % 2 != 0){
                System.out.println("ILLEGAL_MOVE, not your turn");
                return "Not Your Turn";
            } 
        }

        if (castling(board, destination)) return null;

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

        if (type.charAt(0) == 'b'){
            board.updateBlackKing(destination); // update possition of the black king
        } else {
            board.updateWhiteKing(destination); // update posstion of the white king
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

        int[][] possibleMoves = {
            {start_array[0]-1, start_array[1]-1},
            {start_array[0]-1, start_array[1]},
            {start_array[0]-1, start_array[1]+1},
            {start_array[0], start_array[1]+1},
            {start_array[0]+1, start_array[1]+1},
            {start_array[0]+1, start_array[1]},
            {start_array[0]+1, start_array[1]-1},
            {start_array[0], start_array[1]-1},
        };
        if (!possibleMove(possibleMoves, destination_array)) return false; // see if move is possible.

        // check to see if this move puts your king into check. If it does print illegal move and return false.
        if (movePutsYouInCheck(destination, board)){
            System.out.println("move puts you in check"); 
            return false; 
        }

        return true;
    }

    public boolean castling(Board board, String destination){
        int [] destination_array = Piece.coordinateToInteger(destination);
            // returns true if calsing was worked 
        if (type.charAt(0) == 'b' ) {
            boolean validCastle = true;
            // king is black
            //make sure king is not in check
            if (board.kingInCheck() == 1){
                validCastle = false;
            }
            

            if (destination.equals("c8")){
                // left movement
                // if rook is not in correct posstion and king and rook have moved already, return false
                if (moved != false || board.getSpace(Piece.coordinateToInteger("a8")).moved != false){
                    validCastle = false;
                }
                // check if left side is clear
                //check to make sure path is clear
                if (!(board.getSpace(Piece.coordinateToInteger("b8")).type.equals("ES") && board.getSpace(Piece.coordinateToInteger("c8")).type.equals("ES") && board.getSpace(Piece.coordinateToInteger("d8")).type.equals("ES"))){
                    validCastle = false;
                }

                //check to see that path does not put you into check
                board.updateBlackKing("c8");
                if (board.kingInCheck() == 1){
                    board.updateBlackKing("d8");
                    validCastle = false;
                }
                board.updateBlackKing("b8");
                if (board.kingInCheck() == 1){
                    board.updateBlackKing("d8");
                    validCastle = false;
                }
                board.updateBlackKing("d8");
                if (board.kingInCheck() == 1){
                    board.updateBlackKing("b8");
                    validCastle = false;
                }
                board.updateBlackKing("d8");
                //king goes to c8, rook goes to d8
                if  (validCastle){
                    board.setSpace(destination_array, this);
                    board.updateWhiteKing("c8");
                    board.getSpace(Piece.coordinateToInteger("c8")).position = "c8";
                    board.setSpace(Piece.coordinateToInteger("d8"), board.getSpace(Piece.coordinateToInteger("a8")));
                    board.getSpace(Piece.coordinateToInteger("d8")).position = "d8";
                    board.setSpace(Piece.coordinateToInteger("e8"), new EmptySpace(Piece.coordinateToInteger("e8")));
                    board.setSpace(Piece.coordinateToInteger("a8"), new EmptySpace(Piece.coordinateToInteger("a8")));
                    board.incrementTotalMoveCount();
                    moved = true;
                    return true;
                }
            } else if (destination.equals("g8")){
                //right movement
                // if rook is not in correct posstion and king and rook have moved already, return false
                if (moved != false || board.getSpace(Piece.coordinateToInteger("h8")).moved != false){
                    validCastle = false;
                }
                // check if right side is clear
                if (!(board.getSpace(Piece.coordinateToInteger("f8")).type.equals("ES") && board.getSpace(Piece.coordinateToInteger("g8")).type.equals("ES"))){
                    validCastle = false;
                }
            
                board.updateBlackKing("g8");
                if (board.kingInCheck() == 1){
                    board.updateBlackKing("d8");
                    validCastle = false;
                }
                board.updateBlackKing("f8");
                if (board.kingInCheck() == 1){
                    board.updateBlackKing("d8");
                    validCastle = false;
                }
                board.updateBlackKing("d8");

                // King goes to g8, rook goes to f8
                if (validCastle){
                    board.setSpace(destination_array, this);
                    board.updateWhiteKing("g8");
                    board.getSpace(Piece.coordinateToInteger("g8")).position = "g8";
                    board.setSpace(Piece.coordinateToInteger("f8"), board.getSpace(Piece.coordinateToInteger("h8")));
                    board.getSpace(Piece.coordinateToInteger("f8")).position = "f8";
                    board.setSpace(Piece.coordinateToInteger("e8"), new EmptySpace(Piece.coordinateToInteger("e8")));
                    board.setSpace(Piece.coordinateToInteger("h8"), new EmptySpace(Piece.coordinateToInteger("h8")));
                    board.incrementTotalMoveCount();
                    moved = true;
                    return true;
                }
            } 
        } else {
            boolean validCastle = true;
            // king is white
            // make sure king is not in check
            if (board.kingInCheck() == -1){
                validCastle = false;
            }
            // if rook is not in correct posstion and king and rook have moved already, return false
            if (moved != false || board.getSpace(Piece.coordinateToInteger("a1")).moved != false){
                validCastle = false;
            }
            //check to make sure path is clear
            
            if (destination.equals("c1")){
                // left movement
                // check if left side is clear
                if (!(board.getSpace(Piece.coordinateToInteger("b1")).type.equals("ES") && board.getSpace(Piece.coordinateToInteger("c1")).type.equals("ES") && board.getSpace(Piece.coordinateToInteger("d1")).type.equals("ES"))){
                    validCastle = false;
                }

                // check to make sure path doesn't put king in check
                board.updateWhiteKing("c1");
                if (board.kingInCheck() == -1){
                    board.updateWhiteKing("d1");
                    validCastle = false;
                }
                board.updateWhiteKing("b1");
                if (board.kingInCheck() == -1){
                    board.updateWhiteKing("d1");
                    validCastle = false;
                }
                board.updateWhiteKing("d1");
                if (board.kingInCheck() == -1){
                    board.updateWhiteKing("b1");
                    validCastle = false;
                }
                board.updateWhiteKing("d1");
                //king goes to c1, rook goes to d1
                if (validCastle){
                    board.setSpace(destination_array, this);
                    board.updateWhiteKing("c1");
                    board.getSpace(Piece.coordinateToInteger("c1")).position = "c1";
                    board.setSpace(Piece.coordinateToInteger("d1"), board.getSpace(Piece.coordinateToInteger("a1")));
                    board.getSpace(Piece.coordinateToInteger("d1")).position = "d1";
                    board.setSpace(Piece.coordinateToInteger("a1"), new EmptySpace(Piece.coordinateToInteger("a1")));
                    board.setSpace(Piece.coordinateToInteger("e1"), new EmptySpace(Piece.coordinateToInteger("e1")));
                    board.incrementTotalMoveCount();
                    moved = true;
                    return true;
                }
            } else if (destination.equals("g1")){
                //right movement
                // make sure king is not in check
                if (board.kingInCheck() == -1){
                    validCastle = false;
                }
                // if rook is not in correct posstion and king and rook have moved already, return false
                if (moved != false || board.getSpace(Piece.coordinateToInteger("h1")).moved != false){
                    validCastle = false;
                }
                // check if right side is clear
                if (!(board.getSpace(Piece.coordinateToInteger("f1")).type.equals("ES") && board.getSpace(Piece.coordinateToInteger("g1")).type.equals("ES"))){
                    validCastle = false;
                }

                board.updateWhiteKing("g1");
                if (board.kingInCheck() == -1){
                    board.updateWhiteKing("d1");
                    validCastle = false;
                }
                board.updateWhiteKing("f1");
                if (board.kingInCheck() == -1){
                    board.updateWhiteKing("d1");
                    validCastle = false;
                }
                board.updateWhiteKing("d1");

                // King goes to g1, rook goes to f1
                if (validCastle){
                    board.setSpace(destination_array, this);
                    board.updateWhiteKing("g1");
                    board.getSpace(Piece.coordinateToInteger("g1")).position = "g1";
                    board.setSpace(Piece.coordinateToInteger("f1"), board.getSpace(Piece.coordinateToInteger("h1")));
                    board.getSpace(Piece.coordinateToInteger("f1")).position = "f1";
                    board.setSpace(Piece.coordinateToInteger("h1"), new EmptySpace(Piece.coordinateToInteger("h1")));
                    board.setSpace(Piece.coordinateToInteger("e1"), new EmptySpace(Piece.coordinateToInteger("e1")));
                    board.incrementTotalMoveCount();
                    moved = true;
                    return true;
                }
            } 
        }
        return false;
    }
}
