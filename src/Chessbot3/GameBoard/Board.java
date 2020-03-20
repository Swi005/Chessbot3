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

    private static final Tuple A8 = new Tuple(0,0);
    private static final Tuple H8 = new Tuple(7,0);
    private static final Tuple A1 = new Tuple(0,7);
    private static final Tuple H1 = new Tuple(7,7);
    private static final Tuple E1 = new Tuple(4,7);
    private static final Tuple E8 = new Tuple(4,0);


    iPiece[][] grid;

    private int wScore;
    private int bScore;
    private boolean isWhitesTurn = true;
    private Tuple<Boolean, Boolean> wCastle;
    private Tuple<Boolean, Boolean> bCastle;

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
        wScore = 0;
        bScore = 0;

    }

    public Board(iPiece[][] customBoard, boolean isWhite, int wScore, int bScore, Tuple<Boolean, Boolean> wCastle, Tuple<Boolean, Boolean> bCastle)
    //En konstruktør som kun skal brukes for å opprette en kopi av et tidligere brett.
    //Denne tar inn score, rokadebetingelser, osv fra det forrige brettet.
    {
        this.grid = customBoard;
        this.isWhitesTurn = isWhite;
        this.wCastle = wCastle;
        this.bCastle = bCastle;
        this.wScore = wScore;
        this.bScore = bScore;
    }

    public List<Move> GenMoves(WhiteBlack c){
        //Tar inn en farge og gir deg en liste over alle trekk den spilleren kan ta akkurat nå,
        // inkludert rokader og en passant.
        List<iPiece> list = GetPieceList(c);
        ArrayList<Move> ret = new ArrayList();
        for(iPiece pie : list){
            ret.addAll(pie.getMoves(this));
        }
        ret.addAll(GetCastleMoves(c)); //Legger til rokadetrekk
        // TODO: 20.03.2020 Legg til passanttrekk 
        return ret;
    }
    private List<Move> GetCastleMoves(WhiteBlack c){
        //En funksjon kun for å legge til rokader som lovlige trekk.
        //En rokade er lov om:
        // 1: Kongen ikke har flyttet seg i det hele tatt ennå
        // 2: Tårnet på den valgte siden ikke har flyttet seg ennå (og lever fortsatt)
        // 3: Alle rutene imellom kongen og det valgte tårnet er åpne
        // 4: Ingen av rutene fra og med kongen til og med der kongen vil flytte blir truet av noen fiendtlig brikke. 
        // Nummer 4 ignorerer vi, fordi det vil doble kompleksiteten til GenMoves.
        List<Move> ret = new ArrayList<>();
        if(c == WHITE){
            if(wCastle.getY() && GetPiece(5, 7) ==  null && GetPiece(6, 7) == null){
                ret.add(new Move(new Tuple(4, 7), new Tuple(6, 7))); //Hvit rokerer kort
            }
            if(wCastle.getX() && GetPiece(3, 7) ==  null && GetPiece(2, 7) == null && GetPiece(1, 7) ==  null){
                ret.add(new Move(new Tuple(4, 7), new Tuple(2, 7))); //Hvit rokerer langt
            }
        }else if(c == BLACK){
            if(bCastle.getY() && GetPiece(5, 0) ==  null && GetPiece(6, 0) == null){
                ret.add(new Move(new Tuple(4, 0), new Tuple(6, 0))); //Svart rokerer kort
            }
            if(bCastle.getX() && GetPiece(3, 0) == null && GetPiece(2, 0) ==  null && GetPiece(1, 0) ==  null){
                ret.add(new Move(new Tuple(4, 0), new Tuple(2, 0))); //Svart rokerer langt
            }
        }
        return ret;
    }

    public Tuple<Integer, Integer> GetCoordsOfPiece(iPiece piece) throws IllegalArgumentException {
        //Søker etter en brikke og returnerer koordinatene dens.
        //Kræsjer om den ikke finner den.
        for (int i = 0; i < grid.length; i++) {
            iPiece[] subRow = grid[i];
            for (int j = 0; j < subRow.length; j++) {
                if (subRow[j] == piece)
                    return new Tuple<Integer, Integer>(i, j);
            }
        }
        throw new IllegalArgumentException("Fant ikke brikken!");
    }

    public int AddScore(iPiece piece) {
        if(piece == null) return 0;
        if (piece.getColor() == WHITE)
            bScore += piece.getValue();
        else
            wScore += piece.getValue();
        return piece.getValue();
    }
    public void AddScore(Integer x, WhiteBlack c){
        if(c == WHITE) wScore += x;
        else bScore += x;
    }

    @Override
    public ArrayList<Tuple> MovePiece(Move move) {
        //Flytter en brikke. Denne oppdaterer rokadebetingelser og en passant.
        //Denne driter i om trekket er lovlig eller ikke, det må sjekkes med checkPlayerMove/GenMoves.
        //Returnerer en liste over lokasjoner som ble endret på, slik at paintPiece kan oppdatere GUI.
        ArrayList<Tuple> ret = new ArrayList<>();
        System.out.println(wCastle);

        Tuple<Integer, Integer> fra = move.getX();
        Tuple<Integer, Integer> til = move.getY();

        //Oppdaterer rokadebetingelser
        if(fra.equals(A1) || til.equals(A1)) wCastle.setX(false); //Om et tårn blir tatt eller flyttet, kan ikke lenger spilleren rokere den veien.
        else if(fra.equals(H1) || til.equals(H1)) wCastle.setY(false);
        else if(fra.equals(A8) || til.equals(A8)) bCastle.setX(false);
        else if(fra.equals(H8) || til.equals(H8)) bCastle.setY(false);

        else if(fra.equals(E1)) wCastle = new Tuple(false, false); //Om kongen flytter seg, kan den aldri rokere.
        else if(fra.equals(E8)) bCastle = new Tuple(false, false);



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
        //Sjekker om spillerens trekk er lovlig.
        // Tar hensyn til om trekket setter seg selv i sjakk.
        // Returnerer true om det er lov.
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
    public int GetScore(WhiteBlack color){
        //Returnerer scoren til en farge.
        if(color == WHITE) return wScore;
        else return bScore;
    }

    @Override
    public iPiece[][] GetGrid()
    {
        //Returnerer selve rutenettet av brikker.
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
        //Returnerer en kopi av brettet, og husker hvem som kan rokerer hvor, og scoren til hver spiller.
        return new Board(this.GetGrid(), this.isWhitesTurn, this.wScore, this.bScore, this.wCastle, this.bCastle);
    }

    public Boolean IsWhitesTurn(){ return this.isWhitesTurn; }

    public WhiteBlack GetColorToMove(){
        //Returnerer hvilken farge som skal flytte.
        if(isWhitesTurn) return WHITE;
        else return BLACK;
    }
    public List<iPiece> GetPieceList(WhiteBlack c){
        //Lager en liste over alle brikkene til en farge.
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
        //Reverserer brettet. Nå er plutselig svart nederst!.
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