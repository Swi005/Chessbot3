package Pieces;

import Chessbot3.Color;
import Chessbot3.GameBoard.Board;

import static Chessbot3.Chess.imageDict;
import static Chessbot3.Color.WHITE;

public class King extends SimplePiece {
    public King(Color c, Board gameboard) {
        super(c, gameboard);
        canSprint = false;
        value = 69420;
        symbol = 'K';
        if(c == WHITE){
            image = imageDict.get("king_white");
        }else image = imageDict.get("king_black");
    }
}