package Chessbot3.SaveSystem;

import Chessbot3.GameBoard.Game;
import Chessbot3.MiscResources.Move;
import Chessbot3.Pieces.PieceResources.WhiteBlack;
import Chessbot3.sPGN.Ispgn;
import Chessbot3.sPGN.spgnIO;

import java.io.File;
import java.io.IOException;

public class SaveController
{
    String pathToSaves = "src\\Chessbot3\\files\\saves";
    spgnIO io = new spgnIO();
    public SaveController() {
        File save = new File(pathToSaves);
        if(!save.exists())
            if(!save.mkdir())
                System.out.println("Error: Could not create new save folder for some reason.");
    }

    public Ispgn[] getAllSaves() throws IOException {

        File saveDir = new File(pathToSaves);

        if(saveDir.isDirectory() && saveDir.exists())
        {
            File[] files = saveDir.listFiles();
            Ispgn[] retAr = new Ispgn[files.length];
            if(files.length > 0)
            {
                for (int i = 0; i < files.length; i++)
                {
                    int y = i;
                    new Thread(() -> retAr[y] = io.GetSave(files[y])).start(); //Do function async!
                }
            }

            return retAr;
        }
        throw new IOException("Error the chosen directory either doesn't exist or isn't a directory");
    }

    public boolean save(Ispgn ispgn)
    {
        return io.WriteSPGNtoFile(ispgn, new File(ispgn.GetPathToFile()));
    }

    public Game convertToGame(Ispgn ispgn)
    {
        Move[] moves = ispgn.GetAllMoves();
        Game game = new Game();

        switch (ispgn.GetPvP())
        {
            case 1://Bot is white
                game.addBotColor(WhiteBlack.WHITE);
                break;
            case 2:
                game.addBotColor(WhiteBlack.BLACK);
                break;
            case 3:
                game.addBotColor(WhiteBlack.BLACK);
                game.addBotColor(WhiteBlack.WHITE);
                break;
            default:
                break;
        }

        for (Move move : moves) {
            game.playerMove(move);
        }
        //Sanity check(s)
        if(game.getCurrentBoard().getScore() == ispgn.GetScore())
            return game;
        else
        {
            //TODO: replace with GUI prompt that asks if player want to continue, see Issue #48
            System.out.println("Error: The game score differs from predicted score");
        }
        return game;
    }


}
