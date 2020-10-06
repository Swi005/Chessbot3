package Chessbot3.GameBoard;

import static Chessbot3.GuiMain.Chess.gui;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.BLACK;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;
import static java.lang.StrictMath.abs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.PieceResources.PieceFactory;
import Chessbot3.Pieces.PieceResources.WhiteBlack;
import Chessbot3.Pieces.PieceResources.iPiece;
import Chessbot3.Pieces.Types.King;
import Chessbot3.Pieces.Types.Pawn;
import Chessbot3.Pieces.Types.Queen;
import Chessbot3.Pieces.Types.Rook;

/**
 * Board
 */
public class Board implements Comparable {

    //Det initielle brettet. Denne kan endres på for å debugge ting litt fortere.
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

    //Hjørneindekser
    private static final Tuple A8 = new Tuple(0,0);
    private static final Tuple H8 = new Tuple(7,0);
    private static final Tuple A1 = new Tuple(0,7);
    private static final Tuple H1 = new Tuple(7,7);

    //Andre nyttige indekser
    private static final Tuple E1 = new Tuple(4,7);
    private static final Tuple E8 = new Tuple(4,0);
    private static final Tuple G1 = new Tuple(6,7);
    private static final Tuple C1 = new Tuple(2,7);
    private static final Tuple G8 = new Tuple(6,0);
    private static final Tuple C8 = new Tuple(2,0);

    //Rutenettet av brikker
    private iPiece[][] grid;

    //'Verdien' til brettet. Positivt tall er bra for hvit, negativt er bra for svart.
    private Integer score;

    //Hvem sin tur det er. Begynner alltid med hvit #SexismInVideoGames
    private WhiteBlack colorToMove = WHITE;

    //Hvor hvit kan rokere.
    private Tuple<Boolean, Boolean> wCastle;

    //Hvor svart kan rokere.
    private Tuple<Boolean, Boolean> bCastle;

    //Hvor det er lov til å ta en passant.
    //Denne er vanligvis (-1, -1), som er utenfor brettet, men blir endret når en bonde flytter to skritt frem.
    private Tuple<Integer, Integer> passantPos;

