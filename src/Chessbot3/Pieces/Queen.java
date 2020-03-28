package Chessbot3.Pieces;

import static Chessbot3.GuiMain.Chess.imageDict;
import static Chessbot3.Pieces.WhiteBlack.WHITE;

public class Queen extends SimplePiece {
    public Queen(WhiteBlack c) {
        super(c);
        posValue = new int[][] 
        {
            new int[] {6,   1,  -8,-104,  69,  24,  88,  26 },
            new int[] {14,  32,  60, -10,  20,  76,  57,  24},
            new int[] {-2,  43,  32,  60,  72,  63,  43,   2},
            new int[] {1, -16,  22,  17,  25,  20, -13,  -6},
            new int[] {-14, -15,  -2,  -5,  -1, -10, -20, -22},
            new int[] {-30,  -6, -13, -11, -16, -11, -16, -27},
            new int[] {-36, -18,   0, -19, -15, -15, -21, -38},
            new int[] {-39, -30, -31, -13, -31, -36, -34, -42}
        };
        canSprint = true;
        value = 929;
        symbol = 'Q';
        if(c == WHITE){
            image = imageDict.get("queen_white");
        }else image = imageDict.get("queen_black");
    }
}
