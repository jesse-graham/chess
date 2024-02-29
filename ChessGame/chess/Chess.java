// Jesse Graham, Arsal Shaikh
package chess;

import java.util.ArrayList;

import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;

class ReturnPiece {
	static enum PieceType {WP, WR, WN, WB, WQ, WK, 
		            BP, BR, BN, BB, BK, BQ};
	static enum PieceFile {a, b, c, d, e, f, g, h};
	
	PieceType pieceType;
	PieceFile pieceFile;
	int pieceRank;  // 1..8
	public String toString() {
		return ""+pieceFile+pieceRank+":"+pieceType;
	}
	public boolean equals(Object other) {
		if (other == null || !(other instanceof ReturnPiece)) {
			return false;
		}
		ReturnPiece otherPiece = (ReturnPiece)other;
		return pieceType == otherPiece.pieceType &&
				pieceFile == otherPiece.pieceFile &&
				pieceRank == otherPiece.pieceRank;
	}
}

class ReturnPlay {
	enum Message {ILLEGAL_input, DRAW, 
				  RESIGN_BLACK_WINS, RESIGN_WHITE_WINS, 
				  CHECK, CHECKMATE_BLACK_WINS,	CHECKMATE_WHITE_WINS, 
				  STALEMATE};
	
	ArrayList<ReturnPiece> piecesOnBoard;
	Message message;
}

public class Chess {
	
	enum Player { white, black }
	public static Board board;
	
