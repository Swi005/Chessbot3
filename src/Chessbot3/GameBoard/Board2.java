package Chessbot3.GameBoard;

import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.PieceResources.PieceFactory;
import Chessbot3.Pieces.PieceResources.WhiteBlack;
import Chessbot3.Pieces.PieceResources.iPiece;
import Chessbot3.Pieces.Types.King;
import Chessbot3.Pieces.Types.Pawn;
import Chessbot3.Pieces.Types.Queen;

import java.awt.event.MouseEvent;
import java.util.*;
import java.util.stream.Collectors;

import static Chessbot3.GuiMain.Chess.gui;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.BLACK;
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

    private int counter = 0;
    private WhiteBlack colorToMove = WHITE;
    private final iPiece[][] grid = new iPiece[8][8];
    private final Stack<Move> moves = new Stack<>();
    private final Stack<Integer> scores = new Stack<>();
    private final Stack<DeathLog> deaths = new Stack<>();
    private final Stack<boolean[]> castles = new Stack<>();

    //Hjørneindekser og andre nyttige ting
    private static final Tuple<Integer, Integer> A1 = new Tuple<>(0, 7);
    private static final Tuple<Integer, Integer> H1 = new Tuple<>(7, 7);
    private static final Tuple<Integer, Integer> A8 = new Tuple<>(0, 0);
    private static final Tuple<Integer, Integer> H8 = new Tuple<>(7, 0);
    private static final Tuple<Integer, Integer> E1 = new Tuple<>(4, 7);
    private static final Tuple<Integer, Integer> E8 = new Tuple<>(4, 0);

    public Board2(){
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                grid[y][x] = PieceFactory.getPiece(initialBoard[y][x], new Tuple<>(x, y));
            }
        }
        moves.push(new Move(new Tuple<>(4, 7), new Tuple<>(4, 6)));    //Defaultmove er Ke2, bare fordi stacken må ha minste ett move for å unngå kræsj i getMoves.
        scores.push(0);                                                      //Initiell score
        castles.push(new boolean[] {true, true, true, true});                     //Til å begynne med kan begge rokere begge veier.
    }

    public static void main(String[] args) {
        Board2 bård = new Board2();
        Scanner scanner = new Scanner(System.in);
        while (true){
            String input = scanner.nextLine();
            if (input.equals("back")) bård.goBack();
            else if (Move.isAMove(input) && bård.checkMoveLegality(new Move(input))) bård.movePiece(new Move(input));
            else System.out.println("Not a legal move!");
            System.out.println(bård);
            System.out.println(bård.scores);
            System.out.println(bård.moves);
            System.out.println(bård.counter);
            System.out.println(bård.deaths);
            System.out.println(Arrays.toString(bård.castles.peek()));
            System.out.println(bård.colorToMove);
        }

    }

    private static final Tuple<Integer, Integer> defaultPassantPos = new Tuple(-1, -1);
    public Tuple<Integer, Integer> getPassantPos(){
        Move prev = moves.peek();
        iPiece pie = grid[prev.getTo().getY()][prev.getTo().getX()];
        if (pie != null && pie instanceof Pawn && Math.abs(prev.getFrom().getY() - prev.getTo().getY()) == 2){
            return new Tuple(prev.getFrom().getX(), (prev.getFrom().getY() + prev.getTo().getY()) / 2);
        }
        else return defaultPassantPos;
    }

    public ArrayList<Move> getMoves(WhiteBlack color){
        ArrayList<Move> ret = new ArrayList<>();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                iPiece pie = grid[y][x];
                if (pie != null && pie.getColor() == colorToMove) ret.addAll(pie.getMoves(this));
            }
        }
        return ret;
    }
    
    public ArrayList<Move> getMoves(){ return getMoves(colorToMove); }

    public void movePiece(Move move){ movePiece(move, false); }

    private static final Tuple[] castleMoves = {
            new Tuple<>(new Move(new Tuple<>(4, 7), new Tuple<>(2, 7)),
                        new Move(new Tuple<>(0, 7), new Tuple<>(3, 7))),
            new Tuple<>(new Move(new Tuple<>(4, 7), new Tuple<>(6, 7)),
                        new Move(new Tuple<>(7, 7), new Tuple<>(5, 7))),
            new Tuple<>(new Move(new Tuple<>(4, 0), new Tuple<>(2, 0)),
                        new Move(new Tuple<>(0, 0), new Tuple<>(3, 0))),
            new Tuple<>(new Move(new Tuple<>(4, 0), new Tuple<>(6, 0)),
                        new Move(new Tuple<>(7, 0), new Tuple<>(5, 0)))
    };
    public void movePiece(Move move, boolean isHumanPlayer){
        int scoreDelta = 0;

        Tuple<Integer, Integer> from = move.getFrom();
        Tuple<Integer, Integer> to   = move.getTo();

        iPiece piece = grid[from.getY()][from.getX()];
        if (piece == null) throw new NullPointerException("The piece to move is null: " + move + "\n" + this);
        iPiece target = grid[to.getY()][to.getX()];
        if (target != null){
            scoreDelta += target.getCombinedValue(to);
            deaths.push(new DeathLog(counter, target, to));
        }

        for (Tuple<Move, Move> tup : castleMoves){
            if (move.equals(tup.getX())){
                grid[tup.getY().getTo().getY()][tup.getY().getTo().getX()] = grid[tup.getY().getFrom().getY()][tup.getY().getFrom().getX()];
                grid[tup.getY().getFrom().getY()][tup.getY().getFrom().getX()] = null;
                scoreDelta += 25;     //Bonuspoeng for å rokere.
                break;
            }
        }
        if (to.equals(getPassantPos())){
            iPiece untouchedTarget = grid[from.getY()][to.getX()];
            if (untouchedTarget == null) throw new NullPointerException("The passantpos is null? " + move + "\n" + this);
            scoreDelta += untouchedTarget.getCombinedValue(new Tuple<>(to.getX(), from.getY()));
            grid[from.getY()][to.getX()] = null;

        }

        scoreDelta -= piece.getValueAt(from);
        scoreDelta += piece.getValueAt(to);

        grid[to.getY()][to.getX()] = grid[from.getY()][from.getX()];
        grid[from.getY()][from.getX()] = null;

        if (piece instanceof Pawn && (to.getY() == 0 || to.getY() == 7)){
            iPiece promoted;
            if (isHumanPlayer) promoted = gui.promotePawn(to);
            else promoted = new Queen(colorToMove, to);
            grid[to.getY()][to.getX()] = promoted;

            //Når er bonde blir promotert, skriver vi at den 'dør', slik at vi husker at det sto en bonde der når vi flytter tilbkae igjen.
            deaths.push(new DeathLog(counter, piece, from));
            scoreDelta -= piece.getCombinedValue(to);
            scoreDelta += promoted.getCombinedValue(to);
        }

        counter++;
        if (colorToMove == WHITE) scores.push(scores.peek() + scoreDelta);
        else scores.push(scores.peek() - scoreDelta);
        colorToMove = getOppositeColor(colorToMove);
        moves.push(move);
        boolean[] prevCastle = castles.peek();
        castles.push(new boolean[]{prevCastle[0] && !from.equals(A1) && !to.equals(A1) && !from.equals(E1),
                                   prevCastle[1] && !from.equals(H1) && !to.equals(H1) && !from.equals(E1),
                                   prevCastle[2] && !from.equals(A8) && !to.equals(A8) && !from.equals(E8),
                                   prevCastle[3] && !from.equals(H8) && !to.equals(H8) && !from.equals(E8)});
    }

    public void goBack(){
        if (counter <= 0) throw new IllegalStateException("Cannot go further back!");
        counter--;
        colorToMove = getOppositeColor(colorToMove);
        scores.pop();
        castles.pop();
        Move prev = moves.pop();

        grid[prev.getFrom().getY()][prev.getFrom().getX()] = grid[prev.getTo().getY()][prev.getTo().getX()];
        grid[prev.getTo().getY()][prev.getTo().getX()] = null;

        //Respawner opptil to drepte brikker. Det eneste tilfellet der to brikker har dødd på samme tur
        // er om en bonde tar en brikke samtidig som den promoterer seg selv. Da 'dør' bonden.
        for (int i = 0; i < 2; i++) {
            if (deaths.isEmpty()) break;
            DeathLog last = deaths.peek();
            if (last.time == counter){
                grid[last.pos.getY()][last.pos.getX()] = last.piece;
                deaths.pop();
            }
            else break;
        }

        for (Tuple<Move, Move> cmove : castleMoves){
            if (cmove.getX().equals(prev)){
                grid[cmove.getY().getFrom().getY()][cmove.getY().getFrom().getX()] = grid[cmove.getY().getTo().getY()][cmove.getY().getTo().getX()];
                grid[cmove.getY().getTo().getY()][cmove.getY().getTo().getX()] = null;
                break;
            }
        }

    }

    // Funksjonell programmering FTW 8)
    public List<Move> getCompletelyLegalMoves(){
        return getMoves().stream().filter(this::checkMoveLegality).collect(Collectors.toList());
    }

    // TODO: 19.01.2021 Denne er litt buggy
    public boolean checkMoveLegality(Move move){
        iPiece pie = grid[move.getFrom().getY()][move.getFrom().getX()];
        if (pie == null) throw new IllegalStateException("The piece is null: " + move + "\n" + this);
        boolean ret = false;
        for (Move m : pie.getMoves(this)){
            if (m.equals(move)){
                ret = true;
                break;
            }
        }
        if (ret) {
            WhiteBlack color = colorToMove;
            movePiece(move, false);
            for (Move counter : getMoves()) {
                iPiece target = grid[counter.getTo().getY()][counter.getTo().getX()];
                if (target instanceof King && target.getColor() == color) {
                    System.out.println("Broooo");
                    goBack();
                    return false;
                }
            }
            goBack();
            return true;
        }
        else return false;
    }

    public ArrayList<Move> getPreviousMoves(){ return new ArrayList<>(moves); }
    public int getScore(){ return scores.peek(); }
    public boolean[] getCastle(){ return castles.peek(); }
    public WhiteBlack getColorToMove(){ return colorToMove; }

    public iPiece getPiece (Tuple<Integer, Integer> pos) {return grid[pos.getY()][pos.getX()]; }
    public iPiece getPiece(int x, int y){ return grid[y][x]; }

    private static class DeathLog{
        int time;
        iPiece piece;
        Tuple<Integer, Integer> pos;
        public DeathLog(int time, iPiece type, Tuple<Integer, Integer> pos){
            this.time = time; this.piece = type; this.pos = pos;
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

    WhiteBlack getOppositeColor(WhiteBlack c){
        if (c == WHITE) return BLACK;
        else return WHITE;
    }

}
