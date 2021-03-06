package Chessbot3.sPGN;

import Chessbot3.MiscResources.Move;
import Chessbot3.Pieces.PieceResources.WhiteBlack;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

public class spgn implements Ispgn
{
    private int index = 0;
    private int score;
    private String name;
    private int type; //0 for save| 1 for lookup table
    private Move[] moves;
    private String path;
    private int pvp; // 0 = pvp | 1 = bot is white | 2 = bot is black | 3 = bot v bot

    //Constructor for making a new file
    public spgn(int score, int type, int pvp, String name, Move[] moves)
    {
        this.score = score;
        this.type = type;
        this.pvp = pvp;
        this.name = name;
        this.moves = moves;

        if(type == 0)
            path = "src/Chessbot3/files/saves/" + name + ".psgn";
        else if(type == 1)
            path = "Chessbot3/src/Chessbot3/files/lookuptables/" + name+ ".spgn";
    }

    //Constructor for retriving a file as an spgn
    public spgn(File file)
    {
        spgnIO spgnController = new spgnIO();
        spgn temp = spgnController.GetSPGN(file);
        this.moves = temp.moves;
        this.score = temp.score;
        this.type = temp.type;
        path = file.getPath();
        this.pvp = temp.pvp;
        this.name = temp.name;
    }
    public spgn(String path)
    {
        spgnIO spgnController = new spgnIO();
        spgn temp = spgnController.GetSPGN(new File(path));
        this.moves = temp.moves;
        this.score = temp.score;
        this.name = temp.name;
        this.type = temp.type;
        this.path = temp.path;
        this.pvp = temp.pvp;
    }

    @Override
    public int GetScore()
    {
        return score;
    }

    @Override
    public int GetType()
    {
        return type;
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
