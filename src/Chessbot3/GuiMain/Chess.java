package Chessbot3.GuiMain;

import java.awt.image.BufferedImage;
import java.util.Dictionary;
import java.util.Hashtable;

import Chessbot3.MiscResources.Generator;
import Chessbot3.MiscResources.Tuple;

public class Chess {

    //Dict over hvilke retninger hver brikke kan gå.
    public static final Dictionary<Character, Tuple[]> direcDict = Generator.makeDirections();

    //Dict over bildet som hører til hver enkelt brikke.
    public static final Hashtable<String, BufferedImage> imageDict = Generator.makeImages();

    public static void main(String[] args) {
        //Opprettet Gui, og alt som skal til for å spille.
        //Når Gui er ferdig er det ingen kode som kjører, kun actionListeners i Action som venter på at du skal trykke noe.
        //Hver gang spilleren gjør et trekk aktiverer det botten til den har gjort et trekk,
        //etter det er det ingen kode som kjører lenger, før spilleren gjør et nytt trekk.
        new Gui();

    }
}
