package Chessbot3.Pieces.Types;

import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.PieceResources.SimplePiece;
import Chessbot3.Pieces.PieceResources.WhiteBlack;

import static Chessbot3.GuiMain.Chess.imageDict;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class Knight extends SimplePiece {
    public Knight(WhiteBlack c, Tuple position) {
        super(c, position);
        canSprint = false;
        inherentValue = 280;
        symbol = 'N';
        if(c == WHITE) imageKey = "Nw";
        else imageKey = "Nb";
    }

    @Override
    public String toString(){ return color + "Knight"; }
}
