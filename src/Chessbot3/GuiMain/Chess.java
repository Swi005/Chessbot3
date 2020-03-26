package Chessbot3.GuiMain;

import java.awt.image.BufferedImage;
import java.util.Dictionary;
import java.util.Hashtable;

import Chessbot3.MiscResources.Generator;
import Chessbot3.MiscResources.Tuple;

import static Chessbot3.GuiMain.Gui.game;

public class Chess {

    //Dict over hvilke retninger hver brikke kan gå.
    public static final Dictionary<Character, Tuple[]> direcDict = Generator.makeDirections();

    //Dict over bildet som hører til hver enkelt brikke.
    public static final Hashtable<String, BufferedImage> imageDict = Generator.makeImages();

    //Selve Gui-en.
    public static Gui gui;

    public static void main(String[] args) {
        //Opprettet Gui, og alt som skal til for å spille.
        //Om det er botten sin tur startes den også.
        //Ellers er det ingen kode som kjører, kun actionListeners som venter på at brukeren skal trykke på knapper.
        gui = new Gui();
        if(game.isBotTurn()) game.botMove(); //Starter botten om det er dens tur.

    }
}
