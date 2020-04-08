package Chessbot3.bot.Simulate;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Randbot {
    public static Move findMove(Board bård){
        List<Move> possibles = bård.GenCompletelyLegalMoves();
        Collections.shuffle(possibles);

        try {
            Thread.sleep(100); //Sover litt, gir en illusjon om at den tenker hardt
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return possibles.get(0);
    }
}
