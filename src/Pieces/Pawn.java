package Pieces;

import Chessbot3.Color;
import Chessbot3.GameBoard.Board;

import static Chessbot3.Chess.imageDict;
import static Chessbot3.Color.WHITE;

public class Pawn extends SimplePiece {
    public Pawn(Color c, Board gameboard) {
        super(c, gameboard);
        canSprint = false;
        value = 100;
        symbol = 'P';
        if(c == WHITE){
            image = imageDict.get("pawn_white");
        }else image = imageDict.get("pawn_black");
    }
    // TODO: 11.03.2020 Override getMoves(), siden bondelogikk fungerer helt annerledes enn resten av brikkene. 
}
