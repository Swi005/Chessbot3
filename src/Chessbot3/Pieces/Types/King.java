package Chessbot3.Pieces.Types;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.PieceResources.SimplePiece;
import Chessbot3.Pieces.PieceResources.WhiteBlack;

import java.util.ArrayList;

import static Chessbot3.Pieces.PieceResources.WhiteBlack.BLACK;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class King extends SimplePiece {
    public King(WhiteBlack c) {
        super(c);
        canSprint = false;
        inherentValue = 69420; //Nice.
        symbol = 'K';
        if (c == WHITE) imageKey = "Kw";
        else imageKey = "Kb";
    }
    @Override
    public ArrayList<Move> getMoves(Board bård, Tuple<Integer, Integer> pos){
        //En konges lovlige trekk kan regnes ut på samme måte som i supermetoden, men i tillegg skal den ha opptil 2 lovlige rokadetrekk.
        //Disse sjekkes og legges til av getCastleMoves.
        //Denne metoden tar ikke hensyn til at trekket eventuellt setter kongen i sjakk.
        ArrayList<Move> ret = super.getMoves(bård, pos);
        ret.addAll(getCastleMoves(bård));
        return ret;
    }

    private ArrayList<Move> getCastleMoves(Board bård){
        //En funksjon kun for å legge til rokader som lovlige trekk.
        //En rokade er lov om:
        // 1: Kongen ikke har flyttet seg i det hele tatt ennå
        // 2: Tårnet på den valgte siden ikke har flyttet seg ennå (og lever fortsatt)
        // 3: Alle rutene imellom kongen og det valgte tårnet er åpne
        // 4: Ingen av rutene fra og med kongen til og med der kongen vil flytte blir truet av noen fiendtlig brikke.
        // Nummer 4 ignorerer vi, fordi det ville mangedoblet kompleksiteten.
        ArrayList<Move> ret = new ArrayList<>();
        boolean[] castle = bård.getCastle();
        if(color == WHITE){
            if(castle[1] && bård.getPiece(5, 7) ==  null && bård.getPiece(6, 7) == null){
                ret.add(new Move(new Tuple(4, 7), new Tuple(6, 7))); //Hvit rokerer kort
            }
            if(castle[0] && bård.getPiece(3, 7) ==  null && bård.getPiece(2, 7) == null && bård.getPiece(1, 7) ==  null){
                ret.add(new Move(new Tuple(4, 7), new Tuple(2, 7))); //Hvit rokerer langt
            }
        }else if(color == BLACK){
            if(castle[3] && bård.getPiece(5, 0) ==  null && bård.getPiece(6, 0) == null){
                ret.add(new Move(new Tuple(4, 0), new Tuple(6, 0))); //Svart rokerer kort
            }
            if(castle[2] && bård.getPiece(3, 0) == null && bård.getPiece(2, 0) ==  null && bård.getPiece(1, 0) ==  null){
                ret.add(new Move(new Tuple(4, 0), new Tuple(2, 0))); //Svart rokerer langt
            }
        }
        return ret;
    }
}
