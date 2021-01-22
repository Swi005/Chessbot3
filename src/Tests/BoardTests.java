package Tests;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.PieceResources.iPiece;
import Chessbot3.Pieces.Types.King;
import Chessbot3.Pieces.Types.Pawn;
import Chessbot3.Pieces.Types.Queen;
import Chessbot3.Pieces.Types.Rook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static Chessbot3.Pieces.PieceResources.WhiteBlack.BLACK;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTests {

    Board bård;

    @BeforeEach
    void setUp(){
        bård = new Board();
    }

    @Test
    void numberOfInitialMoves(){
        List<Move> wmoves = bård.getLegalMoves();
        List<Move> bmoves = bård.getLegalMoves();
        assertEquals(20, wmoves.size());
        assertEquals(20, bmoves.size());

        bård.movePiece(new Move("e2e4"));
        bård.movePiece(new Move("e7e5"));

        wmoves = bård.getLegalMoves();
        bmoves = bård.getLegalMoves();

        assertEquals(29, wmoves.size());
        assertEquals(29, bmoves.size());
    }

    @Test
    void goBackMovesPieceBack(){
        assertEquals(new Pawn(WHITE), bård.getPiece(0, 6));
        assertEquals(null, bård.getPiece(0, 5));

        bård.movePiece(new Move("a2a3"));

        assertEquals(new Pawn(WHITE), bård.getPiece(0, 5));
        assertEquals(null, bård.getPiece(0, 6));

        bård.goBack();

        assertEquals(new Pawn(WHITE), bård.getPiece(0, 6));
        assertEquals(null, bård.getPiece(0, 5));
    }

    @Test
    void goBackPollsTheStacks(){
        int score = bård.getScore();
        Move prev = bård.getPreviousMoves().get(bård.getPreviousMoves().size()-1); // Defaultmovet før partiet har begynt er e1e2.
        boolean[] castle = bård.getCastle();

        //Dette teamkiller en bonde, endrer score, og gjør at hvit ikke lenger kan rokere.
        bård.movePiece(new Move("e1f2"));

        assertNotEquals(score, bård.getScore());
        assertNotEquals(prev, bård.getPreviousMoves().get(bård.getPreviousMoves().size()-1));
        assertNotEquals(castle[1], bård.getCastle()[1]);

        bård.goBack();

        assertEquals(score, bård.getScore());
        assertEquals(prev, bård.getPreviousMoves().get(bård.getPreviousMoves().size()-1));
        assertEquals(castle[1], bård.getCastle()[1]);

    }

    @Test
    void enPassant(){
        bård.movePiece(new Move("e2e4"));
        bård.movePiece(new Move("a7a6"));
        bård.movePiece(new Move("e4e5"));
        bård.movePiece(new Move("d7d5")); //Nå er en passant lov

        assertTrue(bård.checkMoveLegality(new Move("e5d6")));
        assertEquals(new Tuple<>(3, 2), bård.getPassantPos());
        bård.movePiece(new Move("e5d6"));
        assertNull(bård.getPiece(3, 3));

        bård.goBack();      //Om vi går tilbake igjen, har brettet gått tilbake til dets tidligere tilstand?
        assertNotNull(bård.getPiece(3, 3));
        assertTrue(bård.checkMoveLegality(new Move("e5d6")));

        bård.movePiece(new Move("a2a3")); //Et hvilket som helst trekk annet enn en passant, nå skal hvit miste sjansen til å ta en passant.
        bård.movePiece(new Move("a6a5"));
        assertFalse(bård.checkMoveLegality(new Move("e5d6")));
    }
    
    @Test
    void queening(){
        bård.movePiece(new Move ("a2a4"));
        bård.movePiece(new Move ("h7h5"));
        bård.movePiece(new Move ("a4a5"));
        bård.movePiece(new Move ("h5h4"));
        bård.movePiece(new Move ("a5a6"));
        bård.movePiece(new Move ("h4h3"));
        bård.movePiece(new Move ("a6b7"));
        bård.movePiece(new Move ("h3g2"));
        //Etter dette kan begge promotere i neste trekk.

        assertTrue(bård.checkMoveLegality(new Move("b7a8")));

        bård.movePiece(new Move("b7a8"));

        assertTrue(bård.getScore() > 500); //Å få en dronning skal gi mange poeng
        assertEquals(new Queen(WHITE), bård.getPiece(0, 0));
        assertNull(bård.getPiece(1, 1));

        bård.movePiece(new Move("g2h1"));

        assertTrue(bård.getScore() < 100); //Nå skal begge ha fått en dronning, da er det jevnt.
        assertEquals(new Queen(BLACK), bård.getPiece(7, 7));
        assertNull(bård.getPiece(6, 6));

        bård.goBack();  //Om vi går tilbake, skal brettet være som det var før vi begynte.

        assertFalse(bård.getScore() < 100);
        assertEquals(new Rook(WHITE), bård.getPiece(7, 7));
        assertEquals(new Pawn(BLACK), bård.getPiece(6, 6));

        bård.goBack();

        assertFalse(bård.getScore() > 500);
        assertEquals(new Rook(BLACK), bård.getPiece(0, 0));
        assertEquals(new Pawn(WHITE), bård.getPiece(1, 1));

    }
    
    @Test
    void movingKingOrRookPreventsCastling(){
        //alle veiene vi kan rokere, og et trekk for hver som hindrer rokerering.
        for (Tuple<Move, Integer> move : new Tuple[]{
                new Tuple(new Move("a1a2"), 0),
                new Tuple(new Move("h1h2"), 1),
                new Tuple(new Move("a8a7"), 2),
                new Tuple(new Move("h8h7"), 3)}){
            bård.movePiece(move.getX());
            for (int i : new int[]{0, 1, 2, 3}){
                if (i == move.getY()) assertFalse(bård.getCastle()[i]);
                else assertTrue(bård.getCastle()[i]);    //Alle andre veier skal fortsatt kunne rokere, unntatt den over.
            }
            bård.goBack();
            for (int j : new int[]{0, 1, 2, 3}) assertTrue(bård.getCastle()[j]); //Om vi går tilbake igjen skal alle kunne rokerer igjen.
        }

        //Om hvit flytter kongen kan han ikke lenger rokere.
        bård.movePiece(new Move("e1e2"));

        assertFalse(bård.getCastle()[0]);
        assertFalse(bård.getCastle()[1]);
        assertTrue(bård.getCastle()[2]);
        assertTrue(bård.getCastle()[3]);

        bård.goBack();

        bård.movePiece(new Move("e8e7"));

        assertTrue(bård.getCastle()[0]);
        assertTrue(bård.getCastle()[1]);
        assertFalse(bård.getCastle()[2]);
        assertFalse(bård.getCastle()[3]);

        bård.goBack();
    }

    /** Denne funksjonen prøver å rokere, flytter vekk en brikke, prøver igjen, osv. */
    @Test
    void blockingPiecesPreventsCastling(){
        assertFalse(bård.checkMoveLegality(new Move("e1g1")));
        bård.movePiece(new Move("f1e2"));
        assertFalse(bård.checkMoveLegality(new Move("e1g1")));
        bård.movePiece(new Move("g1f3"));
        assertTrue(bård.checkMoveLegality(new Move("e1g1")));

        bård.movePiece(new Move("e1g1"));
        assertFalse(bård.checkMoveLegality(new Move("e1g1")));
        bård.goBack();
        assertTrue(bård.checkMoveLegality(new Move("e1g1")));
        bård.goBack();
        bård.goBack();


        assertFalse(bård.checkMoveLegality(new Move("e1c1")));
        bård.movePiece(new Move("b1c3"));
        assertFalse(bård.checkMoveLegality(new Move("e1c1")));
        bård.movePiece(new Move("c1d2"));
        assertFalse(bård.checkMoveLegality(new Move("e1c1")));
        bård.movePiece(new Move("d1d2"));
        bård.movePiece(new Move("e8e7"));
        assertTrue(bård.checkMoveLegality(new Move("e1c1")));

        bård.movePiece(new Move("e1c1"));
        assertFalse(bård.checkMoveLegality(new Move("e1c1")));
        assertNull(bård.getPiece(0, 7));
        assertNull(bård.getPiece(4, 7));
        assertEquals(new Rook(WHITE), bård.getPiece(3, 7));
        assertEquals(new King(WHITE), bård.getPiece(2, 7));

        bård.goBack();
        assertTrue(bård.checkMoveLegality(new Move("e1c1")));
        assertNotNull(bård.getPiece(0, 7));
        assertNotNull(bård.getPiece(4, 7));
        assertNull(bård.getPiece(3, 7));
        assertNull(bård.getPiece(2, 7));
    }

    @Test
    void checkMate(){       //Sjekker at det faktisk blir matt når det er sjakk matt.
        bård.movePiece(new Move("f2f3"));
        assertFalse(bård.checkCheckMate());
        bård.movePiece(new Move("e7e5"));
        assertFalse(bård.checkCheckMate());
        bård.movePiece(new Move("g2g4")); //Ikke spill slik i et reellt parti
        assertFalse(bård.checkCheckMate());
        bård.movePiece(new Move("d8h4"));
        assertTrue(bård.checkCheckMate());
    }

    @Test
    void checkRemis(){

        //Denne stillingen er remis om det er svart sin tur, men ikke om det er hvit sin tur.
        iPiece[][] pattgrid = new iPiece[][]{
                new iPiece[]{null, null, null, new King(BLACK), null, null, null, null},
                new iPiece[]{null, null, null, new Pawn(WHITE), null, null, null, null},
                new iPiece[]{null, null, null, new King(WHITE), null, null, null, null},
                new iPiece[]{null, null, null, null, null, null, null, null},
                new iPiece[]{null, null, null, null, null, null, null, null},
                new iPiece[]{null, null, null, null, null, null, null, null},
                new iPiece[]{null, null, null, null, null, null, null, null},
                new iPiece[]{null, null, null, null, null, null, null, null}
        };
        //Default-verdier, så board ikke kræsjer.
        Stack<Move> moves = new Stack<>();
        Stack<Integer> scores = new Stack<>();
        Stack<boolean[]> castles = new Stack<>();
        moves.push(new Move("d5d6"));
        scores.push(0);
        castles.push(new boolean[]{false, false, false, false});

        //Burde bli remis
        Board remis = new Board(0, BLACK, pattgrid, moves, scores, new Stack<>(), castles);

        //Spillet pågår fortsatt. (Om hvit spiller riktig, vinner han etterpå)
        Board ongoing = new Board(0, WHITE, pattgrid, moves, scores, new Stack<>(), castles);

        assertNull(remis.checkCheckMate());
        assertFalse(ongoing.checkCheckMate());
    }


    @Test
    void cannotPutYourselfInCheck(){
        assertTrue(bård.checkMoveLegality(new Move("e2e4")));
        bård.movePiece(new Move("e2e4"));
        assertTrue(bård.checkMoveLegality(new Move("a7a6")));
        bård.movePiece(new Move("a7a6"));
        assertTrue(bård.checkMoveLegality(new Move("d1h5")));
        bård.movePiece(new Move("d1h5"));

        //Dette ville fjernet bonden som står mellom den svarte kongen og den hvite dronningen.
        assertFalse(bård.checkMoveLegality(new Move("f7f6")));
    }

    @Test
    void cannotIgnoreACheck(){
        assertTrue(bård.checkMoveLegality(new Move("e2e4")));
        bård.movePiece(new Move("e2e4"));
        assertTrue(bård.checkMoveLegality(new Move("f7f6")));
        bård.movePiece(new Move("f7f6"));
        assertTrue(bård.checkMoveLegality(new Move("d1h5")));
        bård.movePiece(new Move("d1h5")); //Dette sjakker svart.

        //Det finnes kun ett trekk, g7g6, som forsvarer kongen her.
        assertEquals(1, bård.getLegalMoves().size());

    }

    @Test
    void simulateAndSeeIfItCrashesOrNot(){
        //Denne metoden tar også tiden, fordi det er kjekt å vite.
        long starttime = System.nanoTime();

        int leafs = 0;
        int nodes = 0;
        for (Move move : bård.getLegalMoves()){
            bård.movePiece(move);
            nodes++;
            for (Move counter : bård.getLegalMoves()){
                bård.movePiece(counter);
                nodes++;
                for (Move move2 : bård.getLegalMoves()){
                    bård.movePiece(move2);
                    nodes++;
                    for (Move counter2 : bård.getLegalMoves()){
                        bård.movePiece(counter2);
                        leafs++;
                        bård.goBack();
                    }
                    bård.goBack();
                }
                bård.goBack();
            }
            bård.goBack();
        }


        long endtime = System.nanoTime();
        System.out.println("Nodes: " + nodes);
        System.out.println("Leafs: " + leafs);
        System.out.println("time spent: " + (endtime-starttime) / 1_000_000 + "ms");
    }
}
