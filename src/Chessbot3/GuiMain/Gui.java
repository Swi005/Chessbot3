package Chessbot3.GuiMain;

import Chessbot3.GameBoard.Board;
import Chessbot3.GameBoard.Game;
import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.PieceResources.WhiteBlack;
import Chessbot3.Pieces.PieceResources.iPiece;
import Chessbot3.Pieces.Types.Bishop;
import Chessbot3.Pieces.Types.Knight;
import Chessbot3.Pieces.Types.Queen;
import Chessbot3.Pieces.Types.Rook;
import Chessbot3.SaveSystem.SaveController;
import Chessbot3.sPGN.Ispgn;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static Chessbot3.Pieces.PieceResources.WhiteBlack.BLACK;
import static Chessbot3.Pieces.PieceResources.WhiteBlack.WHITE;

public class Gui extends JFrame {

    //En nøstet liste over alle rutene på brettet.
    //Disse må være statiske, så Action kan referere til dem når noen gjør et trekk.
    protected static JButton[][] chessBoardSquares = new JButton[8][8];

    protected static JDialog menu;

    //Selve partiet.
    public static Game game = new Game();

    //Om brettet er rotert eller ikke.
    protected static Boolean reverse = false;

    //Om tekstfeltet har noen feilmeldinger i seg nå eller ikke.
    private static Boolean errorInChat = false;

    //listen over ruter som er lyst opp akkurat nå.
    //Denne blir oppdatert av lightUpButtons() og makeButtonsGrey().
    private static ArrayList<Tuple<Integer, Integer>> litSquares = new ArrayList();

    //Fargene på alle rutene.
    private static final Color darkSquareColor = Color.DARK_GRAY;
    private static final Color lightSquareColor = Color.GRAY;
    private static final Color litUpColor = Color.LIGHT_GRAY; //farger på ruter som blir opplyst av mulige trekk.

    //Knappene og tekstfeltet som vises på skjermen.
    //Disse må være statiske, slik at Action kan referere til dem når noen trykker på dem.
    protected static JButton enter = new JButton("Enter");
    protected static JButton back = new JButton("Go Back");
    protected static JButton forward = new JButton("Go Forward");
    protected static JButton neww = new JButton("New Game");
    protected static JButton quit = new JButton("Quit Game");
    protected static JButton openMenu = new JButton("Menu");
    protected static JButton save = new JButton("Save Game");
    protected static JButton load = new JButton("Load Game");

    //Tekstfeltet.
    private JTextField textField = new JTextField(20);

    public Gui(){
        //Gui er egentlig en ramme.
        //Oppå denne rammen skal vi legge til alt det andre.
        File test3 = new File("src\\Chessbot3\\files\\test3.txt");
        setTitle("Chessbot3");
        add(initializeGUI()); //Her legger vi til alle knappene og rutenettet.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        pack();
        setMinimumSize(getSize());
        paintPieces(game.getCurrentBoard());
        setVisible(true);
        chooseGamemode();
    }

    private JPanel initializeGUI() {
        //Lager selve vinduet i Gui-en.
        //Den oppretter alle knappene, tekstfeltet, samt rutene som brikkene skal stå på.
        JPanel panel = new JPanel(new BorderLayout(3, 3));
        panel.setBorder(new EmptyBorder(1, 1, 1, 1));
        panel.add(makeTopToolbar(), BorderLayout.PAGE_START);
        panel.add(makeButtons());
        panel.add(makeTextField(), BorderLayout.PAGE_END);
        menu = makeMenu();

        return panel;
    }
    private JToolBar makeTextField(){
        //Oppretter tekstfeltet nederst.
        JToolBar ret = new JToolBar();
        JTextField text = new JTextField(20);
        textField = text; //Tekstfeltet støtter juksekoder, en komplett liste finnes i Action.enter().
        textField.addKeyListener(new Action());
        ret.add(text);
        ret.add(enter);
        enter.addActionListener(new Action());
        return ret;
    }

