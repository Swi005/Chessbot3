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

    //Dict over hvor det generelt er bra å stå for de forskjellige brikkene.
    public static final Dictionary<Character, Integer[][]> posValueDict = Generator.makePosValueDict();

    //Selve Gui-en.
    public static Gui gui = new Gui();

    public static void main(String[] args) {
        //Oppretter Gui, og alt som skal til for å spille.
        //Starter en uendelig loop. Gui har en haug med actionlisteners,
        //som gjør at brukeren kan spille ved å trykke på knappene på skjermen,
        //selv om loopen kjører på siden.
        while (true) {
            try {
                Thread.sleep(0);
                if (game.isBotTurn()) game.botMove();
            } catch (InterruptedException x) { }
        }
    }
}
