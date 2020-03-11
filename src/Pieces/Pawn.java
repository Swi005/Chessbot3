package Pieces;

import Chessbot3.Color;
import Chessbot3.GameBoard.Board;
import Chessbot3.Move;
import Chessbot3.Tuple;

import java.util.ArrayList;

import static Chessbot3.Chess.imageDict;
import static Chessbot3.Chess.moveDict;
import static Chessbot3.Color.BLACK;
import static Chessbot3.Color.WHITE;
import static Chessbot3.Generator.*;

public class Pawn extends SimplePiece {

    public Pawn(Color c, Board gameboard) {
        super(c, gameboard);
        canSprint = false;
        value = 100;
        symbol = 'P';
        if(c == WHITE){
            image = imageDict.get("pawn_white");
        }else image = imageDict.get("pawn_black");
    }
    @Override
    public ArrayList<Move> getMoves(){
        ArrayList<Move> ret = new ArrayList<>();
        Tuple[] directions = moveDict.get('P');
        if(color == BLACK){
            for(Tuple<Integer, Integer> direc : directions){
                direc.setY(direc.getY()*-1); //Om det er en svart bonde, vil dette invertere alle retningene.
            }
        }
        Integer fraX = getX();
        Integer fraY = getY();
        for(Tuple<Integer, Integer> direc : directions){ //Looper igjennom alle retningene den kan gå.
            Integer tilX = fraX+direc.getX();
            Integer tilY = fraY+direc.getY();
            if(tilX < 0 || tilY < 0 || tilX > 7 || tilY > 7) break; //Om den holder på å gå utenfor brettet.
            iPiece mål = gameboard.GetGrid()[tilX][tilY];
            if(direc.getX() == -1 || direc.getX() == 1){ //Om den prøver å gå skrått. Da trenger den at det står en fiendtlig brikke der.
                if(mål != null && this.isOppositeColor(mål)) ret.add(new Move(new Tuple(fraX, fraY), new Tuple(tilX, tilY)));
            }
            if(direc.getX() == 0 && mål == null){ //Om den prøver å gå rett frem, må målet være en åpen rute.
                ret.add(new Move(new Tuple(fraX, fraY), new Tuple(tilX, tilY)));
                //Om den klarer å gå et skritt, og det er første gang den flytter, kan den prøve om det er lov å gå et skritt til.
                if((fraY == 1 && color == WHITE) || (fraY == 6 && color == BLACK) && gameboard.GetGrid()[tilX][tilY+direc.getY()] == null){
                    ret.add(new Move(new Tuple(fraX, fraY), new Tuple(tilX, tilY+direc.getY()))); //Om den får lov til å gå to skritt.
                }
            }
        }
        return ret;
    }



    // TODO: 11.03.2020 Override getMoves(), siden bondelogikk fungerer helt annerledes enn resten av brikkene. 
}
