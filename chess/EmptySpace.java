package chess;

public class EmptySpace extends Piece {
    private char color;

    public EmptySpace(int[] coordinates){
        if (((coordinates[0] + coordinates[1]) % 2) == 0){
            color = 'W';
        } else {
            color = 'B';
        }
        type = "ES";
    }

    public String move(String destination, Board board){
        System.out.println("This is an empty square, you cannot move it!");
        return "ILLEGAL_MOVE";
    }

    public boolean moveValid(String destination, Board board){
        return false;
    }

    public String toString(){
        if (color == 'W'){
            return "  ";
        } else {
            return "##";
        }
    }
}
