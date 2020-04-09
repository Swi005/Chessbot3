package Chessbot3.Simulators;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Randbot {
    public static Move findMove(Board bård) throws IllegalStateException{
        List<Move> possibles = bård.GenCompletelyLegalMoves();
        if(possibles.size() == 0) throw new IllegalStateException();
        Collections.shuffle(possibles);

        try {
            Thread.sleep(15); //Sover litt, gir en illusjon om at den tenker hardt
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return possibles.get(0);
    }
}
