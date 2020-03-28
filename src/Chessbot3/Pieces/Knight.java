package Chessbot3.Pieces;

import static Chessbot3.GuiMain.Chess.imageDict;
import static Chessbot3.Pieces.WhiteBlack.WHITE;

public class Knight extends SimplePiece {
    public Knight(WhiteBlack c) {
        super(c);
        posValue = new int[][] 
        {
            new int[] {-66, -53, -75, -75, -10, -55, -58, -70},
            new int[] { -3,  -6, 100, -36,   4,  62,  -4, -14},
            new int[] {10,  67,   1,  74,  73,  27,  62,  -2},
            new int[] {24,  24,  45,  37,  33,  41,  25,  17},
            new int[] {-1,   5,  31,  21,  22,  35,   2,   0},
            new int[] {-18,  10,  13,  22,  18,  15,  11, -14},
            new int[] { -23, -15,   2,   0,   2,   0, -23, -20},
            new int[] {-74, -23, -26, -24, -19, -35, -22, -69}
        };
        canSprint = false;
        value = 280;
        symbol = 'N';
        if(c == WHITE){
            image = imageDict.get("horse_white");
        }else image = imageDict.get("horse_black");
    }
}