	/**
	 * Plays the next input for whichever player has the turn.
	 * 
	 * @param input String for next input, e.g. "a2 a3"
	 * 
	 * @return A ReturnPlay instance that contains the result of the input.
	 *         See the section "The Chess class" in the assignment description for details of
	 *         the contents of the returned ReturnPlay instance.
	 */
	public static ReturnPlay play(String move) {
		ReturnPlay returnPlay = new ReturnPlay();
		/* FILL IN THIS METHOD */
		String input = move.replaceAll("\\s", "");

		if (input.length() == 4){
			// if just simple input with no extra modifiers
			String start = input.substring(0,2);
			String end = input.substring(2,4);

			String message = board.getSpace(Piece.coordinateToInteger(start)).move(end, board);
			if (board.getSpace(Piece.coordinateToInteger(end)).toBePromoted == true){
				Pawn.promote((board.getSpace(Piece.coordinateToInteger(end)).type.charAt(0)), end, 'Q', board);
				if (board.blackCheckMate() == true){
					message = "Checkmate, black wins!";
				} else if (board.whiteCheckMate() == true){
					message = "Checkmate, white wins!";
				} else if (board.kingInCheck() != 0){
					message = "Check";
				}
			}

			if (message != null){
				if (message.equals("ILLEGAL_MOVE")) returnPlay.message = ReturnPlay.Message.ILLEGAL_input;
				if (message.equals("Check")) returnPlay.message = ReturnPlay.Message.CHECK;
				if (message.equals("Checkmate, white wins!")) returnPlay.message = ReturnPlay.Message.CHECKMATE_WHITE_WINS;
				if (message.equals("Checkmate, black wins!")) returnPlay.message = ReturnPlay.Message.CHECKMATE_BLACK_WINS;
			} 
			parseFromBoard(returnPlay);
		} else if (input.length() == 5){
			// input that involves pawn promotion
			String start = input.substring(0,2);
			String end = input.substring(2,4);
			char desiredPromote = input.charAt(4);

			String message = board.getSpace(Piece.coordinateToInteger(start)).move(end, board);
			if (board.getSpace(Piece.coordinateToInteger(end)).toBePromoted == true){
				Pawn.promote((board.getSpace(Piece.coordinateToInteger(end)).type.charAt(0)), end, desiredPromote, board);
			}

			if (message != null){
				if (message.equals("ILLEGAL_MOVE")) returnPlay.message = ReturnPlay.Message.ILLEGAL_input;
			} else {
				if (board.blackCheckMate() == true){
					returnPlay.message = ReturnPlay.Message.CHECKMATE_BLACK_WINS;
				} else if (board.whiteCheckMate() == true){
					returnPlay.message = ReturnPlay.Message.CHECKMATE_WHITE_WINS;
				} else if (board.kingInCheck() != 0){
					returnPlay.message = ReturnPlay.Message.CHECK;
				}
			}
			parseFromBoard(returnPlay);
		} else if (input.equals("resign")){
			if (board.getTotalMoveCount() % 2 == 0){
				returnPlay.message = ReturnPlay.Message.RESIGN_BLACK_WINS;
			} else {
				returnPlay.message = ReturnPlay.Message.RESIGN_WHITE_WINS;
			}
			parseFromBoard(returnPlay);
			board.endGame();
		} else if (input.length() == 8 && input.substring(4, 8).equals("draw") ){
			String start = input.substring(0,2);
			String end = input.substring(2,4);

			String message = board.getSpace(Piece.coordinateToInteger(start)).move(end, board);
			if (board.getSpace(Piece.coordinateToInteger(end)).toBePromoted == true){
				Pawn.promote((board.getSpace(Piece.coordinateToInteger(end)).type.charAt(0)), end, 'Q', board);
			}

			if (message != null && message.equals("ILLEGAL_MOVE")){
				parseFromBoard(returnPlay);
				returnPlay.message = ReturnPlay.Message.ILLEGAL_input;
				return returnPlay;
			}
			
			parseFromBoard(returnPlay);

			returnPlay.message = ReturnPlay.Message.DRAW;
			board.endGame();
		}

		/* FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY */
		/* WHEN YOU FILL IN THIS METHOD, YOU NEED TO RETURN A ReturnPlay OBJECT */
		return returnPlay;
	}
	
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		/* FILL IN THIS METHOD */
		board = new Board();
		board.fillBoard();
	}

	public static void parseFromBoard(ReturnPlay returnPlay){
		returnPlay.piecesOnBoard = new ArrayList<ReturnPiece>();
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				if (!board.getSpace(new int[] {i,j}).type.equals("ES")){
					ReturnPiece returnPiece = new ReturnPiece();

					Piece piece = board.getSpace(new int[] {i,j});
					PieceType type = stringToPieceType(piece.type);
					PieceFile file = charToFile(piece.position.charAt(0));
					int rank = piece.position.charAt(1) - '0';

					returnPiece.pieceType = type;
					returnPiece.pieceFile = file;
					returnPiece.pieceRank = rank;
					
					returnPlay.piecesOnBoard.add(returnPiece);
				}
			}
		}
	}
	
	public static PieceFile charToFile(char file){
		if (file == 'a'){
			return PieceFile.a;
		} else if (file == 'b'){
			return PieceFile.b;
		} else if (file == 'c'){
			return PieceFile.c;
		} else if (file == 'd'){
			return PieceFile.d;
		} else if (file == 'e'){
			return PieceFile.e;
		} else if (file == 'f'){
			return PieceFile.f;
		} else if (file == 'g'){
			return PieceFile.g;
		} else {
			return PieceFile.h;
		}
	}

	public static PieceType stringToPieceType(String type){
		if (type.charAt(0) == 'w'){
			if (type.charAt(1) == 'p'){
				return PieceType.WP;
			} else if (type.charAt(1) == 'B'){
				return PieceType.WB;
			} else if (type.charAt(1) == 'K'){
				return PieceType.WK;
			} else if (type.charAt(1) == 'N'){
				return PieceType.WN;
			} else if (type.charAt(1) == 'Q'){
				return PieceType.WQ;
			} else if (type.charAt(1) == 'R'){
				return PieceType.WR;
			} 
		} else if (type.charAt(0) == 'b') {
			if (type.charAt(1) == 'p'){
				return PieceType.BP;
			} else if (type.charAt(1) == 'B'){
				return PieceType.BB;
			} else if (type.charAt(1) == 'K'){
				return PieceType.BK;
			} else if (type.charAt(1) == 'N'){
				return PieceType.BN;
			} else if (type.charAt(1) == 'Q'){
				return PieceType.BQ;
			} else if (type.charAt(1) == 'R'){
				return PieceType.BR;
			} 
		} 
		return null;
	}
}

