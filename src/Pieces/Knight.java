package Pieces;

import Chessbot3.Color;
import Chessbot3.GameBoard.Board;

import static Chessbot3.Chess.imageDict;
import static Chessbot3.Color.WHITE;

public class Knight extends SimplePiece {
    public Knight(Color c, Board gameboard) {
        super(c, gameboard);
        canSprint = false;
        value = 280;
        symbol = 'N';
        if(c == WHITE){
            image = imageDict.get("horse_white");
        }else image = imageDict.get("horse_black");
    }
}
