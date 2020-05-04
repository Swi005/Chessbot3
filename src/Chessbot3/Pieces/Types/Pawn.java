package Chessbot3.Pieces.Types;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.PieceResources.SimplePiece;
import Chessbot3.Pieces.PieceResources.WhiteBlack;
import Chessbot3.Pieces.PieceResources.iPiece;

import java.util.ArrayList;

import static Chessbot3.GuiMain.Chess.*;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.BLACK;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class Pawn extends SimplePiece {

    public Pawn(WhiteBlack c) {
        super(c);
        canSprint = false;
        inherentValue = 100;
        symbol = 'P';
        if(c == WHITE) imageKey = "Pw";
        else imageKey = "Pb";
    }
    @Override
    public ArrayList<Move> getMoves(Board bård){
        Tuple<Integer, Integer> passantPos = bård.GetPassantPos();
        ArrayList<Move> ret = new ArrayList<>();
        Tuple[] directions;
        if(color == WHITE) directions = direcDict.get('P');
        else directions = direcDict.get('p');

        Integer fraX = getX(bård);
        Integer fraY = getY(bård);
        Tuple<Integer, Integer> fraPos = new Tuple<>(fraX, fraY);

        for(Tuple<Integer, Integer> direc : directions){ //Looper igjennom alle retningene den kan gå.
            Integer tilX = fraX+direc.getX();
            Integer tilY = fraY+direc.getY();
            Tuple<Integer, Integer> tilPos = new Tuple(tilX, tilY);

            if(tilX < 0 || tilY < 0 || tilX > 7 || tilY > 7) continue; //Om den holder på å gå utenfor brettet.
            iPiece mål = bård.GetPiece(tilPos);

            if(direc.getX() == -1 || direc.getX() == 1){ //Om den prøver å gå skrått. Da trenger den at det står en fiendtlig brikke der, eller at den kan ta en passant.
                if (mål != null && this.isOppositeColor(mål) || tilPos.equals(passantPos)) {
                    ret.add(new Move(fraPos, tilPos));
                }
            }
            if(direc.getX() == 0 && mål == null){ //Om den prøver å gå rett frem, må målet være en åpen rute.
                ret.add(new Move(fraPos, tilPos));

                //Om den klarer å gå et skritt, og det er første gang den flytter, kan den prøve om det er lov å gå et skritt til.
                if(((fraY == 6 && color == WHITE) || (fraY == 1 && color == BLACK)) && bård.GetPiece(tilX, tilY+direc.getY()) ==  null){
                    ret.add(new Move(fraPos, new Tuple(tilX, tilY + direc.getY()))); //Om det er en åpen rute, kan den gå to skritt.
                }
            }
        }
        return ret;
    }
}
