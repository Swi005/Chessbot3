package Chessbot3.Pieces;

import static Chessbot3.GUIMain.Chess.imageDict;
import static Chessbot3.Pieces.WhiteBlack.WHITE;

public class Rook extends SimplePiece {
    public Rook(WhiteBlack c) {
        super(c);
        canSprint = true;
        value = 479;
        symbol = 'R';
        if(c == WHITE){
            image = imageDict.get("rook_white");
        }else image = imageDict.get("rook_black");
    }
}