package Pieces;

import Chessbot3.Color;
import Chessbot3.GameBoard.Board;
import Chessbot3.Move;
import Chessbot3.Tuple;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Chessbot3.Chess.direcDict;
import static Chessbot3.Color.BLACK;
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
    public Boolean isBlack() { return color == BLACK; }
    public Color getColor() { return this.color; }

    public Boolean isOppositeColor(iPiece p){ return this.getColor() != p.getColor(); }
    public Boolean canSprint(){ return canSprint; }

    public Integer getX(){ return getCoords().getX(); }
    public Integer getY(){ return getCoords().getY(); }
    public Tuple<Integer, Integer> getCoords() { return gameboard.GetCoordsOfPiece(this); }

    public int getValue() { return value; }
    public Character getSymbol(){ return symbol; }

    public ArrayList<Move> getMoves(){
        //Lager en liste over trekk som denne brikken kan ta akkurat nå. NB! Bønder må overskrive denne funksjonen, siden de fungerer helt annereledes.
        //Denne tar IKKE hensyn til om trekket setter kongen i sjakk, det må gjøres i en annen funksjon.
        ArrayList<Move> ret = new ArrayList<>();
        iPiece[][] grid = gameboard.GetGrid();
        Integer fraX = getX();
        Integer fraY = getY();
        for(Tuple<Integer, Integer> retning : direcDict.get(symbol)){ //Looper igjennom hver enkelt retning den kan gå.
            Integer tilX = fraX;
            Integer tilY = fraY;
            for(int i=0; i<=7; i++){ //Looper til 7, for noen ganger kan den gå 7 skritt, men forventer å bli brutt før det.
                tilX += retning.getX();
                tilY += retning.getY();

                if(tilX < 0 || tilY < 0 || tilX > 7 || tilY > 7) break; //Om den prøver å gå ut av brettet.
                iPiece mål = grid[tilX][tilY];
                if(mål == null || this.isOppositeColor(mål)){ //Kan kun gå der om det er en tom rute eller en fiendtlig brikke.
                    ret.add(new Move(new Tuple(fraX, fraY), new Tuple(tilX, tilY)));
                } else break;
                if(!canSprint) break; //Om brikken er en konge/hest/bonde, kan den ikke gå mer enn ett skritt om gangen. Derfor bryttes loopen her.
            }
        }
        return ret;
    }

}