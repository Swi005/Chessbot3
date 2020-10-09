package Chessbot3.Pieces.Types;

import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.PieceResources.SimplePiece;
import Chessbot3.Pieces.PieceResources.WhiteBlack;

import static Chessbot3.GuiMain.Chess.imageDict;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class Bishop extends SimplePiece {
    public Bishop(WhiteBlack c, Tuple position) {
        super(c, position);
        canSprint = true;
        inherentValue = 320;
        symbol = 'B';
        if(c == WHITE)imageKey = "Bw";
        else imageKey = "Bb";
    }
}
