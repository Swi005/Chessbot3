package Chessbot3.bot.Simulate;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;
import Chessbot3.Pieces.PieceResources.WhiteBlack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static Chessbot3.Pieces.PieceResources.WhiteBlack.BLACK;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class Tempbot {
    public static Move temporaryMoveFinder(Board bård){
        //Gir verdi til hvert enkelt Move, og sorterer dem.
        WhiteBlack myColor = bård.GetColorToMove();
        List<Move> legals = bård.GenCompletelyLegalMoves(); //bruker den kompliserte metoden første gangen, og standard GenMoves alle de andre gangene.
        for(Move move : legals){
            Board copy = bård.Copy();
            copy.MovePiece(move, false);
            move.addWeight(copy.GetScore());

            List<Move> countermoves = copy.GenMoves();
            for(Move counter : countermoves){
                counter.addWeight(copy.GetValue(counter));
            }
            if(myColor == WHITE) Collections.sort(countermoves);
            else Collections.sort(countermoves, Collections.reverseOrder());
            move.addWeight(countermoves.get(0).getWeight());
        }
        if(myColor == BLACK) Collections.sort(legals);
        else Collections.sort(legals, Collections.reverseOrder());

        try {
            Thread.sleep(15); //Tar seg en høneblund, for å skape en illusjon om at den tenker.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        return legals.get(0);
    }
}
