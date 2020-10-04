package Chessbot3.Simulators;

import Chessbot3.GameBoard.Board;
import Chessbot3.GuiMain.Gui;
import Chessbot3.MiscResources.Move;

import java.util.Collections;
import java.util.List;

import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class MiniMaxBot {

    static int plies = 4;

    public static Move findMove(Board bård){
        List<Move> possibles = bård.genCompletelyLegalMoves();
        if(possibles.size() == 0) throw new IllegalStateException();

        if(bård.getColorToMove() == WHITE){
            for(Move move : possibles){
                if(Gui.game.stop) throw new IllegalStateException();   //Botten ble avbrutt

                Board copy = bård.copy();
                copy.movePiece(move);
                int value = alphaBeta(copy, plies, -2147483648, 2147483647, false);
                move.setWeight(value);

                System.out.println(move + ": " + value);
            }
            System.out.println();
            Collections.sort(possibles, Collections.reverseOrder());
        }
        else{
            for(Move move : possibles){
                if(Gui.game.stop) throw new IllegalStateException();   //Botten ble avbrutt

                Board copy = bård.copy();
                copy.movePiece(move);
                int value = alphaBeta(copy, plies, -2147483648, 2147483647, true);
                move.setWeight(value);

                System.out.println(move + ": " + value);
            }
            System.out.println();
            Collections.sort(possibles);
        }
        return possibles.get(0);
    }


    private static int minimax(Board bård, int depth, boolean isMaximizing){
        if(depth == 0) return bård.getScore();

        if(isMaximizing){
            int value = -2147483648;
            for(Move move : bård.genMoves()){
                Board copy = bård.copy();
                copy.movePiece(move);
                value = Math.max(value, minimax(copy, depth-1, false));
            }
            return value;
        }
        else{
            int value = 2147483647;
            for(Move move : bård.genMoves()){
                Board copy = bård.copy();
                copy.movePiece(move);
                value = Math.min(value, minimax(copy, depth-1, true));
            }
            return value;
        }
    }

    private static int alphaBeta(Board bård, int depth, int alpha, int beta, boolean isMaximizing){
        if(depth == 0) return bård.getScore();

        if (isMaximizing){
            int value = -2147483648;
            for(Move move : bård.genMoves()) {
                Board copy = bård.copy();
                copy.movePiece(move);
                value = Math.max(value, alphaBeta(copy, depth - 1, alpha, beta, false));
                alpha = Math.max(alpha, value);
                if(alpha >= beta) break;
            }
            return value;
        }
        else{
            int value = 2147483647;
            for(Move move : bård.genMoves()) {
                Board copy = bård.copy();
                copy.movePiece(move);
                value = Math.min(value, alphaBeta(copy, depth-1, alpha, beta, true));
                beta = Math.min(beta, value);
                if(beta <= alpha) break;
            }
            return value;
        }
    }

}
