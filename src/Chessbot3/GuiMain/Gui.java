package Chessbot3.GuiMain;

import Chessbot3.GameBoard.Board;
import Chessbot3.GameBoard.Game;
import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static Chessbot3.Pieces.WhiteBlack.BLACK;
import static Chessbot3.Pieces.WhiteBlack.WHITE;

public class Gui {

    //Selve vinduet som vises på skjermen.
    public static JPanel chessBoard;

    //En nøstet liste over alle rutene på brettet.
    //Disse må være statiske, så Action kan referere til dem når noen gjør et trekk.
    public static JButton[][] chessBoardSquares = new JButton[8][8];

    //Selve partiet.
    public static Game game;

    //Ombrettet er rotert eller ikke.
    public static Boolean reverse = false;

    //listen over ruter som er lyst opp akkurat nå.
    //Denne blir oppdatert av lightUpButtons() og makeButtonsGrey().
    private static ArrayList<Tuple<Integer, Integer>> litSquares = new ArrayList();

    private static Color darkSquareColor = Color.DARK_GRAY;
    private static Color lightSquareColor = Color.GRAY;
    private static Color litUpColor = Color.LIGHT_GRAY;

    //Knappene og tekstfeltet som vises på skjermen.
    //Disse må være statiske, slik at Action kan referere til dem når noen trykker på dem.
    public static JButton enter = new JButton("Enter");
    public static JButton back = new JButton("Go Back");
    public static JButton neww = new JButton("New Game");
    public static JButton quit = new JButton("Quit Game");
    public static JTextField textField = new JTextField(20);

    public Gui(){
        game = new Game();
        JFrame frame = new JFrame("Chessbot3");
        frame.add(initializeGUI());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        paintPieces();
        frame.setVisible(true);
        chooseGamemode();
    }

