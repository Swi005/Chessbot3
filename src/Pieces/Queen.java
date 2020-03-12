package Pieces;

import Chessbot3.Color;
import Chessbot3.GameBoard.Board;

import static Chessbot3.Chess.imageDict;
import static Chessbot3.Color.WHITE;

public class Queen extends SimplePiece {
    public Queen(Color c, Board gameboard) {
        super(c, gameboard);
        canSprint = true;
        value = 929;
        symbol = 'Q';
        if(c == WHITE){
            image = imageDict.get("queen_white");
        }else image = imageDict.get("queen_black");
    }
}