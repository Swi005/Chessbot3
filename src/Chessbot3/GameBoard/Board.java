package Chessbot3.GameBoard;

import Chessbot3.Tuple;
import Pieces.iPiece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Chessbot3.Color;
import Chessbot3.Move;

/**
 * Board
 */
public class Board implements IBoard {

    iPiece[][] grid;

    iPiece[] wPices, bPieces;

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
        wPices = GeneratePieceArray(true);
        bPieces = GeneratePieceArray(false);
    }

    public Board(iPiece[][] customBoard, boolean isWhite) 
    {
        grid = customBoard;
        this.isWhitesTurn = isWhite;
        wPices = GeneratePieceArray(true);
        bPieces = GeneratePieceArray(false);
    }

    @Override
    public iPiece GetPiece(Tuple<Integer, Integer> pos) 
    {
        return grid[pos.getX()][pos.getY()];
    }

    public Tuple<Integer,Integer> GetCoordsOfPiece(iPiece piece)
    {
        for (int i = 0; i < grid.length; i++) 
        {
            iPiece[] subRow = grid[i];
            for (int j = 0; j < subRow.length; j++) 
            {
                if(subRow[j] == piece)
                    return new Tuple<Integer, Integer>(i, j);
            }
        }
        return null;
    }

    @Override
    public void MovePiece(Move move) 
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

    @Override
    public iPiece[][] GetGrid()
    {
        return Arrays.copyOf(this.grid, this.grid.length);
    }

    @Override
    public Board Copy() 
    {
        return new Board(this.GetGrid(),this.isWhitesTurn);
    }

    public iPiece[] GeneratePieceArray(boolean isWhite) 
    {
        List<iPiece> returnList = new ArrayList<>();
        for (iPiece[] row : grid) 
        {
            for (iPiece piece : row) {
                if (isWhite)
                    if (piece.getColor() == Color.WHITE)
                        returnList.add(piece);
                if (!isWhite)
                    if (piece.getColor() == Color.BLACK)
                        returnList.add(piece);
            }
        }
        return returnList.toArray(new iPiece[returnList.size()]);
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