package Chessbot3.bot.Simulate;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tempbot {
    public static Move findRandomMove(Board bård){
        //Tar et brett, og returnerer et helt tilfeldig, men lovlig, trekk.
        List<Move> legals = bård.GenCompletelyLegalMoves();
        Collections.shuffle(legals);

        try
        {
            Thread.sleep(0); //Tar seg en høneblund, for å skape en illusjon om at den tenker.
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }

        return legals.get(0);
    }

}
