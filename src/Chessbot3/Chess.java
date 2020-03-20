package Chessbot3;

import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Dictionary;
import java.util.Hashtable;

import Chessbot3.GameBoard.Board;
import Pieces.iPiece;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Chess {

    //Dict over hvilke retninger hver brikke kan gå.
    public static final Dictionary<Character, Tuple[]> direcDict = Generator.makeDirections();

    //Dict over bildet som hører til hver enkelt brikke.
    public static final Hashtable<String, BufferedImage> imageDict = Generator.makeImages();

    //Selve vinduet som vises på skjermen.
    public static JPanel chessBoard;

    //En nøstet liste over alle rutene på brettet.
    //Disse må være statiske, så Action kan referere til dem når noen gjør et trekk.
    public static JButton[][] chessBoardSquares = new JButton[8][8];

    //Selve partiet.
    public static Game game;

    //Knappene og tekstfeltet som vises på skjermen.
    //Disse må være statiske, slik at Action kan referere til dem.
    public static JButton enter = new JButton("Enter");
    public static JButton back = new JButton("Go Back");
    public static JButton neww = new JButton("New Game");
    public static JButton quit = new JButton("Quit Game");
    public static JTextField textField = new JTextField(20);

    public static void main(String[] args) {
        //Opprettet GUI, og alt som skal til for å spille.
        //Når main er ferdig er det ingen kode som kjører, kun actionListeners i Action som venter på at du skal trykke noe.
        //Hver gang spilleren gjør et trekk aktiverer det botten til den har gjort et trekk,
        //så er det ingen kode som kjører lenger, helt til spilleren gjør et nytt trekk.

        game = new Game();
        JFrame frame = new JFrame("Chessbot3");
        frame.add(initializeGUI());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        paintPieces();
        frame.setVisible(true);
    }
    public static void paintPieces(){
        /* Tegner alle brikkene på brettet, helt fra scratch.
         */
        iPiece[][] grid = game.getCurrentBoard().GetGrid();
        for(int x=0; x<8; x++){
            for(int y=0; y<8; y++){
                if(grid[x][y] != null) {
                    ImageIcon newIcon = new ImageIcon();
                    newIcon.setImage(grid[x][y].getImage());
                    chessBoardSquares[x][y].setIcon(newIcon);
                }else chessBoardSquares[x][y].setIcon(new ImageIcon());
            }
        }
    }
    public static void repaintPiece(Tuple<Integer, Integer> tuple){
        //Tegner en bestemt rute på nytt.
        //Denne må kalles opp for hver rute som blir rørt når noen gjør et trekk.
        Integer x = tuple.getX();
        Integer y = tuple.getY();
        iPiece[][] grid = game.getCurrentBoard().GetGrid();
        if(grid[x][y] != null) {
            ImageIcon icon = (ImageIcon) chessBoardSquares[x][y].getIcon();
            icon.setImage(grid[x][y].getImage());
        }else chessBoardSquares[x][y].setIcon(new ImageIcon());
    }

    private static JPanel initializeGUI() {
        //Lager selve vinduet i GUI-en.
        //Den oppretter alle knappene, tekstfeltet, samt rutene som brikkene skal stå på.
        JPanel gui = new JPanel(new BorderLayout(3, 3));
        gui.setBorder(new EmptyBorder(1, 1, 1, 1));

        JToolBar toolbar2 = new JToolBar();
        toolbar2.add(quit);
        toolbar2.add(neww);
        toolbar2.add(back);
        quit.addActionListener(new Action());
        back.addActionListener(new Action());
        neww.addActionListener(new Action());
        gui.add(toolbar2, BorderLayout.PAGE_START);

        JToolBar toolbar = new JToolBar();
        gui.add(toolbar, BorderLayout.PAGE_END);

        JTextField text = new JTextField(20);
        textField = text; //Tekstfeltet støtter kun juksekoder, en komplett liste finnes i Action.enter().
        textField.addKeyListener(new Action());
        toolbar.add(text);
        toolbar.add(enter);
        enter.addActionListener(new Action());

        chessBoard = new JPanel(new GridLayout(0, 8));
        chessBoard.setBorder(new LineBorder(Color.BLACK));
        makeButtons(); //Legger til knapper oppå chessBoard
        gui.add(chessBoard);

        return gui;
    }

    private static void makeButtons() {
        //Skaper alle rutene som brikkene skal stå på. Disse rutene er egentlig knapper.
        //Disse knappene får alle sammen et blankt ikon, som paintPieces() og repaintPiece tegner oppå.

        Insets buttonMargin = new Insets(0,0,0,0);
        for (int ii = 0; ii < chessBoardSquares.length; ii++) {
            for (int jj = 0; jj < chessBoardSquares[ii].length; jj++) {

                JButton butt = new JButton();
                butt.setMargin(buttonMargin);
                ImageIcon icon = new ImageIcon(new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB));
                butt.setIcon(icon);
                if ((jj % 2 == 1 && ii % 2 == 1) || (jj % 2 == 0 && ii % 2 == 0)) butt.setBackground(Color.LIGHT_GRAY);
                else butt.setBackground(Color.gray);
                butt.addActionListener(new Action());
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
