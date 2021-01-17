package Chessbot3.GameBoard;

import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.PieceResources.PieceFactory;
import Chessbot3.Pieces.PieceResources.WhiteBlack;
import Chessbot3.Pieces.PieceResources.iPiece;

import java.util.ArrayList;
import java.util.Stack;

import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class Board2 {

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

    private iPiece[][] grid = new iPiece[8][8];
    private Stack<Move> moves = new Stack<>();
    private Stack<Integer> scores = new Stack<>();
    private Stack<DeathLog> deaths = new Stack<>();
    private Stack<boolean[]> castles = new Stack<>();
    private int counter = 0;
    private WhiteBlack colorToMove = WHITE;

    public Board2(){
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                grid[y][x] = PieceFactory.getPiece(initialBoard[y][x], new Tuple(x, y));
            }
        }
        moves.push(new Move(new Tuple<>(4, 7), new Tuple<>(4, 6)));     //Defaultmove er Ke2, bare fordi stacken må ha minste ett move for å unngå kræsj i getMoves.
        scores.push(0);     //Initiell score
        castles.push(new boolean[] {true, true, true, true});   //Til å begynne med kan begge rokere begge veier.
    }



    public ArrayList<Move> getMoveList(){ return new ArrayList<>(moves); }
    public int getScore(){ return scores.peek(); }
    public boolean[] getCastle(){ return castles.peek(); }
    public WhiteBlack getColorToMove(){ return colorToMove; }

    public iPiece getPiece (Tuple<Integer, Integer> pos) {return grid[pos.getY()][pos.getX()]; }
    public iPiece getPiece(int x, int y){ return grid[y][x]; }

    private static class DeathLog{
        int time;
        iPiece type;
        Tuple<Integer, Integer> pos;
        public DeathLog(int time, iPiece type, Tuple<Integer, Integer> pos){
            this.time = time; this.type = type; this.pos = pos;
        }
    }

    @Override
    public String toString(){
        //Lager en streng av brettet, og printer score nederst.
        String ret = "";
        for(int y=0; y<8; y++){
            String rekke = "";
            for(int x=0; x<8; x++){
                iPiece pie = getPiece(x, y);
                if(pie == null) rekke += ".";
                else if(pie.isWhite()) rekke += pie.getSymbol();
                else rekke += Character.toLowerCase(pie.getSymbol());
                rekke += "";
            }
            ret += rekke + "\n";
        }
        ret += "Score: " + getScore();
        return ret;
    }

}
