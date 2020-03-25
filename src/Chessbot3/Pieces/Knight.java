package Chessbot3.Pieces;

import static Chessbot3.GuiMain.Chess.imageDict;
import static Chessbot3.Pieces.WhiteBlack.WHITE;

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
