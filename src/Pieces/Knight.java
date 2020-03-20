package Pieces;

import Chessbot3.GameBoard.Board;

import static Chessbot3.Chess.imageDict;
import static Pieces.WhiteBlack.WHITE;

public class Knight extends SimplePiece {
    public Knight(WhiteBlack c) {
        super(c);
        canSprint = false;
        value = 280;
        symbol = 'N';
        if(c == WHITE){
            image = imageDict.get("horse_white");
        }else image = imageDict.get("horse_black");
    }
}
