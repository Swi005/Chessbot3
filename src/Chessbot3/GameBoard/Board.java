package Chessbot3.GameBoard;

import static Pieces.WhiteBlack.BLACK;
import static Pieces.WhiteBlack.WHITE;

import java.util.ArrayList;
import java.util.List;

import Chessbot3.Move;
import Chessbot3.Tuple;
import Pieces.PieceFactory;
import Pieces.WhiteBlack;
import Pieces.iPiece;

/**
 * Board
 */
public class Board implements IBoard {

    private static final char[][] initialBoard = new char[][]{
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

    private int wScore;
    private int bScore;
    private boolean isWhitesTurn = true;
    private Tuple<Boolean, Boolean> wCastle;
    private Tuple<Boolean, Boolean> bCastle;

    public Board() {
        grid = new iPiece[8][8];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                iPiece pie = PieceFactory.getPiece(initialBoard[y][x]);
                grid[x][y] = pie;
            }
        }
        wCastle = new Tuple<>(true, true);
        bCastle = new Tuple<>(true, true);
        wScore = 0;
        bScore = 0;

    }

    public Board(iPiece[][] customBoard, boolean isWhite, int wScore, int bScore, Tuple<Boolean, Boolean> wCastle, Tuple<Boolean, Boolean> bCastle) {
        this.grid = customBoard;
        this.isWhitesTurn = isWhite;
        this.wCastle = wCastle;
        this.bCastle = bCastle;
        this.wScore = wScore;
        this.bScore = bScore;
    }

    public List<Move> GenMoves(WhiteBlack c){
        List<iPiece> list = GetPieceList(c);
        ArrayList<Move> ret = new ArrayList();
        for(iPiece pie : list){
            ret.addAll(pie.getMoves(this));
        }
        return ret;
    }

    public Tuple<Integer, Integer> GetCoordsOfPiece(iPiece piece) {
        for (int i = 0; i < grid.length; i++) {
            iPiece[] subRow = grid[i];
            for (int j = 0; j < subRow.length; j++) {
                if (subRow[j] == piece)
                    return new Tuple<Integer, Integer>(i, j);
            }
        }
        return null;
    }

    public int AddScore(iPiece piece) {
        if (piece.getColor() == WHITE)
            bScore += piece.getValue();
        else
            wScore += piece.getValue();
        return piece.getValue();
    }

    @Override
    public ArrayList<Tuple> MovePiece(Move move) {
        ArrayList<Tuple> ret = new ArrayList<>();

        Tuple<Integer, Integer> fra = move.getX();
        Tuple<Integer, Integer> til = move.getY();
        ret.add(fra);
        ret.add(til);

        iPiece target = grid[til.getX()][til.getY()];

        grid[til.getX()][til.getY()] = grid[fra.getX()][fra.getY()];
        grid[fra.getX()][fra.getY()] = null;
        // TODO: 14.03.2020 En passant, og rokadelogikk

        isWhitesTurn = !isWhitesTurn;

        return ret; //Returnerer en liste over lokasjoner som ble endret på, så de kan bli tegnet på nytt.
    }

    public Boolean checkPlayerMove(Move playerMove)
    {
        List<Move> availableMoves;
        if (IsWhitesTurn()) availableMoves = GenMoves(WHITE);
        else availableMoves = GenMoves(BLACK);
        Boolean ret = false;
        for (Move move : availableMoves) {
            if (move.equals(playerMove)) {
                ret = true;
            }
        }
        /* // TODO: 15.03.2020 Fiks denne, så det ikke blir lov å sette seg selv i sjakk
        if(ret) {
            Board copy = this.Copy();
            copy.MovePiece(playerMove);
            Move[] counterMoves;
            if (copy.isWhitesTurn) counterMoves = GenMoves(WHITE);
            else counterMoves = GenMoves(BLACK);
            for (Move counter : counterMoves) {
                iPiece target = copy.GetGrid()[counter.getY().getX()][counter.getY().getY()];
                if (target instanceof King) return false;
            }
            return true;
        */
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
        iPiece[][] retgrid = new iPiece[8][8];
        for(int y=0; y<8; y++){
            for(int x=0; x<8; x++){
                retgrid[y][x] = grid[y][x];
            }
        }
        return retgrid;
    }

    @Override
    public Board Copy()
    {
        return new Board(this.GetGrid(), this.isWhitesTurn, this.wScore, this.bScore, this.wCastle, this.bCastle);
    }

    public Boolean IsWhitesTurn(){ return this.isWhitesTurn; }

    public WhiteBlack GetColorToMove(){
        if(isWhitesTurn) return WHITE;
        else return BLACK;
    }
    public List<iPiece> GetPieceList(WhiteBlack c){
        List<iPiece> ret = new ArrayList<>();
        for(int y=0; y<8; y++){
            for(int x=0; x<8; x++){
                iPiece pie = grid[y][x];
                if(pie != null && pie.getColor() == c) ret.add(pie);
            }
        }
        return ret;
    }

    @Override
    public iPiece GetPiece(Tuple<Integer, Integer> pos) {
        return grid[pos.getX()][pos.getY()];
    }

    public iPiece GetPiece(int x, int y){ return grid[x][y]; }

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
        for(int y=0; y<8; y++){
            String rekke = "";
            for(int x=0; x<8; x++){
                iPiece pie = GetPiece(x, y);
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