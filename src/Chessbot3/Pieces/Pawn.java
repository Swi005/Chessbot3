package Chessbot3.Pieces;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;

import java.util.ArrayList;

import static Chessbot3.GUIMain.Chess.*;
import static Chessbot3.Pieces.WhiteBlack.BLACK;
import static Chessbot3.Pieces.WhiteBlack.WHITE;

public class Pawn extends SimplePiece {

    public Pawn(WhiteBlack c) {
        super(c);
        canSprint = false;
        value = 100;
        symbol = 'P';
        if(c == WHITE){
            image = imageDict.get("pawn_white");
        }else image = imageDict.get("pawn_black");
    }
    @Override
    public ArrayList<Move> getMoves(Board bård){
        ArrayList<Move> ret = new ArrayList<>();
        Tuple[] directions;
        if(color == WHITE){
            directions = direcDict.get('P');
        } else directions = direcDict.get('p');

        Integer fraX = getX(bård);
        Integer fraY = getY(bård);
        for(Tuple<Integer, Integer> direc : directions){ //Looper igjennom alle retningene den kan gå.
            Integer tilX = fraX+direc.getX();
            Integer tilY = fraY+direc.getY();
            if(tilX < 0 || tilY < 0 || tilX > 7 || tilY > 7) break; //Om den holder på å gå utenfor brettet.
            iPiece mål = bård.GetGrid()[tilX][tilY];

            if(direc.getX() == -1 || direc.getX() == 1){ //Om den prøver å gå skrått. Da trenger den at det står en fiendtlig brikke der.
                if(mål != null && this.isOppositeColor(mål)) ret.add(new Move(new Tuple(fraX, fraY), new Tuple(tilX, tilY)));
            }
            if(direc.getX() == 0 && mål == null){ //Om den prøver å gå rett frem, må målet være en åpen rute.
                ret.add(new Move(new Tuple(fraX, fraY), new Tuple(tilX, tilY)));
                //Om den klarer å gå et skritt, og det er første gang den flytter, kan den prøve om det er lov å gå et skritt til.
                if((fraY == 6 && color == WHITE) || (fraY == 1 && color == BLACK) && bård.GetGrid()[tilX][tilY+direc.getY()] == null){
                    ret.add(new Move(new Tuple(fraX, fraY), new Tuple(tilX, tilY+direc.getY()))); //Om den får lov til å gå to skritt.
                }
            }
        }
        return ret;
    }
}