    private JPanel initializeGUI() {
        //Lager selve vinduet i Gui-en.
        //Den oppretter alle knappene, tekstfeltet, samt rutene som brikkene skal stå på.
        JPanel gui = new JPanel(new BorderLayout(3, 3));
        gui.setBorder(new EmptyBorder(1, 1, 1, 1));

        JToolBar toolbar2 = new JToolBar();
        toolbar2.add(quit);
        toolbar2.add(neww);
        toolbar2.add(back);
        quit.addActionListener(new Chessbot3.GuiMain.Action());
        back.addActionListener(new Chessbot3.GuiMain.Action());
        neww.addActionListener(new Chessbot3.GuiMain.Action());
        gui.add(toolbar2, BorderLayout.PAGE_START);

        JToolBar toolbar = new JToolBar();
        gui.add(toolbar, BorderLayout.PAGE_END);

        JTextField text = new JTextField(20);
        textField = text; //Tekstfeltet støtter kun juksekoder, en komplett liste finnes i Action.enter().
        textField.addKeyListener(new Chessbot3.GuiMain.Action());
        toolbar.add(text);
        toolbar.add(enter);
        enter.addActionListener(new Chessbot3.GuiMain.Action());

        chessBoard = new JPanel(new GridLayout(0, 8));
        chessBoard.setBorder(new LineBorder(Color.BLACK));
        makeButtons(); //Legger til knapper oppå chessBoard
        gui.add(chessBoard);

        return gui;
    }
    private void makeButtons() {
        //Skaper alle rutene som brikkene skal stå på. Disse rutene er egentlig knapper.
        //Disse knappene får alle sammen et blankt ikon, som paintPieces() og repaintPiece tegner oppå.

        Insets buttonMargin = new Insets(0,0,0,0);
        for (int ii = 0; ii < chessBoardSquares.length; ii++) {
            for (int jj = 0; jj < chessBoardSquares[ii].length; jj++) {

                JButton butt = new JButton();
                butt.setMargin(buttonMargin);
                ImageIcon icon = new ImageIcon(new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB));
                butt.setIcon(icon);
                if ((jj % 2 == 1 && ii % 2 == 1) || (jj % 2 == 0 && ii % 2 == 0)) butt.setBackground(lightSquareColor);
                else butt.setBackground(darkSquareColor);
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
    public void lightUpButtons(Tuple initpos){
        //Tar en brikke, finner alle rutene den kan gå til, og lyser dem opp.
        Board bård = game.getCurrentBoard();
        iPiece pie = bård.GetPiece(initpos);
        List<Move> legals = bård.GenCompletelyLegalMoves();
        for(Move move : legals){
            if(bård.GetPiece(move.getX()) == pie) {
                int X = move.getY().getX();
                int Y = move.getY().getY();
                if(!reverse) {
                    chessBoardSquares[X][Y].setBackground(litUpColor);
                    litSquares.add(new Tuple(X, Y));
                }else{
                    chessBoardSquares[7-X][7-Y].setBackground(litUpColor);
                    litSquares.add(new Tuple(7-X, 7-Y));
                }
            }
        }
    }
    public void makeButtonsGrey(){
        //Gjør alle ruter grå igjen. Denne må kalles opp før lightUpButtons, så bare de riktige knappene lyses opp.
        for(Tuple<Integer, Integer> pos : litSquares){
            int x = pos. getX();
            int y = pos.getY();
            if((y % 2 == 1 && x % 2 == 1) || (y % 2 == 0 && x % 2 == 0)) chessBoardSquares[x][y].setBackground(lightSquareColor);
            else chessBoardSquares[x][y].setBackground(darkSquareColor);
        }
    }
    public void chooseGamemode(){
        //Velger hvilken spillmodus som skal brukes. Om spilleren ikke velger noe blir det PvP.

        //Klarerer listen over farger som botten skal styre.
        game.clearBotColors();

        //Selve popup-vinduet. Endre denne på eget ansvar!
        Object[] options = {"Player vs Player", "Player vs Bot", "Bot vs Bot"};
        int n = JOptionPane.showOptionDialog(chessBoard, "Please choose a gamemode.", "Gamemode", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

        //n er indeksen til hvilken knapp brukeren trykket på. Dette legger til farger som botten skal styre.
        // F. eks om du klikket Bot vs Bot blir både svart og hvit lagt til, og botten vil automatisk gjøre trekk for begge fargene.

        //Om spilleren vil spille mot botten, da skal han få velge farge.
        if(n == 1){

            //Om spilleren vil spille mot en bot må han få lov til å velge hvilken farge han vil spille som.
            int m = JOptionPane.showOptionDialog(chessBoard, "Please pick a side.", "Gamemode", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, new String[]{"White", "Black"}, 0);
            if(m == 1){
                //Om spilleren vil være svart.
                game.addBotColor(WHITE);
                reverse();
            }
            //Om spilleren vil være hvit.
            else game.addBotColor(BLACK);
        }
        //Om botten skal spille mot seg selv.
        else if(n == 2){
            game.addBotColor(BLACK);
            game.addBotColor(WHITE);
        }
    }

    public iPiece promotePawn(){
        WhiteBlack color = game.getCurrentBoard().GetColorToMove();
        int n = JOptionPane.showOptionDialog(chessBoard, "Please pick a piece to promote to.", "Promotion", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, new String[]{"Queen", "Rook", "Knight", "Bishop"}, 0);
        if(n == 1) return new Rook(color);
        else if(n == 2) return new Knight(color);
        else if(n == 3) return new Bishop(color);
        else return new Queen(color);
    }

    public void displayMessage(String s) {
        // TODO: 26.03.2020 Finn ut av hvordan meldinger fint kan vises til skjermen.
        System.out.println(s); //Placeholder
    }

    public void reverse() {
        //Reverserer alt det visuelle på brettet.
        //Knappene er fortsatt på samme plass, men de får nye bilder.
        //Dette blir tatt hensyn til i findSquare() i Action.
        reverse = !reverse;
        paintPieces();
    }
    public void reset(){
        //Resetter orienteringen, slik at hvit er nederst, og maler brikkene på nytt.
        reverse = false;
        paintPieces();
    }

    public void paintPieces(){
        //Tegner alle brikkene på brettet, helt fra scratch.
        Board bård = game.getCurrentBoard();
        for(int x=0; x<8; x++){
            for(int y=0; y<8; y++){
                if(bård.GetPiece(x, y) != null) {
                    ImageIcon newIcon = new ImageIcon();
                    newIcon.setImage(bård.GetPiece(x, y).getImage());

                    //Reverse er kun om brettet skal være opp-ned, med svart nederst.
                    if(!reverse) chessBoardSquares[x][y].setIcon(newIcon);
                    else chessBoardSquares[7-x][7-y].setIcon(newIcon);
                }
                else if(!reverse) chessBoardSquares[x][y].setIcon(new ImageIcon());
                else chessBoardSquares[7-x][7-y].setIcon(new ImageIcon());
            }
        }
    }
}
