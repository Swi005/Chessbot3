package Pieces;

import Chessbot3.GameBoard.Board;

import static Chessbot3.Chess.imageDict;
import static Pieces.WhiteBlack.WHITE;

public class Rook extends SimplePiece {
    public Rook(WhiteBlack c, Board gameboard) {
        super(c, gameboard);
        canSprint = true;
        value = 479;
        symbol = 'R';
        if(c == WHITE){
            image = imageDict.get("rook_white");
        }else image = imageDict.get("rook_black");
    }
}