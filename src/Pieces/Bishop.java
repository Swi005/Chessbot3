package Pieces;

import Chessbot3.Color;
import Chessbot3.GameBoard.Board;

import static Chessbot3.Chess.imageDict;
import static Chessbot3.Color.WHITE;

public class Bishop extends SimplePiece {
    public Bishop(Color c, Board gameboard) {
        super(c, gameboard);
        canSprint = true;
        value = 320;
        symbol = 'B';
        if(c == WHITE){
            image = imageDict.get("bishop_white");
        }else image = imageDict.get("bishop_black");
    }
}