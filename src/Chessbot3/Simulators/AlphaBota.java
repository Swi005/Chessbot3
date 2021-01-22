package Chessbot3.Simulators;

import Chessbot3.GameBoard.Board;
import Chessbot3.GuiMain.Gui;
import Chessbot3.MiscResources.Move;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class AlphaBota implements iBot{

    static int initialPlies = 5;
    static HashMap<Board, Integer> uniqueBoards = new HashMap<>();
    static HashMap<Board, int[]> uniques = new HashMap<>();
    static HashMap<Board, Transposition> transpositions = new HashMap<>();
    static int i = 0;

    public Move findMove(Board bård) throws IllegalStateException{
        Board copy = bård.copy();
        // TODO: 18.10.2020 Finn en bedre måte å skalere botten på når spillet forenkles
        //int nMoves = copy.genCompletelyLegalMoves().size();
        //if(nMoves > 25) return setupSearch(copy, initialPlies);
        //if(nMoves > 18) return setupSearch(copy, initialPlies+1);
        //if(nMoves > 12) return setupSearch(copy, initialPlies+2);
        //if(nMoves > 8) return setupSearch(copy,initialPlies+3);
        //if(nMoves > 4) return setupSearch(copy, initialPlies+4);
        return setupSearch(copy, initialPlies);
    }
    
    public static Move setupSearch(Board bård, int depth) throws IllegalStateException{
        List<Move> possibles = bård.getLegalMoves();
        if(possibles.size() == 0) throw new IllegalStateException();

        //Regner ut den umiddelbare verdien til hvert trekk, slik at alfabeta kan starte med en nesten sortert liste
        for(Move move : possibles) move.setWeight(bård.getValue(move));

        if(bård.getColorToMove() == WHITE){
            Collections.sort(possibles, Collections.reverseOrder());
            for(Move move : possibles){
                if(Gui.game.stop) throw new IllegalStateException();   //Botten ble avbrutt

                //Board copy = bård.copy();
                bård.movePiece(move);
                int value = alphaBeta(bård, depth-1, -2147483648, 2147483647, false);
                move.setWeight(value);
                bård.goBack();
            }
            Collections.sort(possibles, Collections.reverseOrder());
        }
        else{
            Collections.sort(possibles);
            for(Move move : possibles){
                if(Gui.game.stop) throw new IllegalStateException();   //Botten ble avbrutt

                //Board copy = bård.copy();
                bård.movePiece(move);
                int value = alphaBeta(bård, depth-1, -2147483648, 2147483647, true);
                move.setWeight(value);
                bård.goBack();
            }
            Collections.sort(possibles);
        }
        //System.out.println("Unike brett etter " + depth + " trekk: " + uniqueBoards.size());
        //System.out.println("Totalt antall brett etter "+ depth + " trekk: " + i);
        //uniqueBoards.clear();
        //uniques.clear();
        //transpositions.clear();
        //i = 0;
        return possibles.get(0);
    }

    private static int alphaBeta(Board bård, int depth, int alpha, int beta, boolean isMaximizing){
        if(depth == 0){
            //i++;
            //transpositions.put(bård, new Transposition(bård.getScore(), EXACT, null, 0));
            return bård.getScore();
        }
        /*if(transpositions.containsKey(bård)){
            Transposition trans = transpositions.get(bård);
            if(trans.depth >= depth){
                if(trans.flag == UPPER_BOUND) beta = Math.min(beta, trans.value);
                else if(trans.flag == LOWER_BOUND) alpha = Math.max(alpha, trans.value);
                else return trans.value;
            }
        }*/

        int value;
        if (isMaximizing){
            value = -2147483648;
            for(Move move : bård.getMoves()) {

                //Board copy = bård.copy();
                bård.movePiece(move);
                value = Math.max(value, alphaBeta(bård, depth - 1, alpha, beta, false));
                alpha = Math.max(alpha, value);
                if (alpha >= beta) {
                    //transpositions.put(bård, new Transposition(value, LOWER_BOUND, move, depth));
                    bård.goBack();
                    break;
                    //return value;
                }
                bård.goBack();
            }
        }
        else{
            value = 2147483647;
            for(Move move : bård.getMoves()) {

                //Board copy = bård.copy();
                bård.movePiece(move);
                value = Math.min(value, alphaBeta(bård, depth - 1, alpha, beta, true));
                beta = Math.min(beta, value);
                if (beta <= alpha) {
                    //transpositions.put(bård, new Transposition(value, UPPER_BOUND, move, depth));
                    bård.goBack();
                    break;
                    //return value;
                }
                bård.goBack();
            }
        }

        //transpositions.put(bård, new Transposition(value, EXACT, null, depth));
        return value;
    }

    private static class Transposition{
        TransFlag flag;
        int value;
        Move bestMove;
        int depth;
        public Transposition(int value, TransFlag flag, Move bestMove, int depth){
            this.bestMove = bestMove;
            this.flag = flag;
            this.value = value;
            this.depth = depth;
        }
    }
}
