package Chessbot3;

import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Dictionary;
import java.util.Hashtable;

import Chessbot3.GameBoard.Board;
import Pieces.Pawn;
import Pieces.iPiece;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import static Chessbot3.Color.WHITE;

public class Chess {

    public static final Dictionary<Character, Tuple[]> direcDict = Generator.makeDirections();
    public static final Hashtable<String, BufferedImage> imageDict = Generator.makeImages();
    public static JPanel chessBoard;
    public static JTextField textField;
    public static JButton[][] chessBoardSquares = new JButton[8][8];
    public static Game game;

    public static void main(String[] args) {
        game = new Game();
        JFrame frame = new JFrame("Chessbot3");
        frame.add(initializeGUI());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        paintPieces();
        frame.setVisible(true);

        //TODO: Do stuff with the board
    }
    public static void paintPieces(){
        Board bård = game.getCurrentBoard();
        iPiece[][] grid = bård.GetGrid();
        for(int x=0; x<8; x++){
            for(int y=0; y<8; y++){
                if(grid[x][y] != null) {
                    if (grid[x][y] instanceof Pawn) {
                        //ImageIcon icon = new ImageIcon(new BufferedImage(55, 55, BufferedImage.TYPE_INT_ARGB));
                        ImageIcon icon = (ImageIcon) chessBoardSquares[x][y].getIcon();
                        icon.setImage(imageDict.get("pawn_white"));


                    }
                }
            }
        }
    }

    private static JPanel initializeGUI() {
        JPanel gui = new JPanel(new BorderLayout(3, 3));
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));

        JButton enter = new JButton("Enter");
        JButton back = new JButton("Go Back");
        JButton neww = new JButton("New Game"); // TODO: 12.03.2020 Sørg for at knapper gjør noe.
        JButton quit = new JButton("Quit Game");

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
        makeButtons(); //Legger til knapper oppå chessBoard
        gui.add(chessBoard);

        return gui;
    }

    private static void makeButtons() {
        Insets buttonMargin = new Insets(0,0,0,0);
        for (int ii = 0; ii < chessBoardSquares.length; ii++) {
            for (int jj = 0; jj < chessBoardSquares[ii].length; jj++) {

                JButton butt = new JButton();
                butt.setMargin(buttonMargin);
                ImageIcon icon = new ImageIcon(new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB));
                butt.setIcon(icon);
                if ((jj % 2 == 1 && ii % 2 == 1) || (jj % 2 == 0 && ii % 2 == 0)) butt.setBackground(Color.LIGHT_GRAY);
                else butt.setBackground(Color.gray);
                //butt.addActionListener(new Action()); // TODO: 12.03.2020 Legg til at knapper gjør noe
                chessBoardSquares[jj][ii] = butt;
            }
        }
        for (int ii = 0; ii < 8; ii++) {
            for (int jj = 0; jj < 8; jj++) {
                chessBoard.add(chessBoardSquares[jj][ii]);
            }
        }
    }
}
