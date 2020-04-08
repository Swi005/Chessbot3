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
        if(c == WHITE) imageKey = "Rw";
        else imageKey = "Rb";
    }
}