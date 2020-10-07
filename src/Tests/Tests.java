package Tests;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.PieceResources.iPiece;
import Chessbot3.Pieces.Types.Pawn;
import Chessbot3.Pieces.Types.Rook;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import static Chessbot3.Pieces.PieceResources.WhiteBlack.BLACK;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;
import static Chessbot3.Simulators.AlphaBota.alphaSetup;
import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    @Test
    void testBoardEquals(){
        Board bård1 = new Board();
        Board bård2 = new Board();
        assertTrue(bård1.equals(bård2));

        bård1.movePiece(new Move(new Tuple<>(0, 1), new Tuple<>(0, 2)));
        bård2.movePiece(new Move(new Tuple<>(0, 7), new Tuple<>(0, 6)));
        assertFalse(bård1.equals(bård2));

        bård1.movePiece(new Move(new Tuple<>(0, 7), new Tuple<>(0, 6)));
        bård2.movePiece(new Move(new Tuple<>(0, 1), new Tuple<>(0, 2)));
        assertTrue(bård1.equals(bård2));
    }

    @Test
    void testHashMapSize(){
        HashMap<Board, Integer> map = new HashMap<>();
        Board bård1 = new Board();
        Board bård2 = new Board();
        map.put(bård1, 0);
        map.put(bård2, 0);
        assertEquals(1, map.size());

        map.clear();
        bård1.movePiece(new Move(new Tuple<>(0, 1), new Tuple<>(0, 2)));
        bård2.movePiece(new Move(new Tuple<>(0, 7), new Tuple<>(0, 6)));
        map.put(bård1, 0);
        map.put(bård2, 0);
        assertEquals(2, map.size());

        map.clear();
        bård1.movePiece(new Move(new Tuple<>(0, 7), new Tuple<>(0, 6)));
        bård2.movePiece(new Move(new Tuple<>(0, 1), new Tuple<>(0, 2)));
        map.put(bård1, 0);
        map.put(bård2, 0);
        assertEquals(1, map.size());

    }

    @Test
    void testPawnEquals(){
        iPiece pawn = new Pawn(WHITE);
        iPiece rook = new Rook(BLACK);

        assertNotEquals(rook, pawn); //Feil type

        iPiece pawn2 = new Pawn(BLACK);
        assertNotEquals(pawn2, pawn); //Feil farge

        iPiece pawn3 = new Pawn(WHITE);
        assertEquals(pawn3, pawn);
    }

    @Test
    void testHashMapDuplicateBoards(){
        HashMap<Board, Integer> map = new HashMap();
        Board bård1 = new Board();
        bård1.movePiece(new Move(new Tuple<>(0, 1), new Tuple<>(0, 2)));
        map.put(bård1, 10);

        Board bård2 = new Board();
        assertFalse(map.containsKey(bård2));

        bård2.movePiece(new Move(new Tuple<>(0, 1), new Tuple<>(0, 2)));
        System.out.println(bård1.hashCode());
        System.out.println(bård2.hashCode());

        assertTrue(map.containsKey(bård2)); // TODO: 04.10.2020 Fiks hashing, slik at denne blir true 
    }

    @Test
    void testHashing(){
        int[][] grid1 = {{1, 2}, {3, 4}};
        int[][] grid2 = {{1, 2}, {3, 4}};
        int[][] grid3 = {{1, 2}, {3, 4}};

        System.out.println(grid1.hashCode());
        System.out.println(Objects.hash(grid1));

        System.out.println(grid2.hashCode());
        System.out.println(Objects.hash(grid2));

        System.out.println(grid3.hashCode());
        System.out.println(Objects.hash(grid3));

        System.out.println(Objects.hash(WHITE.toString()));
        System.out.println(Objects.hash(WHITE.toString()));

        System.out.println(Arrays.deepHashCode(grid1));
        System.out.println(Arrays.deepHashCode(grid2));
    }
    
    @Test void hashMapHandlesUniques(){
        ArrayList<Board> boards = new ArrayList();
        Board bård1 = new Board();
        for(Move move : bård1.genMoves()){
            Board b = new Board();
            b.movePiece(move);
            boards.add(b);
            for(Move m : b.genMoves()){
                Board n = new Board();
                n.movePiece(move);
                n.movePiece(m);
                boards.add(n);
            }
        }
        System.out.println(boards.size());
        HashMap<Board, Integer> futures = new HashMap<>();
        for(Board bård : boards){
            assertFalse(futures.containsKey(bård));
            futures.put(bård, bård.getScore());
        }
    }

    @Test
    void testShannonsNumber(){
        Board bård = new Board();
        alphaSetup(bård, 1);
        alphaSetup(bård, 2);
        alphaSetup(bård, 3);
        alphaSetup(bård, 4);

    }

    @Test
    void moveTest(){
        Move move =  new Move(new Tuple(0, 1), new Tuple<>(0, 2));
        assertEquals("a7:a6", move.toAlgebraicNotation());
    }
}
