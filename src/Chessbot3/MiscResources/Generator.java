package Chessbot3.MiscResources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

public class Generator {

    //De primære himmelretningene, lagret som vektorer.
    public static Tuple<Integer, Integer> N = new Tuple(0, -1);
    public static Tuple<Integer, Integer> S = new Tuple(0, 1);
    public static Tuple<Integer, Integer> E = new Tuple(1, 0);
    public static Tuple<Integer, Integer> W = new Tuple(-1, 0);

    //Sekundære himmelretninger.
    public static Tuple<Integer, Integer> NW = new Tuple(-1, -1);
    public static Tuple<Integer, Integer> NE = new Tuple(1, -1);
    public static Tuple<Integer, Integer> SW = new Tuple(-1, 1);
    public static Tuple<Integer, Integer> SE = new Tuple(1, 1);

    //Hesteretninger. f. eks to opp og en til venstre, eller to til høyre og en opp.
    static Tuple<Integer, Integer> NNW = new Tuple(-1, -2);
    static Tuple<Integer, Integer> NNE = new Tuple(1, -2);
    static Tuple<Integer, Integer> EEN = new Tuple(2, -1);
    static Tuple<Integer, Integer> EES = new Tuple(2, 1);
    static Tuple<Integer, Integer> SSE = new Tuple(1, 2);
    static Tuple<Integer, Integer> SSW = new Tuple(-1, 2);
    static Tuple<Integer, Integer> WWS = new Tuple(-2, 1);
    static Tuple<Integer, Integer> WWN = new Tuple(-2, -1);


    public static Dictionary<Character, Integer[][]> makePosValueDict(){
        Dictionary<Character, Integer[][]> ret = new Hashtable<>();
        ret.put('P', new Integer[][]{
            new Integer[] {100,   100,   100,   100,   100,   100,   100,  100,},
            new Integer[] { 78,  83,  86,  73, 102,  82,  85,  90},
            new Integer[] {7,  29,  21,  44,  40,  31,  44,   7},
            new Integer[] {-17,  16,  -2,  15,  14,   0,  15, -13},
            new Integer[] {-26,   3,  10,   9,   6,   1,   0, -23},
            new Integer[] {-22,   9,   5, -11, -10,  -2,   3, -19},
            new Integer[] { -31,   8,  -7, -37, -36, -14,   3, -31},
            new Integer[] {0,   0,   0,   0,   0,   0,   0,  0}
        });
        ret.put('R', new Integer[][]{
            new Integer[] {35,  29,  33,   4,  37,  33,  56,  50 },
            new Integer[] {55,  29,  56,  67,  55,  62,  34,  60},
            new Integer[] { 19,  35,  28,  33,  45,  27,  25,  15},
            new Integer[] {0,   5,  16,  13,  18,  -4,  -9,  -6},
            new Integer[] {-28, -35, -16, -21, -13, -29, -46, -30},
            new Integer[] {-42, -28, -42, -25, -25, -35, -26, -46},
            new Integer[] {-53, -38, -31, -26, -29, -43, -44, -53},
            new Integer[] {-30, -24, -18,   5,  -2, -18, -31, -32}
        });
        ret.put('N', new Integer[][]{
            new Integer[] {-66, -53, -75, -75, -10, -55, -58, -70},
            new Integer[] { -3,  -6, 100, -36,   4,  62,  -4, -14},
            new Integer[] {10,  67,   1,  74,  73,  27,  62,  -2},
            new Integer[] {24,  24,  45,  37,  33,  41,  25,  17},
            new Integer[] {-1,   5,  31,  21,  22,  35,   2,   0},
            new Integer[] {-18,  10,  13,  22,  18,  15,  11, -14},
            new Integer[] { -23, -15,   2,   0,   2,   0, -23, -20},
            new Integer[] {-74, -23, -26, -24, -19, -35, -22, -69}
        });
        ret.put('B', new Integer[][]{
            new Integer[] {-59, -78, -82, -76, -23,-107, -37, -50},
            new Integer[] {-11,  20,  35, -42, -39,  31,   2, -22},
            new Integer[] {-11,  20,  35, -42, -39,  31,   2, -22},
            new Integer[] {-11,  20,  35, -42, -39,  31,   2, -22},
            new Integer[] {13,  10,  17,  23,  17,  16,   0,   7},
            new Integer[] {14,  25,  24,  15,   8,  25,  20,  15},
            new Integer[] {19, 20, 11, 6, 7, 6, 20, 16},
            new Integer[] { -7,   2, -15, -12, -14, -15, -10, -10}
        });
        ret.put('Q', new Integer[][]{
            new Integer[] {6,   1,  -8,-104,  69,  24,  88,  26 },
            new Integer[] {14,  32,  60, -10,  20,  76,  57,  24},
            new Integer[] {-2,  43,  32,  60,  72,  63,  43,   2},
            new Integer[] {1, -16,  22,  17,  25,  20, -13,  -6},
            new Integer[] {-14, -15,  -2,  -5,  -1, -10, -20, -22},
            new Integer[] {-30,  -6, -13, -11, -16, -11, -16, -27},
            new Integer[] {-36, -18,   0, -19, -15, -15, -21, -38},
            new Integer[] {-39, -30, -31, -13, -31, -36, -34, -42}
        });
        ret.put('K', new Integer[][]{
            new Integer[] {4,  54,  47, -99, -99,  60,  83, -62},
            new Integer[] {-32,  10,  55,  56,  56,  55,  10,   3},
            new Integer[] {-62,  12, -57,  44, -67,  28,  37, -31},
            new Integer[] {-55,  50,  11,  -4, -19,  13,   0, -49},
            new Integer[] {-55, -43, -52, -28, -51, -47,  -8, -50},
            new Integer[] {-47, -42, -43, -79, -64, -32, -29, -32},
            new Integer[] {-4,   3, -14, -50, -57, -18,  13,   4},
            new Integer[] {17,  30,  -3, -14,   6,  -1,  40,  18}
        });

        return ret;
    }

    public static Dictionary makeDirections(){
        //Oppretter en dict med alle himmelretningene hver enkelt brikke kan bevege seg.
        //Denne blir kalt opp en gang i starten av Chess, og alle andre kan referere til dem med direcDict.get().

        Dictionary<Character, Tuple<Integer, Integer>[]> directions =  new Hashtable();
        directions.put('P', new Tuple[] {N, NW, NE}); //Hvite bønder
        directions.put('p', new Tuple[] {S, SW, SE}); //Svarte bønder
        directions.put('N', new Tuple[] {NNW, NNE, EEN, EES, SSE, SSW, WWS, WWN});
        directions.put('B', new Tuple[] {NE, SE, SW, NW});
        directions.put('R', new Tuple[] {N, E, S, W});
        directions.put('Q', new Tuple[] {N, E, S, W, NE, SE, SW, NW});
        directions.put('K', new Tuple[] {N, E, S, W, NE, SE, SW, NW});
        return directions;
    }
    public static Hashtable<String, BufferedImage> makeImages(){
        //Oppretter en dict på der nøkkelen er navnet på brikken, og innholdet er et BufferedImage.

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
                File temp = new File("src\\Chessbot3\\Pieces\\PieceImages\\" + name + ".png");
                BufferedImage img = ImageIO.read(temp);
                imageTable.put(name, img);
            } catch (IOException e) {
                System.out.println("Cannot find \"" + name + ".png\"");
            }
        }
        return imageTable;
    }
}
