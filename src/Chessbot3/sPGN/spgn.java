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
    private int pvp;
    private WhiteBlack botColor;

    //Constructor for making a new file
    public spgn(int score, int type, int pvp, WhiteBlack botColor, String name, Move[] moves)
    {
        this.score = score;
        this.type = type;
        this.pvp = pvp;
        this.name = name;
        this.botColor = botColor;
        this.moves = moves;

        if(type == 0)
            path = "src/Chessbot3/files/saves/" + name + ".psgn";
            //path = "src\\Chessbot3\\files\\saves\\" + name + ".spgn";//TODO: Find out a naming scheme
        else if(type == 1)
            path = "Chessbot3/src/Chessbot3/files/lookuptables/"+ "lookup:" + name+ ".spgn";//TODO: Find out a naming scheme
    }

    //Constructor for retriving a file as an spgn
    public spgn(File file)
    {
        spgnIO spgnController = new spgnIO();
        spgn temp = spgnController.GetSPGN(file);
        this.moves = temp.moves;
        this.score = temp.score;
        this.type = temp.type;
        this.botColor = temp.botColor;
        path = file.getPath();
        this.pvp = temp.pvp;
        this.botColor = temp.botColor;
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
        this.botColor = temp.botColor;
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
    public int GetIsPvP() {
        return pvp;
    }

    @Override
    public WhiteBlack GetBotColor() {
        return botColor;
    }

    @Override
    public String toString()//TODO: Find a string representation of an spgn object
    {
        return name;
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
