package Chessbot3.GameBoard;

import Pieces.*;
import Chessbot3.Tuple;

import java.util.ArrayList;
import java.util.Arrays;

import Chessbot3.Move;

import static Pieces.WhiteBlack.BLACK;
import static Pieces.WhiteBlack.WHITE;

/**
 * Board
 */
public class Board implements IBoard {

    private final char[][] initialBoard = new char[][]{
    new char[]{'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
    new char[]{'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
    new char[]{'.', '.', '.', '.', '.', '.', '.', '.'},
    new char[]{'.', '.', '.', '.', '.', '.', '.', '.'},
    new char[]{'.', '.', '.', '.', '.', '.', '.', '.'},
    new char[]{'.', '.', '.', '.', '.', '.', '.', '.'},
    new char[]{'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
    new char[]{'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}
};

    iPiece[][] grid;
    
    ArrayList<iPiece> wPieces;
    ArrayList<iPiece> bPieces;

    int wScore;
    int bScore;
    boolean isWhitesTurn = true;
    Tuple<Boolean, Boolean> wCastle;
    Tuple<Boolean, Boolean> bCastle;

    public Board() 
    {
        wPieces = new ArrayList<>();
        bPieces = new ArrayList<>();
        grid = new iPiece[8][8];
        for(int y=7; y>=0; y--){
            for(int x=0; x<8; x++){
                char bokstav = initialBoard[y][x]; //Ser på hvilken bokstav som er i de koordinatene, og
                iPiece pie = PieceFactory.getPiece(bokstav, this); //Spawner tilsvarende brikke i griddet.
                grid[x][y] = pie;
                if(pie != null) {
                    if (pie.getColor() == WHITE) wPieces.add(pie);
                    else bPieces.add(pie);
                }
            }
        }

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
        // TODO Check whether the GenMoves list is empty, and the king is in check.
        return false;
    }

    
    @Override
    public ArrayList<Move> GenMoves(WhiteBlack c) {
        /* Lager en liste over nesten-lovlige trekk som en farge kan gjøre på dette brettet.
        // TODO: 11.03.2020 Fiks rokader! Det er her spillet må sjekke hvem som kan rokere, og hvor. 
         */
        ArrayList<Move> ret = new ArrayList<>();
        if(c == WHITE){
            for(iPiece pie : wPieces){
                ret.addAll(pie.getMoves());
            }
        }else if(c == BLACK){
            for(iPiece pie : bPieces) {
                ret.addAll(pie.getMoves());
            }
        } else throw new IllegalArgumentException("Yo, please give me a color as an input!");
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