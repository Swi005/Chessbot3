package Chessbot3.LookupTable;

import Chessbot3.MiscResources.Move;

import java.io.File;

public class spgn implements Ispgn
{
    private int index = 0;
    private int score;
    private int type; //0 for save| 1 for lookup table
    private Move[] moves;

    //Constructor for making a new file
    public spgn(int score, int type, Move[] moves)
    {
        this.score = score;
        this.type = type;
        this.moves = moves;
    }

    //Constructor for retriving a file as an spgn
    public spgn(File file)
    {
        spgnIO spgnController = new spgnIO();
        spgn temp = spgnController.GetSPGN(file);
        this.moves = temp.moves;
        this.score = temp.score;
        this.type = temp.type;
    }
    public spgn(String path)
    {
        spgnIO spgnController = new spgnIO();
        spgn temp = spgnController.GetSPGN(new File(path));
        this.moves = temp.moves;
        this.score = temp.score;
        this.type = temp.type;
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
}
