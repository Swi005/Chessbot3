package Chessbot3.Simulators;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;

import java.util.Collections;
import java.util.List;

public class AlphaBota {

    static int plies = 1 ;
    static int alpha = -999999999;
    static int beta = 999999999;

    public static Move findMove(Board bård) throws IllegalStateException{

        //Lager den initielle listen, og sorterer med hensyn på umiddelbar verdi.
        List<Move> possibles = bård.genCompletelyLegalMoves();
        if(possibles.size() == 0) throw new IllegalStateException();
        for(Move move : possibles) move.addWeight(bård.getValue(move));
        Collections.sort(possibles, Collections.reverseOrder());

        for(Move move : possibles)
        {
            move.setWeight(0);
            Board copy = bård.copy();
            copy.movePiece(move);
            int n = alphaBeta(copy, plies, alpha, beta, true);
            move.addWeight(n);
            System.out.println(move + ": " + n);
        }
        Collections.sort(possibles, Collections.reverseOrder());

        return possibles.get(0);
    }

    private static int alphaBeta(Board bård, int plies, int alpha, int beta, boolean isMaximizingPlayer) {
        if(plies == 0) return bård.getScore();

        if(isMaximizingPlayer){
            int value = -999999999;
            for(Move counter : bård.genMoves()){
                Board copy = bård.copy();
                copy.movePiece(counter);

                value = Math.max(value, alphaBeta(copy, plies-1, alpha, beta, false));
                alpha = Math.max(alpha, value);

                if(alpha >= beta) break;
            }
            return value;
        }
        else{
            int value = 999999999;
            for(Move counter : bård.genMoves()){
                Board copy = bård.copy();
                copy.movePiece(counter);

                value = Math.min(value, alphaBeta(copy, plies-1, alpha, beta, true));
                beta = Math.min(beta, value);

                if(alpha >= beta) break;
            }
            return value;
        }
    }
}
