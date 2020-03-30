package Chessbot3.bot.Simulate;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;
import Chessbot3.Pieces.WhiteBlack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static Chessbot3.Pieces.WhiteBlack.BLACK;
import static Chessbot3.Pieces.WhiteBlack.WHITE;

public class Tempbot {
    public static Move temporaryMoveFinder(Board bård){
        //Gir verdi til hvert enkelt Move, og sorterer dem.
        WhiteBlack myColor = bård.GetColorToMove();
        List<Move> legals = bård.GenMoves();
        for(Move move : legals){
            Board copy = bård.Copy();
            copy.MovePiece(move, false);
            move.addWeight(copy.GetScore());
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
