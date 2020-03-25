package Chessbot3.Pieces;

import static Chessbot3.GUIMain.Chess.imageDict;
import static Chessbot3.Pieces.WhiteBlack.WHITE;

public class Bishop extends SimplePiece {
    public Bishop(WhiteBlack c) {
        super(c);
        canSprint = true;
        value = 320;
        symbol = 'B';
        if(c == WHITE){
            image = imageDict.get("bishop_white");
        }else image = imageDict.get("bishop_black");
    }
}
