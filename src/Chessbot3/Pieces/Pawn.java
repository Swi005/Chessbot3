package Chessbot3.Pieces;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;

import java.util.ArrayList;

import static Chessbot3.GuiMain.Chess.*;
import static Chessbot3.Pieces.WhiteBlack.BLACK;
import static Chessbot3.Pieces.WhiteBlack.WHITE;

public class Pawn extends SimplePiece {

    public Pawn(WhiteBlack c) {
        super(c);
        posValue = new int[][] 
        {
            new int[] {100,   100,   100,   100,   100,   100,   100,  100,},//100 causce get new good piece right?
            new int[] { 78,  83,  86,  73, 102,  82,  85,  90},
            new int[] {7,  29,  21,  44,  40,  31,  44,   7},
            new int[] {-17,  16,  -2,  15,  14,   0,  15, -13},
            new int[] {-26,   3,  10,   9,   6,   1,   0, -23},
            new int[] {-22,   9,   5, -11, -10,  -2,   3, -19},
            new int[] { -31,   8,  -7, -37, -36, -14,   3, -31},
            new int[] {0,   0,   0,   0,   0,   0,   0,  0}
        };
        canSprint = false;
        value = 100;
        symbol = 'P';
        if(c == WHITE){
            image = imageDict.get("pawn_white");
        }else image = imageDict.get("pawn_black");
    }
    @Override
    public ArrayList<Move> getMoves(Board bård){
        Tuple<Integer, Integer> passantPos = bård.GetPassantPos();
        ArrayList<Move> ret = new ArrayList<>();
        Tuple[] directions;
        if(color == WHITE){
            directions = direcDict.get('P');
        } else directions = direcDict.get('p');

        Integer fraX = getX(bård);
        Integer fraY = getY(bård);
        Tuple<Integer, Integer> fraPos = new Tuple<>(fraX, fraY);

        for(Tuple<Integer, Integer> direc : directions){ //Looper igjennom alle retningene den kan gå.
            Integer tilX = fraX+direc.getX();
            Integer tilY = fraY+direc.getY();
            Tuple<Integer, Integer> tilPos = new Tuple(tilX, tilY);

            if(tilX < 0 || tilY < 0 || tilX > 7 || tilY > 7) break; //Om den holder på å gå utenfor brettet.
            iPiece mål = bård.GetPiece(tilPos);

            if(direc.getX() == -1 || direc.getX() == 1){ //Om den prøver å gå skrått. Da trenger den at det står en fiendtlig brikke der, eller at den kan ta en passant.
                if (mål != null && this.isOppositeColor(mål) || tilPos.equals(passantPos)) {
                    ret.add(new Move(fraPos, tilPos));
                }
            }
            if(direc.getX() == 0 && mål == null){ //Om den prøver å gå rett frem, må målet være en åpen rute.
                ret.add(new Move(fraPos, tilPos));

                //Om den klarer å gå et skritt, og det er første gang den flytter, kan den prøve om det er lov å gå et skritt til.
                if((fraY == 6 && color == WHITE) || (fraY == 1 && color == BLACK) && bård.GetPiece(tilX, tilY+direc.getY()) == null){
                    ret.add(new Move(fraPos, new Tuple(tilX, tilY+direc.getY()))); //Om den får lov til å gå to skritt.
                }
            }
        }
        return ret;
    }
}
