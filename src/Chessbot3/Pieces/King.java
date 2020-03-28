package Chessbot3.Pieces;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;

import java.util.ArrayList;
import java.util.List;

import static Chessbot3.GuiMain.Chess.imageDict;
import static Chessbot3.Pieces.WhiteBlack.BLACK;
import static Chessbot3.Pieces.WhiteBlack.WHITE;

public class King extends SimplePiece {
    public King(WhiteBlack c) {
        super(c);
        posValue = new int[][] 
        {
            new int[] {4,  54,  47, -99, -99,  60,  83, -62},
            new int[] {-32,  10,  55,  56,  56,  55,  10,   3},
            new int[] {-62,  12, -57,  44, -67,  28,  37, -31},
            new int[] {-55,  50,  11,  -4, -19,  13,   0, -49},
            new int[] {-55, -43, -52, -28, -51, -47,  -8, -50},
            new int[] {-47, -42, -43, -79, -64, -32, -29, -32},
            new int[] {-4,   3, -14, -50, -57, -18,  13,   4},
            new int[] {17,  30,  -3, -14,   6,  -1,  40,  18}
        };
        canSprint = false;
        value = 69420; //Nice.
        symbol = 'K';
        if (c == WHITE) {
            image = imageDict.get("king_white");
        } else
            image = imageDict.get("king_black");
    }
    @Override
    public ArrayList<Move> getMoves(Board bård){
        //En konges lovlige trekk kan regnes ut på samme måte som i supermetoden, men i tillegg skal den ha opptil 2 lovlige rokadetrekk.
        //Disse sjekkes og legges til av getCastleMoves.
        ArrayList<Move> ret = super.getMoves(bård);
        ret.addAll(getCastleMoves(bård));
        return ret;
    }

    private List<Move> getCastleMoves(Board bård){
        //En funksjon kun for å legge til rokader som lovlige trekk.
        //En rokade er lov om:
        // 1: Kongen ikke har flyttet seg i det hele tatt ennå
        // 2: Tårnet på den valgte siden ikke har flyttet seg ennå (og lever fortsatt)
        // 3: Alle rutene imellom kongen og det valgte tårnet er åpne
        // 4: Ingen av rutene fra og med kongen til og med der kongen vil flytte blir truet av noen fiendtlig brikke.
        // Nummer 4 ignorerer vi, fordi det vil doble kompleksiteten til GenMoves.
        List<Move> ret = new ArrayList<>();
        if(color == WHITE){
            if(bård.wCastle.getY() && bård.GetPiece(5, 7) ==  null && bård.GetPiece(6, 7) == null){
                ret.add(new Move(new Tuple(4, 7), new Tuple(6, 7))); //Hvit rokerer kort
            }
            if(bård.wCastle.getX() && bård.GetPiece(3, 7) ==  null && bård.GetPiece(2, 7) == null && bård.GetPiece(1, 7) ==  null){
                ret.add(new Move(new Tuple(4, 7), new Tuple(2, 7))); //Hvit rokerer langt
            }
        }else if(color == BLACK){
            if(bård.bCastle.getY() && bård.GetPiece(5, 0) ==  null && bård.GetPiece(6, 0) == null){
                ret.add(new Move(new Tuple(4, 0), new Tuple(6, 0))); //Svart rokerer kort
            }
            if(bård.bCastle.getX() && bård.GetPiece(3, 0) == null && bård.GetPiece(2, 0) ==  null && bård.GetPiece(1, 0) ==  null){
                ret.add(new Move(new Tuple(4, 0), new Tuple(2, 0))); //Svart rokerer langt
            }
        }
        return ret;
    }
}
