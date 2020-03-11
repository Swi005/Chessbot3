package Pieces;

import Chessbot3.Color;
import Chessbot3.GameBoard.Board;
import Chessbot3.Move;
import Chessbot3.Tuple;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Chessbot3.Chess.moveDict;
import static Chessbot3.Color.WHITE;

public abstract class SimplePiece implements iPiece {
    Color color;
    int value;
    Character symbol;
    Boolean canSprint;
    Board gameboard;
    BufferedImage image;

    public SimplePiece(Color c, Board gameboard){
        this.color = c;
        this.gameboard = gameboard;
    }

    public Boolean isWhite() { return color == WHITE; }
    public Boolean isBlack() { return !isWhite(); }
    public Color getColor() { return this.color; }

    public Boolean isOppositeColor(iPiece p){ return this.getColor() != p.getColor(); }
    public Boolean canSprint(){ return canSprint; }

    public Integer getX(){ return 0; } // TODO: 11.03.2020 Fiks disse!
    public Integer getY(){ return 0; }
    public Tuple<Integer, Integer> getCoords() { return new Tuple(getX(), getY()); }

    public int getValue() { return value; }
    public Character getSymbol(){ return symbol; }

    public ArrayList<Move> getMoves(){
        ArrayList<Move> ret = new ArrayList<>();
        iPiece[][] grid = gameboard.GetGrid();
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
                } else break;
                if(!canSprint) break;
            }
        }
        return ret;
    }

}