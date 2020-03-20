package Pieces;

import Chessbot3.GameBoard.Board;
import Chessbot3.Move;
import Chessbot3.Tuple;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Chessbot3.Chess.direcDict;
import static Pieces.WhiteBlack.BLACK;
import static Pieces.WhiteBlack.WHITE;

public abstract class SimplePiece implements iPiece {
    WhiteBlack color;
    int value;
    Character symbol;
    Boolean canSprint;
    BufferedImage image;

    public SimplePiece(WhiteBlack c){
        this.color = c;
    }

    public Boolean isWhite() { return color == WHITE; }
    public Boolean isBlack() { return color == BLACK; }
    public WhiteBlack getColor() { return this.color; }

    public Boolean isOppositeColor(iPiece p) {
        if(p == null) return false;
        return this.getColor() != p.getColor();
    }
    public Boolean canSprint(){ return canSprint; }
    public String toString(){ return color + " " + getClass().getName().substring(7).toUpperCase(); }

    public Integer getX(Board bård){ return getCoords(bård).getX(); }
    public Integer getY(Board bård){ return getCoords(bård).getY(); }
    public Tuple<Integer, Integer> getCoords(Board bård) { return bård.GetCoordsOfPiece(this); }

    public int getValue() { return value; }
    public Character getSymbol(){ return symbol; }
    public BufferedImage getImage(){ return image; }

    public ArrayList<Move> getMoves(Board bård){
        //Lager en liste over trekk som denne brikken kan ta akkurat nå. NB! Bønder må overskrive denne funksjonen, siden de fungerer helt annereledes.
        //Denne tar IKKE hensyn til om trekket setter kongen i sjakk, det må sjekkes i en annen funksjon.
        ArrayList<Move> ret = new ArrayList<>();
        iPiece[][] grid = bård.GetGrid();
        Integer fraX = getX(bård);
        Integer fraY = getY(bård);
        for(Tuple<Integer, Integer> retning : direcDict.get(symbol)){ //Looper igjennom hver enkelt retning den kan gå.
            Integer tilX = fraX;
            Integer tilY = fraY;
            for(int i=0; i<=7; i++){ //Looper til 7, for noen ganger kan den gå 7 skritt, men forventer å bli brutt før det.
                tilX += retning.getX();
                tilY += retning.getY();

                if(tilX < 0 || tilY < 0 || tilX > 7 || tilY > 7) break; //Om den prøver å gå ut av brettet.

                iPiece mål = grid[tilX][tilY];
                if(mål == null){
                    ret.add(new Move(new Tuple(fraX, fraY), new Tuple(tilX, tilY))); //Om det er en tom rute.
                }
                else if(this.isOppositeColor(mål))      {
                    ret.add(new Move(new Tuple(fraX, fraY), new Tuple(tilX, tilY))); //Om det står en fiendtlig brikke der.
                    break;
                }
                else break;
                if(!canSprint) break; //Om brikken er en konge/hest/bonde, kan den ikke gå mer enn ett skritt om gangen. Derfor bryttes loopen her.
            }
        }
        return ret;
    }

}