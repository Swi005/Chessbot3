package Chessbot3.Simulators;

import Chessbot3.GameBoard.Board;
import Chessbot3.GuiMain.Gui;
import Chessbot3.MiscResources.Move;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class AlphaBota {

    static int initialPlies = 4;
    static HashMap<Board, Integer> uniqueBoards = new HashMap<>();
    static int i = 0;

    public static Move findMove(Board bård){
        int nPieces = bård.getNumberOfPieces();
        if(nPieces < 5) return alphaSetup(bård, initialPlies +4);
        if(nPieces < 7) return alphaSetup(bård, initialPlies +3);
        if(nPieces < 10) return alphaSetup(bård, initialPlies +2);
        if(nPieces < 20) return alphaSetup(bård, initialPlies +1);
        return alphaSetup(bård, initialPlies);
        // TODO: 06.10.2020 Velg dybde basert på antall brikker som er igjen 
    }
    
    public static Move alphaSetup(Board bård, int depth){
        List<Move> possibles = bård.genCompletelyLegalMoves();
        if(possibles.size() == 0) throw new IllegalStateException();

        //Regner ut den umiddelbare verdien til hvert trekk, slik at alfabeta kan starte med en nesten sortert liste
        for(Move move : possibles) move.setWeight(bård.getValue(move));

        if(bård.getColorToMove() == WHITE){
            Collections.sort(possibles, Collections.reverseOrder());
            for(Move move : possibles){
                if(Gui.game.stop) throw new IllegalStateException();   //Botten ble avbrutt

                Board copy = bård.copy();
                copy.movePiece(move);
                int value = alphaBeta(copy, depth-1, -2147483648, 2147483647, false);
                move.setWeight(value);

                //System.out.println(move + ": " + value);
            }
            System.out.println();
            Collections.sort(possibles, Collections.reverseOrder());
        }
        else{
            Collections.sort(possibles);
            for(Move move : possibles){
                if(Gui.game.stop) throw new IllegalStateException();   //Botten ble avbrutt

                Board copy = bård.copy();
                copy.movePiece(move);
                int value = alphaBeta(copy, depth-1, -2147483648, 2147483647, true);
                move.setWeight(value);

                //System.out.println(move + ": " + value);
            }
            System.out.println();
            Collections.sort(possibles);
        }
        System.out.println("Unike brett etter " + depth + " trekk: " + uniqueBoards.size());
        System.out.println("Totalt antall brett etter "+ depth + " trekk: " + i);
        uniqueBoards.clear();
        i = 0;
        return possibles.get(0);
    }

    private static int alphaBeta(Board bård, int depth, int alpha, int beta, boolean isMaximizing){
        if(depth == 0){
            i++;
            return bård.getScore();
        }

        if(uniqueBoards.containsKey(bård)) return uniqueBoards.get(bård);

        int value;
        if (isMaximizing){
            value = -2147483648;
            for(Move move : bård.genMoves()) {
                Board copy = bård.copy();
                copy.movePiece(move);
                value = Math.max(value, alphaBeta(copy, depth - 1, alpha, beta, false));
                alpha = Math.max(alpha, value);
                if(alpha >= beta) break;
            }
        }
        else{
            value = 2147483647;
            for(Move move : bård.genMoves()) {
                Board copy = bård.copy();
                copy.movePiece(move);
                value = Math.min(value, alphaBeta(copy, depth-1, alpha, beta, true));
                beta = Math.min(beta, value);
                if(beta <= alpha) break;
            }
        }
        uniqueBoards.put(bård, value);
        return value;
    }
}
