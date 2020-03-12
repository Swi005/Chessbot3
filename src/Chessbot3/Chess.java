package Chessbot3;

import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Dictionary;
import java.util.Hashtable;

import Chessbot3.GameBoard.Board;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Chess {

    public static final Dictionary<Character, Tuple[]> direcDict = Generator.makeDirections();
    public static final Hashtable<String, BufferedImage> imageDict = Generator.makeImages();
    public static JButton enter = new JButton("Enter");
    public static JButton back = new JButton("Go Back");
    public static JButton neww = new JButton("New Game");
    public static JButton quit = new JButton("Quit Game");
    public static JPanel chessBoard;
    public static JTextField textField;

    public static void main(String[] args) {
        Board gameBoard = new Board();
        JFrame frame = new JFrame("Chessbot3");
        frame.add(initializeGUI());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setVisible(true);

        //TODO: Do stuff with the board
    }

    private static JPanel initializeGUI() {
        JPanel gui = new JPanel(new BorderLayout(3, 3));
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));

        JToolBar toolbar2 = new JToolBar();
        toolbar2.add(quit);
        toolbar2.add(neww);
        toolbar2.add(back);
        gui.add(toolbar2, BorderLayout.PAGE_START);

        JToolBar toolbar = new JToolBar();
        gui.add(toolbar, BorderLayout.PAGE_END);

        JTextField text = new JTextField(20);
        textField = text;
        toolbar.add(text);
        toolbar.add(enter);

        chessBoard = new JPanel(new GridLayout(0, 8));
        chessBoard.setBorder(new LineBorder(Color.BLACK));

        gui.add(chessBoard);

        return gui;
    }
}
