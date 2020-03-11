package Chessbot3;

import java.util.Dictionary;
import java.util.Hashtable;

public class Generator {
    static Tuple<Integer, Integer> N = new Tuple(0, 1);
    static Tuple<Integer, Integer> S = new Tuple(0, -1);
    static Tuple<Integer, Integer> E = new Tuple(1, 0);
    static Tuple<Integer, Integer> W = new Tuple(-1, 0);

    static Tuple<Integer, Integer> NW = new Tuple(-1, 1);
    static Tuple<Integer, Integer> NE = new Tuple(1, 1);
    static Tuple<Integer, Integer> SW = new Tuple(-1, -1);
    static Tuple<Integer, Integer> SE = new Tuple(1, -1);

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
}