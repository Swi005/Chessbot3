package Chessbot3.bot.Simulate;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Tempbot {
    public static Move findRandomMove(Board bård){
        //Tar et brett, og returnerer et helt tilfeldig, men lovlig, trekk.
        List<Move> ori = bård.GenMoves(bård.GetColorToMove());
        List<Move> legals = new ArrayList<>();
        for(Move move : ori){
            if(bård.CheckPlayerMove(move)) legals.add(move);
        }
        Collections.shuffle(legals);
        try
        {
            Thread.sleep(250); //Sover i et kvart sekund, for å gi en illusjon om at den tenker.
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
        return legals.get(0);
    }

}
