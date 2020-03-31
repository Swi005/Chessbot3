package Chessbot3.Pieces.Types;

import Chessbot3.Pieces.PieceResources.SimplePiece;
import Chessbot3.Pieces.PieceResources.WhiteBlack;

import static Chessbot3.GuiMain.Chess.imageDict;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class Queen extends SimplePiece {
    public Queen(WhiteBlack c) {
        super(c);
        canSprint = true;
        inherentValue = 929;
        symbol = 'Q';
        if(c == WHITE){
            image = imageDict.get("queen_white");
        }else image = imageDict.get("queen_black");
    }
}
