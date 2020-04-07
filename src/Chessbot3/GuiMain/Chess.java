package Chessbot3.GuiMain;

import java.awt.image.BufferedImage;
import java.util.Dictionary;
import java.util.Hashtable;

import Chessbot3.MiscResources.Generator;
import Chessbot3.MiscResources.Tuple;

import static Chessbot3.GuiMain.Gui2.game;

//import static Chessbot3.GuiMain.Gui.game;

public class Chess {

    //Dict over hvilke retninger hver brikke kan gå.
    public static final Dictionary<Character, Tuple[]> direcDict = Generator.makeDirections();

    //Dict over bildet som hører til hver enkelt brikke.
    public static final Hashtable<String, BufferedImage> imageDict = Generator.makeImages();

    //Dict over hvor det generelt er bra å stå for de forskjellige brikkene.
    public static final Dictionary<Character, Integer[][]> posValueDict = Generator.makePosValueDict();

    //Selve Gui-en.
    public static Gui2 gui;

    public static void main(String[] args) {
        //Oppretter Gui, og alt som skal til for å spille.
        //Om det er botten sin tur startes den også.
        //Ellers er det ingen kode som kjører, kun actionListeners som venter på at brukeren skal trykke på knapper.
        gui = new Gui2();
        if(game.isBotTurn()) game.botMove(); //Starter botten om det er dens tur.
        //while(game.isBotTurn()) game.botMove();

    }
}
