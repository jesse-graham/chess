package chess;

public class Board {
    private Piece[][] array;
    private String whiteKing;
    private String blackKing;
    private Boolean gameOver;
    protected String checking;
    private int totalMoveCount;

    public Board(){
        array = new Piece[8][8];
        gameOver = false;
        checking = null;
    }

    public boolean isGameOver(){
        return gameOver;
    }

    public void endGame(){
        gameOver = true;
    }

    public Piece getSpace(int[] coordinates){
        return array[coordinates[0]][coordinates[1]];
    }
    
    public void setSpace(int[] coordinates, Piece p){
        array[coordinates[0]][coordinates[1]] = p;
    }

    public String getWhiteKing(){
        return whiteKing;
    }

    public String getBlackKing(){
        return blackKing;
    }

    public void updateWhiteKing(String postion){
        whiteKing = postion;
    }

    public void updateBlackKing(String postion){
        blackKing = postion;
    }

    public void incrementTotalMoveCount(){
        totalMoveCount++;
    }

    public int getTotalMoveCount(){
        return totalMoveCount;
    }

    public void fillBoard(){
        array[0][0] = new Rook("bR", "a8");
        array[0][1] = new Knight("bN", "b8");
        array[0][2] = new Bishop("bB", "c8");
        array[0][3] = new Queen("bQ", "d8");
        array[0][4] = new King("bK", "e8");
        array[0][5] = new Bishop("bB", "f8");
        array[0][6] = new Knight("bN", "g8");
        array[0][7] = new Rook("bR", "h8");

        for(int k = 0; k < 8; k++){
            int[] x = {1,k};
            array[1][k] = new Pawn("bp", Piece.coordinateToString(x));
        }

        for (int i = 2; i < 6; i++){
            for (int j = 0; j < 8; j++){
                int[] a = {i,j};
                setSpace(a, new EmptySpace(a));
            }
        }

        for(int k = 0; k < 8; k++){
            int[] x = {6,k};
            array[6][k] = new Pawn("wp", Piece.coordinateToString(x));
        }

        array[7][0] = new Rook("wR", "a1");
        array[7][1] = new Knight("wN", "b1");
        array[7][2] = new Bishop("wB", "c1");
        array[7][3] = new Queen("wQ", "d1");
        array[7][4] = new King("wK", "e1");
        array[7][5] = new Bishop("wB", "f1");
        array[7][6] = new Knight("wN", "g1");
        array[7][7] = new Rook("wR", "h1");

        blackKing = "e8";
        whiteKing = "e1";
    }

    public void fillEmptyBoard(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                int[] a = {i,j};
                setSpace(a, new EmptySpace(a));
            }
        }
    }

    public void printBoard(){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                System.out.print(array[i][j] + " ");
            }
            System.out.print(8-i);
            System.out.println();
        }
        System.out.print("a  b  c  d  e  f  g  h");
    }

    public int kingInCheck(){
        // returns -1 if white king in check, 1 if black king in check, and 0 if none in check, returns 2 if a king is putting itself in check
        String bK = getBlackKing();
        String wK = getWhiteKing();

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                int[] coordinate = {i, j};
                if (getSpace(coordinate).type.charAt(0) == 'w') {
                    if (getSpace(coordinate).moveValid(bK, this)) {
                        if(getSpace(coordinate).type.equals("wK")) return 2;
                        checking = getSpace(coordinate).type;
                        System.out.println(Piece.coordinateToString(coordinate));
                        return 1;
                    } 
                } else if (getSpace(coordinate).type.charAt(0) == 'b') {
                    if (getSpace(coordinate).moveValid(wK, this)){
                        if(getSpace(coordinate).type.equals("bK")) return 2;
                        checking = getSpace(coordinate).type;
                        System.out.println(Piece.coordinateToString(coordinate));
                        return -1;  
                    } 
                }
            }
        }
        return 0;
    }

    public boolean whiteCheckMate(){
        // returns true if white king is in check mate and flase if it isn't
         // sees if any of your pieces can make a move that results in your king no longer being in check

        
            for (int x = 0; x < 8; x++){
                for (int y = 0; y < 8; y++){
                    if (getSpace(new int[] {x,y}).type.charAt(0) == 'w'){
                        if (getSpace(new int[] {x,y}).hasValidMove(this)) return false;   
                    }   
                }
            }

        return true;
    }

    public boolean blackCheckMate(){
        // sees if any of your pieces can make a move that results in your king no longer being in check
            for (int x = 0; x < 8; x++){
                for (int y = 0; y < 8; y++){
                    if (getSpace(new int[] {x,y}).type.charAt(0) == 'b'){
                        if (getSpace(new int[] {x,y}).hasValidMove(this)) return false;   
                    }   
                }
            }
        
        return true;
    }
}
