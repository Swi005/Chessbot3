package Chessbot3.GameBoard;

import static Pieces.WhiteBlack.BLACK;
import static Pieces.WhiteBlack.WHITE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Chessbot3.Move;
import Chessbot3.Tuple;
import Pieces.King;
import Pieces.PieceFactory;
import Pieces.WhiteBlack;
import Pieces.iPiece;

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

    private int wScore;
    private int bScore;
    private boolean isWhitesTurn = true;
    boolean isMate;
    private Tuple<Boolean, Boolean> wCastle;
    private Tuple<Boolean, Boolean> bCastle;

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
        this.grid = customBoard;
        this.isWhitesTurn = isWhite;
        this.wPices = GeneratePieceArray(true);
        this.bPieces = GeneratePieceArray(false);
    }

    @Override
    public iPiece GetPiece(Tuple<Integer, Integer> pos) 
    {
        return grid[pos.getY()][pos.getX()];
    }

    public Tuple<Integer,Integer> GetCoordsOfPiece(iPiece piece)
    {
        for (int i = 0; i < grid.length; i++) {
            iPiece[] subRow = grid[i];
            for (int j = 0; j < subRow.length; j++) {
                if (subRow[j] == piece)
                    return new Tuple<Integer, Integer>(i, j);
            }
        }
        return null;
    }

    public int AddScore(iPiece piece) 
    {
        if(piece.getColor() == WHITE)
            bScore += piece.getValue();
        else
            wScore += piece.getValue();
        return piece.getValue();
    }

    @Override
    public ArrayList<Tuple> MovePiece(Move move)
    {
        ArrayList<Tuple> ret = new ArrayList<>();

        Tuple<Integer, Integer> fra = move.getX();
        Tuple<Integer, Integer> til = move.getY();
        ret.add(fra);
        ret.add(til);

        grid[til.getX()][til.getY()] = grid[fra.getX()][fra.getY()];
        grid[fra.getX()][fra.getY()] = null;
        // TODO: 14.03.2020 En passant, og rokadelogikk

        isWhitesTurn = !isWhitesTurn;

        return ret; //Returnerer en liste over lokasjoner som ble endret på, så de kan bli tegnet på nytt.
    }

    @Override
    public boolean IsMate() 
    {
        // TODO Check legal moves of king
        return isMate;
    }

    public Boolean checkPlayerMove(Move playerMove){
        // TODO: 14.03.2020 Sjekk at denne faktisk funker, når vi har fikset bugsene rundt copy(). 
        Move[] availableMoves;
        if(isWhitesTurn) availableMoves = GenMoves(WHITE);
        else availableMoves = GenMoves(BLACK);
        Boolean ret = false;
        for(Move move : availableMoves) if(move.equals(playerMove)) ret = true;
        if(ret){
            Board copy = this.Copy();
            copy.MovePiece(playerMove);
            Move[] counterMoves;
            if(isWhitesTurn) counterMoves = GenMoves(BLACK);
            else counterMoves = GenMoves(WHITE);
            for(Move counter : counterMoves){
                iPiece target = copy.GetGrid()[counter.getY().getX()][counter.getY().getY()];
                if(target instanceof King) return false;
            }
            return true;
        } else return false;
    }

    @Override
    public Move[] GenMoves(WhiteBlack c)
    {
        List<Move> ret = new ArrayList<>();
        if (c == WHITE) 
        {
            for (iPiece pie : wPices) 
            {
                // TODO: 14.03.2020 Erstatt alt dette. Bare fordi kongen ikke kan flytte betyr ikke det at det er matt.
                if(pie instanceof King && pie.getMoves().size() ==0)
                {
                    isMate= true;
                }
                else
                    isMate= false;
                    
                ret.addAll(pie.getMoves());
            }
        }
        else 
        {
            for (iPiece pie : bPieces) 
            {
                if(pie instanceof King && pie.getMoves().size() ==0)
                {
                    isMate= true;
                }
                else
                    isMate = false;
                    
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
    public String toString(){
        String ret = "";
        for(int x=0; x<8; x++){
            String rekke = "";
            for(int y=0; y<8; y++){
                iPiece pie = GetPiece(new Tuple<>(x, y));
                if(pie == null) rekke += ".";
                else if(pie.isWhite()) rekke += pie.getSymbol();
                else rekke += Character.toLowerCase(pie.getSymbol());
                rekke += "";
            }
            ret += rekke + "\n";
        }
        ret += "White: " + wScore + " Black: " + bScore;
        return ret;
    }
}