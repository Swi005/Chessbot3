package Chessbot3.Pieces.Types;

import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.PieceResources.SimplePiece;
import Chessbot3.Pieces.PieceResources.WhiteBlack;
import Chessbot3.Pieces.PieceResources.iPiece;

import static Chessbot3.GuiMain.Chess.imageDict;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class Queen extends SimplePiece {
    public Queen(WhiteBlack c, Tuple posisition) {
        super(c, posisition);
        canSprint = true;
        inherentValue = 929;
        symbol = 'Q';
        if(c == WHITE)imageKey = "Qw";
        else imageKey = "Qb";
    }

    @Override
    public iPiece copy() {
        return new Queen(color, position.copy());
    }
}
