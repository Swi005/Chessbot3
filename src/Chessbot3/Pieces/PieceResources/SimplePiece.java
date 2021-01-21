package Chessbot3.Pieces.PieceResources;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

import static Chessbot3.GuiMain.Chess.*;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.BLACK;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public abstract class SimplePiece implements iPiece {
    protected final WhiteBlack color;
    protected int inherentValue;
    protected Character symbol;
    protected Boolean canSprint;
    protected String imageKey;

    public SimplePiece(WhiteBlack c){
        //Det eneste som er felles for alle brikker er hvordan fargen blir assignert.
        //Verdi, symbolet, bildet osv er unikt for hver brikketype, og blir fikset i konstruktøren til de andre brikkeklassene.
        this.color = c;
    }

    public Boolean isWhite() { return color == WHITE; }
    public Boolean isBlack() { return color == BLACK; }
    public WhiteBlack getColor() { return this.color; }

    public Boolean isOppositeColor(iPiece p) { //Om en brikke har en annen farge enn en annen brikke. Da kan disse angripe hverandre.
        if(p == null) return true;
        return this.getColor() != p.getColor();
    }
    public Boolean canSprint(){ return canSprint; } //Om brikken kan gå flere skritt om gangen eller ei.
    public String toString(){ return color + " " + getClass().getName().substring(7).toUpperCase(); }

    public Character getSymbol(){ return symbol; }

    //Returnerer bildet som representerer brikken.
    public BufferedImage getImage() { return imageDict.get(imageKey); }

    //Returnerer både den innebgyde verdien, og verdien til hvor godt plassert brikken er.
    //Denne brukes gjerne når du dreper en brikke og vil ha score for det.
    public int getCombinedValue(Tuple<Integer, Integer> pos) { return getValueAt(pos) + inherentValue; }

    //Returnerer den innebygde verdien til denne brikken. Bønder er 100, dronning er 900, osv.
    public int getInherentValue() { return inherentValue; }

    public int getValueAt(int x, int y){ return getValueAt(new Tuple(x, y)); }

    public int getValueAt(Tuple<Integer, Integer> pos){
        //Returnerer et tall for hvor bra det generelt er å stå for denne brikken på den posisjonen.
        if(color == WHITE) return posValueDict.get(symbol)[pos.getY()][pos.getX()];
        else return posValueDict.get(symbol)[7-pos.getY()][7-pos.getX()];
    }

    @Override
    public boolean equals(Object obj){
        return this.getClass().equals(obj.getClass()) && (this.getColor().equals(((SimplePiece) obj).getColor()));
    }

    @Override
    public int hashCode() { return Objects.hash(color, getClass()); }

    @Override
    public ArrayList<Move> getMoves(Board bård, Tuple<Integer, Integer> position){
        //Lager en liste over trekk som denne brikken kan ta akkurat nå.
        //NB! Bønder må overskrive denne funksjonen, siden de fungerer helt annereledes.
        //Denne tar IKKE hensyn til om trekket setter kongen i sjakk, det må sjekkes i checkPlayerMove.
        ArrayList<Move> ret = new ArrayList<>();
        Integer fraX = position.getX();
        Integer fraY = position.getY();
        Tuple<Integer, Integer> fraPos = new Tuple<>(fraX, fraY);

        for(Tuple<Integer, Integer> retning : direcDict.get(symbol)){ //Looper igjennom hver enkelt retning den kan gå.
            Integer tilX = fraX;
            Integer tilY = fraY;

            for(int i=0; i<7; i++){ //Looper til 7, for noen ganger kan brikken gå 7 skritt, men den forventer å bli brutt før det.
                tilX += retning.getX();
                tilY += retning.getY();
                Tuple<Integer, Integer> tilPos =  new Tuple<>(tilX, tilY);

                if(tilX < 0 || tilY < 0 || tilX > 7 || tilY > 7) break; //Om den prøver å gå ut av brettet.

                iPiece mål = bård.getPiece(tilPos);
                if(mål == null){
                    ret.add(new Move(fraPos, tilPos)); //Om det er en tom rute.
                }
                else if(this.isOppositeColor(mål))      {
                    ret.add(new Move(fraPos, tilPos)); //Om det står en fiendtlig brikke der.
                    break;
                }
                else break;

                if(!canSprint) break; //Om brikken er en konge/hest/bonde, kan den ikke gå mer enn ett skritt om gangen. Derfor bryttes loopen her.
            }
        }
        return ret;
    }

}