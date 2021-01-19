package Chessbot3.Simulators;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;
import Chessbot3.Pieces.PieceResources.WhiteBlack;

import java.util.Collections;
import java.util.List;

import static Chessbot3.Pieces.PieceResources.WhiteBlack.BLACK;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class Tempbot implements iBot{
    public Move findMove(Board bård) throws IllegalStateException{
        //Gir verdi til hvert enkelt Move, og sorterer dem.
        WhiteBlack myColor = bård.getColorToMove();
        List<Move> legals = bård.getLegalMoves(); //bruker den kompliserte metoden første gangen, og standard GenMoves alle de andre gangene.
        if(legals.size() == 0) throw new IllegalStateException();
        for(Move move : legals){
            Board copy = bård.copy();
            copy.movePiece(move);
            move.addWeight(copy.getScore());

            List<Move> countermoves = copy.getMoves();
            for(Move counter : countermoves){
                counter.addWeight(copy.getValue(counter));
            }
            if(myColor == WHITE) Collections.sort(countermoves);
            else Collections.sort(countermoves, Collections.reverseOrder());
            move.addWeight(countermoves.get(0).getWeight());
        }
        if(myColor == BLACK) Collections.sort(legals);
        else Collections.sort(legals, Collections.reverseOrder());

        try {
            Thread.sleep(0); //Tar seg en høneblund, for å skape en illusjon om at den tenker.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        return legals.get(0);
    }
}
