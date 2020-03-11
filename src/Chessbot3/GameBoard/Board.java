package Chessbot3.GameBoard;

import Chessbot3.Tuple;
import Chessbot3.Move;

/**
 * Board
 */
public class Board implements IBoard {

    IPice[][] grid;

    int wScore;
    int bScore;
    Tuple<Boolean,Boolean> wCastle;
    Tuple<Boolean, Boolean> bCastle;

    @Override
    public IPice GetPice(Move move) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void MovePiece(IPice pice, Move move) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean IsMate() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Move[] GenMoves(IPice pice) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int GetScore(boolean isWhite) {
        // TODO Auto-generated method stub
        return 0;
    }

    
}