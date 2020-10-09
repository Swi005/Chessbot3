package Chessbot3.LookupTable;

import Chessbot3.MiscResources.Move;
import Chessbot3.sPGN.Ispgn;
import Chessbot3.sPGN.spgnIO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static Chessbot3.GuiMain.Chess.gui;

public class LookupTableController //Give it an interface maybe?
{
    private List<Ispgn> openingTables;
    private List<Move> movesMade;
    int turnIndex = movesMade.size();
    private final spgnIO io = new spgnIO();
    private final String pathToTables = "";//TODO: Assign path

    public LookupTableController()
    {
        try {
            openingTables = Arrays.asList(getAllTables());
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());//TODO: Handle error in case lookup table folder doesn't exist
            e.printStackTrace();
        }
        movesMade = new ArrayList<>();
    }

    /**Returns all the lookuptables
     *
     * @return - List of tables
     * @throws IOException
     */
    public Ispgn[] getAllTables() throws IOException {

        File tableDir = new File(pathToTables);

        if(tableDir.isDirectory() && tableDir.exists())
        {
            File[] files = tableDir.listFiles();
            Ispgn[] retAr = new Ispgn[files.length];
            if(files.length > 0)
            {
                for (int i = 0; i < files.length; i++)
                {
                    int y = i;
                    new Thread(() -> retAr[y] = io.GetSPGN(files[y])).start(); //Do function async!
                }
            }

            return retAr;
        }
        throw new IOException("Error the chosen directory either doesn't exist or isn't a directory");
    }

    /**
     * Register a move made in
     *
     * @param move - Move made
     */
    public void registerMove(Move move)
    {
        movesMade.add(move);
    }

    /**
     * Get next move in an opening table
     * @return - Next move to be made
     */
    public Move getNextMove()
    {
        pruneMoves();//Remove all tables
        Random rnd = new Random();

        if(openingTables.size() == 0)
            return null; //If no strats are available, return null
        Ispgn strat = openingTables.get(rnd.nextInt(openingTables.size()));//Chose a random available strat
        return strat.GetAllMoves()[turnIndex-1];//Return the move
    }



    /** Removes all tables that do not correspond to current board state
     *
     */
    void pruneMoves()
    {
        turnIndex++;
        for (Ispgn table: openingTables.toArray(new Ispgn[]{}))
        {
            Move[] tableMvs = table.GetAllMoves();
            for (int i = 0; i < turnIndex; i++)
            {
                if(tableMvs[i] != movesMade.get(i))
                {
                   openingTables.remove(table);
                   break;
                }
            }
        }
    }

}
