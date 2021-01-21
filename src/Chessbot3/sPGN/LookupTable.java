package Chessbot3.sPGN;

import Chessbot3.MiscResources.Move;
import Chessbot3.PGNConverter.Converter;
import Chessbot3.PGNConverter.Parser;
import Chessbot3.Pieces.PieceResources.WhiteBlack;

import java.util.Arrays;
import java.util.HashMap;

public class LookupTable implements Ispgn
{
    int i = 0;
    String name = "";
    WhiteBlack color;
    int perfRating;
    double plyrWin;
    double draw;
    double opWin;
    Move[] moves;
    String path;
    public LookupTable(String name,String color, String perfRating, String plyrWin, String draw, String opWin, String moves)
    {
        this.name = name;
        if(color == "white")
            this.color = WhiteBlack.WHITE;
        if(color == "black")
            this.color = WhiteBlack.BLACK;
        this.perfRating = Integer.parseInt(perfRating);
        this.plyrWin = Double.parseDouble(plyrWin);
        this.draw = Double.parseDouble(draw);
        this.opWin = Double.parseDouble(opWin);
        String[] prs = Parser.parseMoves(moves);
        this.moves = new Move[prs.length];
        Converter cvn = new Converter();
        for (int i = 0; i <prs.length; i++)
        {
            this.moves[i] = cvn.convertToMove(prs[i]);
        }
        this.path = "C:\\Users\\Sande\\Documents\\INF101\\Chessbot3\\src\\Chessbot3\\files\\lookuptables\\" + name+ ".spgn";

    }
    public LookupTable(String name,String color, String perfRating, String plyrWin, String draw, String opWin, Move[] moves)
    {
        this.name = name;
        if(color == "white")
            this.color = WhiteBlack.WHITE;
        if(color == "black")
            this.color = WhiteBlack.BLACK;
        this.perfRating = Integer.parseInt(perfRating);
        this.plyrWin = Double.parseDouble(plyrWin);
        this.draw = Double.parseDouble(draw);
        this.opWin = Double.parseDouble(opWin);
        this.moves = moves;
        this.path = "Chessbot3/src/Chessbot3/files/lookuptables/" + name+ ".spgn";
    }

    @Override
    public String toString() {
        return "LookupTable{" +
                "name='" + name + '\'' +
                ", color=" + color +
                ", perfRating=" + perfRating +
                ", plyrWin=" + plyrWin +
                ", draw=" + draw +
                ", opWin=" + opWin +
                ", moves=" + Arrays.toString(moves) +
                '}';
    }

    @Override
    public int GetScore() {
        return -1;
    }


    @Override
    public Move GetMove(int index) {
        return moves[index];
    }

    @Override
    public Move GetNextMove() {
        Move m = moves[i];
        i++;
        return m;
    }

    @Override
    public Move[] GetAllMoves() {
        return moves;
    }

    @Override
    public String GetPathToFile() {
        return path;
    }

    @Override
    public String GetName() {
        return name;
    }

    @Override
    public int GetPvP() {
        return -1;
    }

    @Override
    public HashMap<String,Object> getVars()
    {
        HashMap<String,Object> map = new HashMap<>();
        map.put("NAME", name);
        map.put("Color", color);
        map.put("PVP", perfRating);
        map.put("PLYRWIN", plyrWin);
        map.put("DRAW", draw);
        map.put("OPWIN", opWin);
        return  map;
    }
}
