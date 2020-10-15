package Chessbot3.Simulators;

import Chessbot3.GameBoard.Board;
import Chessbot3.GuiMain.Gui;
import Chessbot3.MiscResources.Move;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class MiniMaxBot implements iBot {

    private static int plies = 4;
    private static HashMap<Board, Integer> uniqueBoards = new HashMap<>();

    public Move findMove(Board bård) {
        List<Move> possibles = bård.genCompletelyLegalMoves();
        if (possibles.size() == 0) throw new IllegalStateException();

        if (bård.getColorToMove() == WHITE) {
            for (Move move : possibles) {
                if (Gui.game.stop) throw new IllegalStateException();   //Botten ble avbrutt

                Board copy = bård.copy();
                copy.movePiece(move);
                int value = minimax(copy, plies, false);
                move.setWeight(value);
            }
            Collections.sort(possibles, Collections.reverseOrder());
        } else {
            for (Move move : possibles) {
                if (Gui.game.stop) throw new IllegalStateException();   //Botten ble avbrutt

                Board copy = bård.copy();
                copy.movePiece(move);
                int value = minimax(copy, plies, true);
                move.setWeight(value);
            }
            Collections.sort(possibles);
        }
        uniqueBoards.clear();
        return possibles.get(0);
    }

    private static int minimax(Board bård, int depth, boolean isMaximizing) {
        if (depth == 0) return bård.getScore();

        Integer v = uniqueBoards.get(bård);
        if (v != null) return v;

        int value;
        if (isMaximizing) {
            value = -2147483648;
            for (Move move : bård.genMoves()) {
                Board copy = bård.copy();
                copy.movePiece(move);
                value = Math.max(value, minimax(copy, depth - 1, false));
            }
        } else {
            value = 2147483647;
            for (Move move : bård.genMoves()) {
                Board copy = bård.copy();
                copy.movePiece(move);
                value = Math.min(value, minimax(copy, depth - 1, true));
            }
        }
        uniqueBoards.put(bård, value);
        return value;
    }
}

