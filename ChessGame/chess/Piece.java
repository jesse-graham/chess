package chess;

public abstract class Piece {
    protected String type;
    protected String position;
    protected boolean moved;
    protected boolean toBePromoted;
    protected boolean passant;
    protected int whenPassantMoved;
    protected int timesmoved;
    protected static int totalMoveCount;
    
    public boolean equals(Piece p) {
        if (p == null || !(p instanceof Piece)){
            return false;
        }

        Piece other = (Piece)p;
        return type.equals(other.type) && position.equals(other.position);
    }

    public abstract String move(String destination, Board board);

    public abstract boolean moveValid(String destination, Board board);

    public String toString(){
        return type;
    }

    public static void incrementMoves(){
        totalMoveCount++;
    }

    public static String coordinateToString(int[] arrayCoordinate){
        if (arrayCoordinate.length != 2) {
            return "";
        }

        // Extract the row and column index from the array coordinate
        int rowIndex = arrayCoordinate[0];
        int columnIndex = arrayCoordinate[1];

        // Convert the column index to a letter ('a' to 'h')
        char columnLetter = (char)('a' + columnIndex);
        // Convert the 2D array row index back to a chess row number
        int rowNumber = 8 - rowIndex;

        // Combine the column letter and row number into the chess board coordinate
        return String.valueOf(columnLetter) + rowNumber;
    }

    public static int[] coordinateToInteger(String chessCoordinate){
        // Method to convert the alfanumerical board value (ex: b4, c6) to the numerical array value (board[x][y])
        int [] arrayCoordinate = new int [2];
        arrayCoordinate[0] = -1;
        arrayCoordinate[1] = -1;

        if (chessCoordinate == null) return arrayCoordinate;
        if (chessCoordinate.length() != 2) return arrayCoordinate;

        // Extract the column letter and row number from the chess coordinate
        char columnLetter = chessCoordinate.charAt(0);
        int rowNumber = Integer.parseInt(chessCoordinate.substring(1));    
        // Convert the column letter to an array index (0 to 7)
        int rowIndex = columnLetter - 'a';
        // Convert the chess row number to a 2D array row index
        int columnIndex = 8 - rowNumber;
        // Return the 2D array coordinates as an array
        arrayCoordinate[0] = columnIndex;
        arrayCoordinate[1] = rowIndex;
        return arrayCoordinate;
    }

    public static boolean inBounds(int [] coordinates){
        // this method checks to see if the specifed coordinates are within the bounds of the 8x8 board array.
        if (coordinates[0] < 0 || coordinates[0] > 7) return false;

        if (coordinates[1] < 0 || coordinates[1] > 7) return false;

        return true;
    }

    public static boolean isDiagonal(int[] space1, int[] space2) {
        int rowDiff = Math.abs(space1[0] - space2[0]);
        int colDiff = Math.abs(space1[1] - space2[1]);
        
        return rowDiff == colDiff;
    }

    public boolean friendlyPiece(String destination, Board board){   
        // method to check if desired move is blocked by a friendly piece.
        int[] destination_array = Piece.coordinateToInteger(destination);
        if (type.charAt(0) == board.getSpace(destination_array).type.charAt(0)) return true;

        return false;
    }

    public boolean clearPath(String destination, Board board){
        return false;
    }

    public static void takePiece(String start, String destination, Board board){
        int[] start_array = Piece.coordinateToInteger(start);
        int[] destination_array = Piece.coordinateToInteger(destination);
        
        Piece piece = board.getSpace(start_array);

        piece.position = destination;

        board.setSpace(destination_array, piece);
        
        board.setSpace(start_array, new EmptySpace(start_array));
    }

    public static boolean possibleMove(int[][] possibleMoves, int[] desiredMove){
        for(int i = 0; i < 8; i++){
            int[] move = possibleMoves[i];
            if (move[0] == desiredMove[0] && move[1] == desiredMove[1]) return true; // go through array of possible moves, and return true of move is possible.
        }
        return false;
    }

    public boolean movePutsYouInCheck(String destination, Board board){
        Piece destinationPiece = board.getSpace(Piece.coordinateToInteger(destination));
        String start = position;
        boolean result = false;

        if (type.charAt(0) == 'w'){
            // make the move. If the white king is in check set result to true. Reverse the move.
            takePiece(start, destination, board);
            if (type.equals("wK")) board.updateWhiteKing(destination);
            if (board.kingInCheck() == -1 || board.kingInCheck() == 2) result = true;
            takePiece(destination, start, board);
            if (type.equals("wK")) board.updateWhiteKing(start);
            board.setSpace(Piece.coordinateToInteger(destination), destinationPiece);
        } else{
            // make the move. If the black king is in check set result to true. Reverse the move.
            takePiece(start, destination, board);
            if (type.equals("bK")) board.updateBlackKing(destination);
            if (board.kingInCheck() == 1 || board.kingInCheck() == 2) result = true;
            takePiece(destination, start, board);
            if (type.equals("bK")) board.updateBlackKing(start);
            board.setSpace(Piece.coordinateToInteger(destination), destinationPiece);
        }

        return result;
    }

    public boolean hasValidMove(Board board){
        for (int i = 0; i < 8; i++){
            for (int j = 0 ; j < 8; j++){
                int [] destination_array = {i,j};
                if (moveValid(Piece.coordinateToString(destination_array), board)) return true;
            }
        }
        return false;
    }
}
