package Chessbot3.SaveSystem;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;
import Chessbot3.sPGN.Ispgn;
import Chessbot3.sPGN.spgnIO;

import java.io.File;
import java.io.IOException;

public class SaveController
{
    String pathToSaves = "src/Chessbot3/files/saves";
    spgnIO io = new spgnIO();
    public SaveController() {};

    public Ispgn[] GetAllSaves() throws IOException {

        File saveDir = new File(pathToSaves);

        if(saveDir.isDirectory() && saveDir.exists())
        {
            File[] files = saveDir.listFiles();
            Ispgn[] retAr = new Ispgn[files.length];
            for (int i = 0; i < files.length; i++)
            {
                retAr[i] = io.GetSPGN(files[i]);
            }
            return retAr;
        }
        throw new IOException("Error the chosen directory either doesn't exist or isn't a directory");
    }

    public boolean Save(Ispgn ispgn)
    {
        return io.WriteSPGNtoFile(ispgn, new File(ispgn.GetPathToFile()));
    }

    public Board ConvertToBoard(Ispgn ispgn)
    {
        Move[] moves = ispgn.GetAllMoves();
        Board board = new Board();
        for (int i = 0; i < moves.length; i++)
        {
            board.movePiece(moves[i]);
        }
        //Sanity check(s)
        if(board.getScore() == ispgn.GetScore())
            return board;
        else
        {
            //TODO: replace with GUI prompt that asks if player want to continue, see Issue #48
            System.out.println("Error: The game score differs from predicted score");
        }
        return board;
    }


}
