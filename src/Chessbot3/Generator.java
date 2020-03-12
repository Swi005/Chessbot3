package Chessbot3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

public class Generator {
    public static Tuple<Integer, Integer> N = new Tuple(0, 1);
    public static Tuple<Integer, Integer> S = new Tuple(0, -1);
    public static Tuple<Integer, Integer> E = new Tuple(1, 0);
    public static Tuple<Integer, Integer> W = new Tuple(-1, 0);

    public static Tuple<Integer, Integer> NW = new Tuple(-1, 1);
    public static Tuple<Integer, Integer> NE = new Tuple(1, 1);
    public static Tuple<Integer, Integer> SW = new Tuple(-1, -1);
    public static Tuple<Integer, Integer> SE = new Tuple(1, -1);

    static Tuple<Integer, Integer> NNW = new Tuple(-1, 2);
    static Tuple<Integer, Integer> NNE = new Tuple(1, 2);
    static Tuple<Integer, Integer> EEN = new Tuple(2, 1);
    static Tuple<Integer, Integer> EES = new Tuple(2, -1);
    static Tuple<Integer, Integer> SSE = new Tuple(1, -2);
    static Tuple<Integer, Integer> SSW = new Tuple(-1, -2);
    static Tuple<Integer, Integer> WWS = new Tuple(-2, -1);
    static Tuple<Integer, Integer> WWN = new Tuple(-2, 1);



    public static Dictionary makeDirections(){
        Dictionary<Character, Tuple<Integer, Integer>[]> directions =  new Hashtable();
        //Oppretter en dict med alle himmelretningene hver enkelt brikke kan bevege seg.
        directions.put('P', new Tuple[] {N, NW, NE});
        directions.put('N', new Tuple[] {NNW, NNE, EEN, EES, SSE, SSW, WWS, WWN});
        directions.put('B', new Tuple[] {NE, SE, SW, NW});
        directions.put('R', new Tuple[] {N, E, S, W});
        directions.put('Q', new Tuple[] {N, E, S, W, NE, SE, SW, NW});
        directions.put('K', new Tuple[] {N, E, S, W, NE, SE, SW, NW});
        return directions;
    }
    public static Hashtable<String, BufferedImage> makeImages(){
        /* Oppretter en dict på der nøkkelen er navnet på brikken, og innholdet er et BufferedImage.
         */
        Hashtable<String, BufferedImage> imageTable = new Hashtable<>();
        for (String name : new String[] {
                "bishop_black",
                "bishop_white",
                "horse_black",
                "horse_white",
                "king_black",
                "king_white",
                "pawn_black",
                "pawn_white",
                "queen_black",
                "queen_white",
                "rook_black",
                "rook_white"
        }) {
            try {
                File temp = new File("src\\Pieces\\PieceImages\\" + name + ".png");
                BufferedImage img = ImageIO.read(temp);
                imageTable.put(name, img);
            } catch (IOException e) {
                System.out.println("Cannot find \"" + name + ".png\"");
            }
        }
        return imageTable;
    }
}
