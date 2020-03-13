package Chessbot3.GameBoard;

import Chessbot3.Tuple;
import Pieces.PieceFactory;
import Pieces.WhiteBlack;
import Pieces.iPiece;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Pieces.WhiteBlack;
import Chessbot3.Move;

import static Pieces.WhiteBlack.BLACK;
import static Pieces.WhiteBlack.WHITE;

/**
 * Board
 */
public class Board implements IBoard {

    private static final char[][] initialBoard = new char[][]{
            new char[] {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
            new char[] {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
            new char[] {'.', '.', '.', '.', '.', '.', '.', '.'},
            new char[] {'.', '.', '.', '.', '.', '.', '.', '.'},
            new char[] {'.', '.', '.', '.', '.', '.', '.', '.'},
            new char[] {'.', '.', '.', '.', '.', '.', '.', '.'},
            new char[] {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
            new char[] {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}
    };

    iPiece[][] grid;

    iPiece[] wPices, bPieces;

    int wScore;
    int bScore;
    boolean isWhitesTurn = true;
    Tuple<Boolean, Boolean> wCastle;
    Tuple<Boolean, Boolean> bCastle;

    public Board() 
    {
        grid = new iPiece[8][8];
        for (int y = 0; y < 8; y++) 
        {
            for (int x = 0; x < 8; x++) 
            {
                iPiece pie = PieceFactory.getPiece(initialBoard[y][x], this);
                grid[x][y] = pie;
            }
        }
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
    public Move[] GenMoves(WhiteBlack c) 
    {
        List<Move> ret = new ArrayList<>();
        if (c == WHITE) 
        {
            for (iPiece pie : wPices) 
            {
                ret.addAll(pie.getMoves());
            }
        }
        else 
        {
            for (iPiece pie : bPieces) 
            {
                ret.addAll(pie.getMoves());
            }
        }
        return ret.toArray(new Move[ret.size()]);
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
                if(piece != null) {
                    if (isWhite)
                        if (piece.getColor() == WHITE)
                            returnList.add(piece);
                    if (!isWhite)
                        if (piece.getColor() == BLACK)
                            returnList.add(piece);
                }
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