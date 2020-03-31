package Chessbot3.Pieces.Types;

import Chessbot3.Pieces.PieceResources.SimplePiece;
import Chessbot3.Pieces.PieceResources.WhiteBlack;

import static Chessbot3.GuiMain.Chess.imageDict;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class Bishop extends SimplePiece {
    public Bishop(WhiteBlack c) {
        super(c);
        canSprint = true;
        inherentValue = 320;
        symbol = 'B';
        if(c == WHITE){
            image = imageDict.get("bishop_white");
        }else image = imageDict.get("bishop_black");
    }
}