    private JToolBar makeTopToolbar(){
        //Oppretter toolbaren øverst på skjermen, den med alle knappene.
        JToolBar ret = new JToolBar();
        ret.add(openMenu);
        ret.addSeparator();
        ret.add(back);
        ret.add(forward);
        openMenu.addActionListener(new Action());
        back.addActionListener(new Action());
        forward.addActionListener(new Action());
        return ret;
    }

    private JPanel makeButtons() {
        //Skaper alle rutene som brikkene skal stå på. Disse rutene er egentlig knapper.
        //Disse knappene får alle sammen et blankt ikon, som paintPieces() og repaintPiece tegner oppå.

        JPanel ret = new JPanel(new GridLayout(0, 8));
        ret.setBorder(new LineBorder(Color.BLACK));
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
                ret.add(chessBoardSquares[jj][ii]);
            }
        }
        return ret;
    }

    private JDialog makeMenu(){
        JDialog ret = new JDialog();
        ret.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        ret.setLayout(new GridLayout(4, 1));
        ret.add(quit);
        ret.add(neww);
        ret.add(save);
        ret.add(load);
        quit.addActionListener(new Action());
        neww.addActionListener(new Action());
        save.addActionListener(new Action());
        load.addActionListener(new Action());
        ret.setMinimumSize(new Dimension(20, 200));
        ret.setLocationRelativeTo(null);
        ret.setVisible(false);
        return ret;
    }

    //Sjekker om det er noen feilmeldinger i tekstfeltet, og fjerner dem.
    public void removeErrorsFromTextField(){ if(errorInChat) clearTextField(); }

    public void clearTextField(){
        //Klarerer tekstfeltet.
        textField.setText("");
        errorInChat = false;
    }

    protected String getTextField() {
        //Fjerner og returnerer det som står i tekstfeltet.
        String ret = textField.getText();
        clearTextField();
        return ret;
    }

    //Sender en streng til tekstfeltet.
    //Denne er for mindre alvorlige meldinger, som ikke trenger et popup.
    public void displayTextFieldMessage(String s) {
        textField.setText(s);
        errorInChat = true;
    }

    //Lager et popup-felt med valgfri melding.
    public void displayPopupMessage(String s){ JOptionPane.showMessageDialog(this, s); }

    public void lightUpButtons(Tuple initpos) throws IllegalArgumentException{
        //Tar en posisjon, finner brikken som står der, og lyser opp alle de lovlige trekkene den brikken kan gjøre.
        //Forutsetter at det faktisk står en brikke der, gir feilmelding ellers.
        Board bård = game.getCurrentBoard();
        iPiece pie = bård.getPiece(initpos);
        if(pie == null) throw new IllegalArgumentException("The piece is null, and has noe legal moves");
        for(Move move : pie.getMoves(bård)){
            if(bård.checkMoveLegality(move)) {
                int x;
                int y;
                if (!reverse) {
                    x = move.getTo().getX();
                    y = move.getTo().getY();
                } else {
                    x = 7 - move.getTo().getX();
                    y = 7 - move.getTo().getY();
                }
                chessBoardSquares[x][y].setBackground(litUpColor);
                litSquares.add(new Tuple(x, y));
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

        //Klarerer listen over farger som botten skal styre, så den kan legge til riktige farger på nytt.
        game.clearBotColors();

        //Selve popup-vinduet. Endre denne på eget ansvar!
        int n = JOptionPane.showOptionDialog(this, "Please pick a gamemode.", "Gamemode", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, new String[]{"Player vs Player", "Player vs Bot", "Bot vs Bot"}, 0);

        //n er indeksen til hvilken knapp brukeren trykket på. Dette legger til farger som botten skal styre.
        // F. eks om du klikket Bot vs Bot blir både svart og hvit lagt til, og botten vil automatisk gjøre trekk for begge fargene.

        if(n == 1){
            //Om spilleren vil spille mot en bot må han få lov til å velge hvilken farge han vil spille som.
            int m = JOptionPane.showOptionDialog(this, "Please pick a side.", "Gamemode", JOptionPane.YES_NO_CANCEL_OPTION,
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
        //Om to mennekser skal spille mot hverandre. Da skal ikke botten ha noen farger.
        game.updateStop();
    }

    public void reverse() {
        //Reverserer alt det visuelle på brettet.
        //Knappene er fortsatt på samme plass, men de får nye bilder.
        //Dette blir tatt hensyn til i findSquare() i Action.
        reverse = !reverse;
        paintPieces(game.getCurrentBoard());
    }
    public void reset(){
        //Resetter orienteringen, slik at hvit er nederst, og maler brikkene på nytt.
        reverse = false;
        paintPieces(game.getCurrentBoard());
    }

    public void paintPieces(Board bård){
        //Tegner alle brikkene på brettet, helt fra scratch.
        //Board bård = game.getCurrentBoard();
        for(int x=0; x<8; x++){
            for(int y=0; y<8; y++){
                JButton butt;
                if(!reverse) butt = chessBoardSquares[x][y];
                else butt = chessBoardSquares[7-x][7-y];

                if(bård.getPiece(x, y) != null) {
                    ImageIcon newIcon = new ImageIcon(); //Oppretter et nytt ikon
                    newIcon.setImage(bård.getPiece(x, y).getImage()); //Legger til et bilde på ikonet, hentet fra iPiece.getImage()
                    butt.setIcon(newIcon);
                }
                //Legger til et nytt og blankt ikon.
                //Det gir samme effekt som at det ikke er noe ikon der i det hele tatt.
                else butt.setIcon(new ImageIcon());
            }
        }
    }
    public iPiece promotePawn(Tuple pos){
        //Lager et popup-vindu og spør hvilken brikke en spiller vil promotere til, og returnerer den brikken.
        //Tar utgangspunkt i at alle vil ha en dronning uansett.
        WhiteBlack color = game.getCurrentBoard().getColorToMove();
        int n = JOptionPane.showOptionDialog(this, "Please pick a piece to promote to.", "Promotion", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, new String[]{"Queen", "Rook", "Knight", "Bishop"}, 0);
        if(n == 1) return new Rook(color, pos);
        else if(n == 2) return new Knight(color, pos);
        else if(n == 3) return new Bishop(color, pos);
        else return new Queen(color, pos); //Standardverdi.
    }

    public void openMenu() {
        //Åpner menyen.
        menu.setVisible(true);
    }

    public void closeMenu(){
        //Lukker menyen.
        menu.setVisible(false);
    }

    public void loadGame(){
        //game = new Game(new File("src\\Chessbot3\\files\\test.spgn"));
        try {
            SaveController controller = new SaveController();
            Ispgn[] savedgames = controller.getAllSaves();
            if(savedgames.length == 0){
                displayPopupMessage("Found no saved games.");
                return;
            }
            Ispgn ispgn = (Ispgn) JOptionPane.showInputDialog(this, "Choose save", "Menu", JOptionPane.PLAIN_MESSAGE, null, savedgames, savedgames[0]);
            game = controller.convertToGame(ispgn);
            // TODO: 11.10.2020 Gjør noe for å legge til riktige farger for botten
            game.updateStop();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    public void saveGame(){
        String name = JOptionPane.showInputDialog("What do you want to save the game as?");
        if (name == null) return;
        while(!checkName(name)) {
            displayPopupMessage("Please use a valid name.");
            name = JOptionPane.showInputDialog("What do you want to save the game as?");
            if(name == null) return;
        }
        game.saveGame(name);
    }

    private boolean checkName(String name){
        if(name.length() == 0){
            System.out.println("too long");
            return false;
        }
        for(char c : name.toCharArray()){
            for(char k : new char[]{'/', '\\', '.', ':'}){
                if (c == k) return false;
            }
        }
        return true;
    }

}