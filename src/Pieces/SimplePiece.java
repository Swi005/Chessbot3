package Pieces;

import Chessbot3.GameBoard.Board;
import Chessbot3.Move;
import Chessbot3.Tuple;
import Pieces.iPiece;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static Chessbot3.Chess.moveDict;

public abstract class SimplePiece implements iPiece {
    Boolean isWhite;
    int value;
    Character symbol;
    Boolean canSprint;
    Board gameboard;

    public Boolean isWhite() { return isWhite; }
    public Boolean isBlack() { return !isWhite; }
    public Boolean isOppositeColor(iPiece p){ return isWhite != p.isWhite(); }

    public Boolean canSprint(){ return canSprint; }

    public int getX(){ return 0; } // TODO: 11.03.2020 Fiks disse!
    public int getY(){ return 0; }
    public Tuple<Integer, Integer> getCoords() { return new Tuple(getX(), getY()); }

    public int getValue() { return value; }
    public char getSymbol(){ return symbol; }

    public ArrayList<Move> getMoves(){
        ArrayList<Move> ret = new ArrayList<>();
        Array[][] grid = gameboard.getGrid();
        Integer fraX = getX();
        Integer fraY = getY();
        for(Tuple retning : moveDict.get(symbol)){
            Integer tilX = fraX;
            Integer tilY = fraY;
            for(int i=0; i<=7; i++){
                tilX += (Integer) retning.getX();
                tilY += (Integer) retning.getY();

                if(tilX < 0 || tilY < 0 || tilX > 7 || tilY > 7) break;
                iPiece mål = grid[tilX][tilY];
                if(mål == null || this.isOppositeColor(mål)){
                    ret.add(new Move(new Tuple(fraX, fraY), new Tuple(tilX, tilY)));
                }
                if(!canSprint) break;
            }
        }
        return ret;
    }

}