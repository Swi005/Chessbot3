package Chessbot3.Pieces;

import static Chessbot3.GuiMain.Chess.imageDict;
import static Chessbot3.Pieces.WhiteBlack.WHITE;

public class Queen extends SimplePiece {
    public Queen(WhiteBlack c) {
        super(c);
        canSprint = true;
        value = 929;
        symbol = 'Q';
        if(c == WHITE){
            image = imageDict.get("queen_white");
        }else image = imageDict.get("queen_black");
    }
}
