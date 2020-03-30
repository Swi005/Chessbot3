package Chessbot3.Pieces;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Chessbot3.GuiMain.Chess.direcDict;
import static Chessbot3.Pieces.WhiteBlack.BLACK;
import static Chessbot3.Pieces.WhiteBlack.WHITE;

public abstract class SimplePiece implements iPiece {
    protected int[][] posValue;
    protected WhiteBlack color;
    protected int inherentValue;
    protected Character symbol;
    protected Boolean canSprint;
    protected BufferedImage image;

    public SimplePiece(WhiteBlack c){
        //Det eneste som er felles for alle brikker er hvordan fargen blir assignert.
        //Verdi, symbolet, bildet osv er unikt for hver brikketype, og blir fikset i konstruktøren til de andre brikkeklassene.
        this.color = c;
    }

    public Boolean isWhite() { return color == WHITE; }
    public Boolean isBlack() { return color == BLACK; }
    public WhiteBlack getColor() { return this.color; }

    public Boolean isOppositeColor(iPiece p) { //Om en brikke har en annen farge enn en annen brikke. Da kan disse angripe hverandre.
        if(p == null) return false;
        return this.getColor() != p.getColor();
    }
    public Boolean canSprint(){ return canSprint; } //Om brikken kan gå flere skritt om gangen eller ei.
    public String toString(){ return color + " " + getClass().getName().substring(7).toUpperCase(); }

    public Integer getX(Board bård){ return getCoords(bård).getX(); }
    public Integer getY(Board bård){ return getCoords(bård).getY(); }
    public Tuple<Integer, Integer> getCoords(Board bård) { return bård.GetCoordsOfPiece(this); }

    public Character getSymbol(){ return symbol; }

    public BufferedImage getImage() 
    {
        return image;
    }

    //Returnerer både den innebgyde verdien, og verdien til hvor godt plassert brikken er.
    //Denne brukes gjerne når du dreper en brikke og vil ha score for det.
    public int getCombinedValue(Tuple<Integer, Integer> pos) { return getValueAt(pos) + inherentValue; }

    //Returnerer den innebygde verdien til denne brikken. Bønder er 100, dronning er 900, osv.
    public int getInherentValue() { return inherentValue; }

    public int getValueAt(Tuple<Integer, Integer> XY)
            //Returnerer hvor bra det generelt er å stå for denne brikken på denne posisjonen.
    {
        if (color == WHITE)
        {
            return posValue[XY.getY()][XY.getX()];
        }
        else
        {
            return posValue[7 - XY.getY()][7 - XY.getX()];
        }
    }

    public ArrayList<Move> getMoves(Board bård){
        //Lager en liste over trekk som denne brikken kan ta akkurat nå.
        //NB! Bønder må overskrive denne funksjonen, siden de fungerer helt annereledes.
        //Denne tar IKKE hensyn til om trekket setter kongen i sjakk, det må sjekkes i checkPlayerMove.
        ArrayList<Move> ret = new ArrayList<>();
        Integer fraX = getX(bård);
        Integer fraY = getY(bård);
        Tuple<Integer, Integer> fraPos = new Tuple<>(fraX, fraY);

        for(Tuple<Integer, Integer> retning : direcDict.get(symbol)){ //Looper igjennom hver enkelt retning den kan gå.
            Integer tilX = fraX;
            Integer tilY = fraY;

            for(int i=0; i<=7; i++){ //Looper til 7, for noen ganger kan brikken gå 7 skritt, men den forventer å bli brutt før det.
                tilX += retning.getX();
                tilY += retning.getY();
                Tuple<Integer, Integer> tilPos =  new Tuple<>(tilX, tilY);

                if(tilX < 0 || tilY < 0 || tilX > 7 || tilY > 7) break; //Om den prøver å gå ut av brettet.

                iPiece mål = bård.GetPiece(tilPos);
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