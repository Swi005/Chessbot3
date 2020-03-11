package Chessbot3.GameBoard;

import Chessbot3.Color;
import Chessbot3.Tuple;
import Pieces.iPiece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Chessbot3.Move;

import static Chessbot3.Color.WHITE;

/**
 * Board
 */
public class Board implements IBoard {

    iPiece[][] grid;

    iPiece[] wPieces, bPieces;

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
    public ArrayList<Move> GenMoves(Color c) {
        /* Lager en liste over nesten-lovlige trekk som en farge kan gjøre på dette brettet.
        // TODO: 11.03.2020 Fiks rokader! Det er her spillet må sjekke hvem som kan rokere, og hvor. 
         */
        ArrayList<Move> ret = new ArrayList<>();
        if(c == WHITE){
            for(iPiece pie : wPieces){
                ret.addAll(pie.getMoves());
            }
        }else{
            for(iPiece pie : bPieces) {
                ret.addAll(pie.getMoves());
            }
        }
        return ret;
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
    { // TODO: 11.03.2020 Tuplene med hvem som kan rokere hvor må også overføres til det nye brettet. 
        return new Board(this.GetGrid());
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