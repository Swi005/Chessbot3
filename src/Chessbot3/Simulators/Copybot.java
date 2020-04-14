package Chessbot3.Simulators;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Copybot {

    //En bot kopipastet nesten direkte fra forrige sjakkbotten, med små endringer.

    static int i = 0;
    static int plies = 4;
    static int alpha = -999999999;
    static int beta = 999999999;

    public static Move findMove(Board bård) throws IllegalArgumentException{
       List<Move> moves = bård.GenCompletelyLegalMoves();
       if(moves.size() == 0) throw new IllegalStateException("The game is over, I have no legal moves");

       for(int i=0; i<moves.size(); i++){
           try{
               int n = AlphaBeta(moves.get(i), plies, alpha, beta, false, bård);
               System.out.println(moves.get(i) + ": " + n);
               moves.get(i).addWeight(n);
           }catch(Exception e){
               e.getStackTrace();
               e.getMessage();
           }
       }
       Collections.sort(moves, Collections.reverseOrder());
        System.out.println("Antall fremtider : " + i);
        System.out.println(moves.get(0).getWeight());
        return moves.get(0);
    }

    private static int AlphaBeta(Move node, int plies, int alpha, int beta, boolean isMaximizingPlayer, Board bård) {
        i++;
        if(plies == 0) return bård.GetValue(node);

        int value;
        Board currPos = bård.Copy();
        Board tempPos;
        if(isMaximizingPlayer){
            value = -999999999;
            tempPos = currPos.Copy();
            tempPos.MovePiece(node);
            for(Move move : tempPos.GenMoves()){
                value = Math.max(value, AlphaBeta(move, plies-1, alpha, beta, false, tempPos));
            }
        }
        else{
            value = 999999999;
            tempPos = currPos.Copy();
            tempPos.MovePiece(node);
            for(Move move : tempPos.GenMoves()){
                value = Math.min(value, AlphaBeta(move, plies-1, alpha, beta, true, tempPos));
            }
        }
        return value;
    }
}
