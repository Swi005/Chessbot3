package Chessbot3.Pieces.Types;

import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.PieceResources.SimplePiece;
import Chessbot3.Pieces.PieceResources.WhiteBlack;

import static Chessbot3.GuiMain.Chess.imageDict;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class Rook extends SimplePiece {
    public Rook(WhiteBlack c, Tuple position) {
        super(c, position);
        canSprint = true;
        inherentValue = 479;
        symbol = 'R';
        if(c == WHITE) imageKey = "Rw";
        else imageKey = "Rb";
    }

    @Override
    public String toString(){ return color + "Rook"; }
}