package Chessbot3.Pieces.Types;

import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.PieceResources.SimplePiece;
import Chessbot3.Pieces.PieceResources.WhiteBlack;
import Chessbot3.Pieces.PieceResources.iPiece;

import static Chessbot3.GuiMain.Chess.imageDict;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class Knight extends SimplePiece {
    public Knight(WhiteBlack c) {
        super(c);
        canSprint = false;
        inherentValue = 280;
        symbol = 'N';
        if(c == WHITE) imageKey = "Nw";
        else imageKey = "Nb";
    }
}
