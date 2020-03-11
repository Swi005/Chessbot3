package Chessbot3;

import java.awt.image.BufferedImage;
import java.util.Dictionary;
import java.util.Hashtable;

import Chessbot3.GameBoard.Board;

public class Chess {

    public static final Dictionary<Character, Tuple[]> moveDict = Generator.makeDirections();
    public static final Hashtable<String, BufferedImage> imageDict = Generator.makeImages();

    public static void main(String[] args) {
        Board gameBoard = new Board();
        //TODO: Do stuff with the board
    }
}
