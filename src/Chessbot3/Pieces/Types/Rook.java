package Chessbot3.Pieces.Types;

import Chessbot3.Pieces.PieceResources.SimplePiece;
import Chessbot3.Pieces.PieceResources.WhiteBlack;

import static Chessbot3.GuiMain.Chess.imageDict;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class Rook extends SimplePiece {
    public Rook(WhiteBlack c) {
        super(c);
        canSprint = true;
        inherentValue = 479;
        symbol = 'R';
        if(c == WHITE){
            image = imageDict.get("rook_white");
        }else image = imageDict.get("rook_black");
    }
}