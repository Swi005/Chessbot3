package Tests;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.Types.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
        // TODO: 20.01.2021  
    }
    
    @Test
    void castling(){
        // TODO: 20.01.2021
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
        System.out.println("time spent: " + (endtime-starttime) / 1_000_000_000 + "ms");
    }
}
