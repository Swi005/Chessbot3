package Chessbot3.sPGN;

import Chessbot3.MiscResources.Move;
import Chessbot3.Pieces.PieceResources.WhiteBlack;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class spgn implements Ispgn
{
    private int index = 0;
    private int score;
    private String name;
    private Move[] moves;
    private String path;
    private int pvp; // 0 = pvp | 1 = bot is white | 2 = bot is black | 3 = bot v bot

    //Constructor for making a new file
    public spgn(int score, int pvp, String name, Move[] moves)
    {
        this.score = score;
        this.pvp = pvp;
        this.name = name;
        this.moves = moves;

        path = "src/Chessbot3/files/saves/" + name + ".psgn";
    }

    //Constructor for retriving a file as an spgn
    public spgn(File file)
    {
        spgnIO spgnController = new spgnIO();
        spgn temp = spgnController.GetSave(file);
        this.moves = temp.moves;
        this.score = temp.score;
        path = file.getPath();
        this.pvp = temp.pvp;
        this.name = temp.name;
    }
    public spgn(String path)
    {
        spgnIO spgnController = new spgnIO();
        spgn temp = spgnController.GetSave(new File(path));
        this.moves = temp.moves;
        this.score = temp.score;
        this.name = temp.name;
        this.path = temp.path;
        this.pvp = temp.pvp;
    }

    @Override
    public int GetScore()
    {
        return score;
    }

    @Override
    public Move GetMove(int index)
    {
        return moves[index];
    }

    @Override
    public Move GetNextMove()
    {
        index++;
        return GetMove(index);
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
        return pvp;
    }

    @Override
    public HashMap<String, Object> getVars() {
        HashMap<String,Object> map = new HashMap<>();
        map.put("NAME", name);
        map.put("SCORE", score);
        map.put("PVP", pvp);
        return  map;

    }

    @Override
    public String toString()//TODO: Find a string representation of an spgn object
    {
        return name.split("\\.")[0];
        /*
        return "spgn{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", type=" + type +
                ", moves=" + Arrays.toString(moves) +
                ", path='" + path + '\'' +
                ", pvp='" + pvp + '\'' +
                ", botColor='" + botColor + '\'' +
                '}';

         */
    }
}
