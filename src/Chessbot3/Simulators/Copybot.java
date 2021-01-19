package Chessbot3.Simulators;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;

import java.util.List;

import static Chessbot3.Pieces.PieceResources.WhiteBlack.BLACK;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class Copybot implements iBot{

    //Steinar prøver å skrive en bot basert på det vi gjorde i chessbot2! Det går ikke bra.

    static int i = 0;
    static int plies = 4;
    static int alpha = -999999999;
    static int beta = 999999999;

    public Move findMove(Board bård) throws IllegalStateException {
        List<Move> possibles = bård.getLegalMoves();
        if (possibles.size() == 0) throw new IllegalStateException();
        System.out.println();
        Move bestMove = possibles.get(0);
        for(int i=0; i<possibles.size(); i++){
            Move move = possibles.get(i);

            int n=steinarsBizarreAlphaBeta(plies, bård, move);
            move.addWeight(n);

            move.addWeight(bård.getValue(move));

            System.out.println(move + ": " + move.getWeight());

            if(bård.getColorToMove() == WHITE && move.getWeight() > bestMove.getWeight()){
                bestMove = move;
            }
            else if(bård.getColorToMove() == BLACK && move.getWeight() < bestMove.getWeight()){
                bestMove = move;
            }
        }

        return bestMove;
    }

    private static int steinarsBizarreAlphaBeta(int plies, Board bård, Move node) {
        if (plies == 0) return bård.getValue(node);


        Board copy = bård.copy();

        copy.movePiece(node);
        List<Move> counters = copy.getMoves();
        int theWorstThatCanHappen = 0;

        if(bård.getColorToMove() == WHITE){
            for(int i=0; i<counters.size(); i++){
                int n=copy.getValue(counters.get(0)) + steinarsBizarreAlphaBeta(plies-1, copy, counters.get(i));
                if(n<theWorstThatCanHappen){
                    theWorstThatCanHappen = n;
                }
            }
        }
        else{
            for(int i=0; i<counters.size(); i++){
                int n=copy.getValue(counters.get(0)) + steinarsBizarreAlphaBeta(plies-1, copy, counters.get(i));
                if(n>theWorstThatCanHappen){
                    theWorstThatCanHappen = n;
                }
            }
        }
        return theWorstThatCanHappen;
    }



/*
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
    }*/
}
