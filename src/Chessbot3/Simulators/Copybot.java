package Chessbot3.Simulators;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class Copybot {

    //En bot kopipastet nesten direkte fra forrige sjakkbotten, med små endringer.

    static int i = 0;
    static int plies = 4;
    static int alpha = -999999999;
    static int beta = 999999999;

    public static Move findMove(Board bård) throws IllegalStateException{
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
                alpha = Math.max(value, alpha);
            }
        }
        else{
            value = 999999999;
            tempPos = currPos.Copy();
            tempPos.MovePiece(node);
            for(Move move : tempPos.GenMoves()){
                value = Math.min(value, AlphaBeta(move, plies-1, alpha, beta, true, tempPos));
                beta = Math.min(value, beta);
            }
        }
        return value;
    }

    public static Move findOkMove(Board bård) throws IllegalStateException{
        List<Move> possibles = bård.GenCompletelyLegalMoves();
        if(possibles.size() == 0) throw new IllegalStateException();

        boolean isMaximizingPlayer;
        if(bård.GetColorToMove() == WHITE) isMaximizingPlayer = false;
        else isMaximizingPlayer = true;

        for(int i=0; i<possibles.size(); i++){
            int initialWeight = bård.GetValue(possibles.get(i));
            int futureWeight = alphaBeta2(possibles.get(i), plies-1, isMaximizingPlayer, bård);

            possibles.get(i).addWeight(initialWeight + futureWeight);
        }


        if(isMaximizingPlayer) Collections.sort(possibles);
        else Collections.sort(possibles, Collections.reverseOrder());
        return possibles.get(0);
    }

    private static int alphaBeta2(Move move, int plies, boolean isMaximizingPlayer, Board bård) {

        if(isMaximizingPlayer){

            Board copy = bård.Copy();
            copy.MovePiece(move);

        }


        return 0;
    }

    public static Move loooooop(Board bård){
        List<Move> possibles = bård.GenCompletelyLegalMoves();
        if(possibles.size() == 0) throw new IllegalStateException();

        for(Move move : possibles){
            Board copy = bård.Copy();
            copy.MovePiece(move);
            move.addWeight(copy.GetScore());

            int best = 999999999;
            for(Move counter : copy.GenMoves()){
                Board countercopy = copy.Copy();
                countercopy.MovePiece(counter);



                best = Math.min(best, countercopy.GetScore());
            }
            move.addWeight(best);
        }
        Collections.sort(possibles);
        return possibles.get(0);
    }
}
