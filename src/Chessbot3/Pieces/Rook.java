package Chessbot3.Pieces;

import static Chessbot3.GuiMain.Chess.imageDict;
import static Chessbot3.Pieces.WhiteBlack.WHITE;

public class Rook extends SimplePiece {
    public Rook(WhiteBlack c) {
        super(c);
        posValue = new int[][] 
        {
            new int[] {35,  29,  33,   4,  37,  33,  56,  50 },
            new int[] {55,  29,  56,  67,  55,  62,  34,  60},
            new int[] { 19,  35,  28,  33,  45,  27,  25,  15},
            new int[] {0,   5,  16,  13,  18,  -4,  -9,  -6},
            new int[] {-28, -35, -16, -21, -13, -29, -46, -30},
            new int[] {-42, -28, -42, -25, -25, -35, -26, -46},
            new int[] {-53, -38, -31, -26, -29, -43, -44, -53},
            new int[] {-30, -24, -18,   5,  -2, -18, -31, -32}
        };
        canSprint = true;
        inherentValue = 479;
        symbol = 'R';
        if(c == WHITE){
            image = imageDict.get("rook_white");
        }else image = imageDict.get("rook_black");
    }
}