package Chessbot3.Pieces;

import static Chessbot3.GuiMain.Chess.imageDict;
import static Chessbot3.Pieces.WhiteBlack.WHITE;

public class Bishop extends SimplePiece {
    public Bishop(WhiteBlack c) {
        super(c);
        posValue = new int[][] 
        {
            new int[] {-59, -78, -82, -76, -23,-107, -37, -50},
            new int[] {-11,  20,  35, -42, -39,  31,   2, -22},
            new int[] {-11,  20,  35, -42, -39,  31,   2, -22},
            new int[] {-11,  20,  35, -42, -39,  31,   2, -22},
            new int[] {13,  10,  17,  23,  17,  16,   0,   7},
            new int[] {14,  25,  24,  15,   8,  25,  20,  15},
            new int[] {19, 20, 11, 6, 7, 6, 20, 16},
            new int[] { -7,   2, -15, -12, -14, -15, -10, -10}
        };
        canSprint = true;
        value = 320;
        symbol = 'B';
        if(c == WHITE){
            image = imageDict.get("bishop_white");
        }else image = imageDict.get("bishop_black");
    }
}