    public Board() {
        //Det initielle brettet. Plasserer brikker med hensyn på initialBoard.
        grid = new iPiece[8][8];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                iPiece pie = PieceFactory.getPiece(initialBoard[y][x]);
                grid[x][y] = pie;
            }
        }
        wCastle = new Tuple<>(true, true);
        bCastle = new Tuple<>(true, true);
        score = 0;
        passantPos = new Tuple(-1, -1);
    }

    public Board(iPiece[][] customBoard, WhiteBlack colorToMove, int score, Tuple<Boolean, Boolean> wCastle, Tuple<Boolean, Boolean> bCastle, Tuple<Integer, Integer> passantPos)
    //En konstruktør som kun skal brukes for å opprette en kopi av et tidligere brett.
    //Denne tar inn score, rokadebetingelser, osv fra det forrige brettet.
    {
        this.grid = customBoard;
        this.colorToMove = colorToMove;
        this.wCastle = wCastle;
        this.bCastle = bCastle;
        this.score = score;
        this.passantPos = passantPos;
    }

    //Lager en liste over nesten-lovlige trekk som kan gjøres akkurat nå.
    //Denne tar IKKE hensyn til om trekket setter kongen i sjakk.
    public List<Move> genMoves(){ return genMoves(colorToMove); }

    public List<Move> genMoves(WhiteBlack color){
        //Tar inn en farge og gir deg en liste over alle trekk den spilleren kan ta akkurat nå,
        //inkludert rokader og en passant.
        ArrayList<Move> ret = new ArrayList();
        for(iPiece pie : getPieceList(color)){
            ret.addAll(pie.getMoves(this));
        }
        return ret;
    }

    //Returnerer en liste over helt lovlige trekk. Denne er mye mer kompleks enn GenMoves, og bør ikke brukes av botten.
    public List<Move> genCompletelyLegalMoves(){ return genCompletelyLegalMoves(colorToMove); }

    //Alternativ versjon, om du vil finne de teoritiske lovlige trekkene til en spesifikk farge,
    //selv om det ikke nødvendigvis er den sin tur.
    public List<Move> genCompletelyLegalMoves(WhiteBlack color){
        List<Move> ret = new ArrayList<>();
        for(Move move : genMoves(color)) if(checkMoveLegality(move)) ret.add(move);
        return ret;
    }

    public Tuple<Integer, Integer> getCoordsOfPiece(iPiece piece) throws IllegalArgumentException {
        //Søker etter en brikke og returnerer koordinatene dens.
        //Kræsjer om den ikke finner den.
        for (int i = 0; i < grid.length; i++) {
            iPiece[] subRow = grid[i];
            for (int j = 0; j < subRow.length; j++) {
                if (subRow[j] == piece)
                    return new Tuple(i, j);
            }
        }
        throw new IllegalArgumentException("Fant ikke brikken!");
    }
    public int getValue(Move move){
        //Regner ut den umiddelbare verdien av et trekk.
        //Denne gir høy score om du tar en brikke eller flytter til en bedre posisjon,
        //og lav score om du flytter til en dårligere posisjon.
        //Denne ignorerer helt hva motstanderen kan gjøre, så om trekket ditt er å ofre en dronning for å ta en bonde,
        //mener denne funksjonen fortsatt at det er et bra trekk.

        //Gir score for å flytte til en bedre posisjon.
        iPiece pie = getPiece(move.getX());
        int ret = pie.getValueAt(move.getY()) - pie.getValueAt(move.getX());

        //Gir score for å ta en brikke på normalt vis.
        iPiece target = getPiece(move.getY());
        if(target != null) ret += target.getCombinedValue(move.getY());

        //Gir score for å ta en passant.
        if(move.getY() == passantPos) ret += 120; //Lettere enn å regne ut den egentlige verdien av å ta den brikken.

        if(colorToMove == WHITE) return ret;
        else return -ret;
    }

    private void addScore(int x){
        //Legger til score.
        //Holder selv styr på hvem sin tur det er, og dermed også om den skal legge til eller trekke fra score.
        if(colorToMove == WHITE) score += x;
        else score -= x;
    }
    public void movePiece(Move move){ movePiece(move, false); }

    public void movePiece(Move move, Boolean isHumanPlayer) {
        //Flytter en brikke. Denne oppdaterer rokadebetingelser og en passant.
        //Denne driter i om trekket er lovlig eller ikke, det må sjekkes med CheckPlayerMove/GenMoves.
        //Om isHumanPlayer=true, og trekket er at en bonde blir flyttet til enden av brettet,
        //vil denne lage et pupup-vindu om hvilken brikke bonden skal promoteres til.
        //Hvis ikke, spawnes bare en dronning.

        Tuple<Integer, Integer> fra = move.getX();
        Tuple<Integer, Integer> til = move.getY();
        iPiece pie = getPiece(fra);
        iPiece target = getPiece(til);

        //Oppdaterer rokadebetingelser
        //Om et tårn blir tatt eller flyttet, kan ikke lenger spilleren rokere den veien.
        if(fra.equals(A1) || til.equals(A1)) wCastle.setX(false);
        else if(fra.equals(H1) || til.equals(H1)) wCastle.setY(false);
        else if(fra.equals(A8) || til.equals(A8)) bCastle.setX(false);
        else if(fra.equals(H8) || til.equals(H8)) bCastle.setY(false);

        //Om kongen flytter seg, kan den aldri rokere.
        else if(fra.equals(E1)) wCastle = new Tuple(false, false);
        else if(fra.equals(E8)) bCastle = new Tuple(false, false);

        //Flytter tårnet, om kongen rokerer.
        if(pie instanceof King){
            iPiece tempRook = new Rook(pie.getColor()); //Et midldertidig tårn kun for å regne ut verdien av å flytte et tårn når kongen rokererer.
            if(fra.equals(E1) && til.equals(G1)){
                addScore(tempRook.getValueAt(5, 7) - tempRook.getValueAt(7, 7));
                grid[5][7] = grid[7][7]; //Flytter tårnet når hvit rokerer kort.
                grid[7][7] = null;
            }else if(fra.equals(E1) && til.equals(C1)){
                addScore(tempRook.getValueAt(3, 7) - tempRook.getValueAt(0, 7));
                grid[3][7] = grid[0][7]; //Flytter tårnet når hvit rokerer langt.
                grid[0][7] = null;
            }else if(fra.equals(E8) && til.equals(G8)){
                addScore(tempRook.getValueAt(5, 0) - tempRook.getValueAt(7, 0));
                grid[5][0] = grid[7][0]; //Flytter tårnet når svart rokerer kort.
                grid[7][0] = null;
            }else if(fra.equals(E8) && til.equals(C8)){
                addScore(tempRook.getValueAt(3, 0) - tempRook.getValueAt(0, 0));
                grid[3][0] = grid[0][0]; //Flytter tårnet når svart rokerer langt.
                grid[0][0] = null;
            }
        }

        //Flytter faktisk brikken.
        grid[til.getX()][til.getY()] = pie;
        grid[fra.getX()][fra.getY()] = null;

        //Dreper en bonde, om trekket er en passant.
        if(til.equals(passantPos) && pie instanceof Pawn){
            Tuple<Integer, Integer> tempPos;

            //Finner hvor den drepte brikken er.
            //Dette er mer komplisert enn vanlig, siden en passant gjør at du kan drepe en brikke uten å ta på den.
            if(til.getY() == 2) tempPos = new Tuple(til.getX(), 3);
            else tempPos = new Tuple(til.getX(), 4);

            addScore(getPiece(tempPos).getCombinedValue(tempPos)); //Legger til score for den drepte brikken.
            grid[tempPos.getX()][tempPos.getY()] = null; //Dreper faktisk brikken.
        }
        //oppdaterer passantbetingelser.
        if(passantPos.getX() != -1) passantPos.setX(-1);
        if(pie instanceof Pawn){
            if(abs(fra.getY()-til.getY()) == 2){
                passantPos.setX(fra.getX());
                passantPos.setY((fra.getY()+til.getY())/2);
            }
            //Promoterer bønder.
            else if(til.getY() == 0 || til.getY() == 7){
                if(!isHumanPlayer) grid[til.getX()][til.getY()] = new Queen(pie.getColor()); //Botten får alltid en dronning.
                else grid[til.getX()][til.getY()] = gui.promotePawn(); //Lager et popup-vindu for å promotere.
                addScore(getPiece(til).getCombinedValue(til)); //Legger til bonusscore for å få en ny brikke.
            }
        }
        //Legger til score for å flytte til en bedre posisjon.
        addScore(pie.getValueAt(til)-pie.getValueAt(fra));

        //Legger til score når du tar en brikke.
        if(target != null) addScore(target.getCombinedValue(til));

        //Bytter farge på hvem sin tur det er.
        colorToMove = getOppositeColor(colorToMove);
    }

    public Boolean checkMoveLegality(Move playerMove) {
        //Sjekker om spillerens trekk er lovlig.
        // Tar hensyn til om trekket setter seg selv i sjakk.
        // Returnerer true om det er lov.
        boolean ret = false;
        for (Move move : genMoves(getColorToMove())) if (move.equals(playerMove)){
            ret = true;
            break;
        }

        //Om spillerens trekk er på listen, er det kanskje lovlig.
        //Da må vi simulere at det trekket blir gjort, og se om motstanderen har noen trekk han kan gjøre for å umiddelbart ta kongen.
        //Hvis ja, betyr det at spilleren har satt seg selv i sjakk, eller at han sto i sjakk og ingorerte det.
        //Da er trekket ulovlig, og returnerer false
        if(ret) {
            Board copy = this.copy();
            copy.movePiece(playerMove, false);
            for (Move counter : copy.genMoves(getOppositeColorToMove())) {
                iPiece target = copy.getGrid()[counter.getY().getX()][counter.getY().getY()]; //Finner hvilke brikker fiendtlige trekk eventuelt kan ta.
                if (target instanceof King) return false; //Om motstanderen kan ta kongen.
            }
            return true; //Om motstanderen ikke har noen trekk som kan ta kongen
        }
        return false; //Om trekket ikke engang er på den originale listen.
    }

    public Boolean checkCheckMate(){
        //Sjekker om brettet er sjakkmatt.
        //Returnerer true om det matt, null om det er patt (uavgjort) og false ellers.
        List<Move> legals = genCompletelyLegalMoves();
        if(legals.size()>0) return false; //Om spilleren har lovlige trekk.
        else{
            for(Move move : genMoves(getOppositeColorToMove())){
                iPiece target = grid[move.getY().getX()][move.getY().getY()];
                if(target instanceof King) return true; //Om spilleren ikke har noen lovlige trekk, og kongen blir truet.
            }
            return null; //Om spilleren ikke har noen lovlige trekk, men heller ikke blir truet. Da er det patt.
        }
    }
    //Returnerer score. positivt er bra for hvit, negativt er bra for svart.
    public Integer getScore() { return score; }

    public iPiece[][] getGrid()
    {
        //Returnerer en kopi av selve rutenettet av brikker.
        iPiece[][] retgrid = new iPiece[8][8];
        for(int x=0; x<8; x++){
            for(int y=0; y<8; y++){
                retgrid[x][y] = grid[x][y];
            }
        }
        return retgrid;
    }

    public Board copy()
    {
        //Returnerer en kopi av brettet, og husker hvem som kan rokerer hvor, og scoren til hver spiller.
        return new Board(this.getGrid(), this.colorToMove, this.score, this.wCastle.copy(), this.bCastle.copy(), this.passantPos.copy());
    }

    //Returnerer hvilken farge som skal flytte.
    public WhiteBlack getColorToMove(){ return colorToMove; }

    //Returnerer den andre fargen, den fargen som ikke skal flytte.
    public WhiteBlack getOppositeColorToMove(){ return getOppositeColor(colorToMove); }
    
    public static WhiteBlack getOppositeColor(WhiteBlack c){
        //Tar en farge, og returnerer den andre fargen.
        //Dette er litt det samme som å sette et ikke-tegn foran en farge.
        if(c == WHITE) return BLACK;
        else return WHITE;
    }
    //Returnerer en liste over brikker som tilhører den som skal flytte.
    //Nyttig for å generere trekk.
    public List<iPiece> getPieceList(){ return getPieceList(colorToMove); }

    public List<iPiece> getPieceList(WhiteBlack c){
        //Lager en liste over alle brikkene til en spesifikk farge.
        List<iPiece> ret = new ArrayList<>();
        for(int y=0; y<8; y++){
            for(int x=0; x<8; x++){
                iPiece pie = grid[y][x];
                if(pie != null && pie.getColor() == c) ret.add(pie);
            }
        }
        return ret;
    }
    public Tuple<Boolean, Boolean> getWhiteCastle(){ return wCastle; }

    public Tuple<Boolean, Boolean> getBlackCastle(){ return bCastle; }

    //Returnerer posisjonen hvor det er greit å ta en passant.
    public Tuple<Integer, Integer> getPassantPos(){ return passantPos; }

    //Returnerer hvilke brikke som befinner seg i en posisjon.
    public iPiece getPiece(Tuple<Integer, Integer> pos) { return grid[pos.getX()][pos.getY()]; }

    //Samme som over, men med x,y-koordinater istedet.
    public iPiece getPiece(int x, int y){ return grid[x][y]; }

    //Sammenligner brett med hensyn på score.
    //Om botten skal sortere brett etter verdi må den være klar over om den skal sortere baklengs eller ikke,
    //avhengig av fargen den spiller som.
    public int compareTo(Object other){ return this.getScore().compareTo(((Board) other).getScore()); }

    @Override
    public boolean equals(Object obj){
        Board other = (Board) obj;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                iPiece one = this.grid[i][j];
                iPiece two = other.grid[i][j];

                if(one == null || two == null){
                    if(one == two) continue;
                    else return false;
                }
                if(!one.equals(two)) return false;
            }
        }
        if(!this.wCastle.equals(other.wCastle) || !this.bCastle.equals(other.bCastle)) return false;
        return true;
    }

    @Override
    public int hashCode(){

        int[] primes = {29, 31, 37, 41, 43, 47, 53, 59, 61};
        int result = 0;
        for (int i = 0; i < 8; i++) {
            result += primes[i] * (Arrays.deepHashCode(grid[i]));
        }
        result += primes[8] * Objects.hash(colorToMove);
        return result;
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
        ret += "Score: " + score;
        return ret;
    }
}