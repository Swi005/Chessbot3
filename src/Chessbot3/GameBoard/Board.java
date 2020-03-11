package Chessbot3.GameBoard;

import Chessbot3.Tuple;
import Chessbot3.iPiece;

import Chessbot3.Move;

/**
 * Board
 */
public class Board implements IBoard {

    iPiece[][] grid;

    int wScore;
    int bScore;
    boolean isWhitesTurn = true;
    Tuple<Boolean, Boolean> wCastle;
    Tuple<Boolean, Boolean> bCastle;

    public Board() 
    {
        grid = new iPiece[][] 
        { 
            new iPiece[] {}, 
            new iPiece[] {}, 
            new iPiece[] {}, 
            new iPiece[] {}, 
            new iPiece[] {},
            new iPiece[] {}, 
            new iPiece[] {}, 
            new iPiece[] {} 
        };
    }

    public Board(iPiece[][] customBoard) 
    {
        grid = customBoard;
    }

    @Override
    public iPiece GetPice(Tuple<Integer, Integer> pos) 
    {
        return grid[pos.getX()][pos.getY()];
    }

    @Override
    public void MovePiece(iPiece pice, Move move) 
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean IsMate() 
    {
        // TODO Check legal moves of king
        return false;
    }

    @Override
    public Move[] GenMoves(iPiece pice) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int GetScore(boolean isWhite) 
    {
        if (isWhite)
            return wScore;
        else
            return bScore;
    }

    public void Reverse()
    {
        for(int i = 0; i<grid.length/2; i++)
        {
            iPiece[] temp = grid[i];
            grid[i] = grid[grid.length - i - 1];
            grid[grid.length - i - 1] = temp;
        }
    }
}