package Chessbot3;

import java.util.Dictionary;
import java.util.HashMap;

import Chessbot3.GameBoard.Board;

public class Chess {

    public static final Dictionary<Character, Tuple[]> moveDict = Generator.makeDirections();

    public static void main(String[] args) {
        Board gameBoard = new Board();
        //TODO: Do stuff with the board
    }
}
