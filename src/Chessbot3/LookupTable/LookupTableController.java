package Chessbot3.LookupTable;

import Chessbot3.MiscResources.Move;
import Chessbot3.sPGN.Ispgn;
import Chessbot3.sPGN.spgnIO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LookupTableController //Give it an interface maybe?
{
    private List<Ispgn> openingTables;
    private List<Move> movesMade;
    private spgnIO IOcontroller = new spgnIO();

    public LookupTableController()
    {
        openingTables = new ArrayList<>();
        movesMade = new ArrayList<>();
        populateOpeningTables();
    }

    private void populateOpeningTables()
    {
        String pathToDirectory = "src/Chessbot3/files/lookuptables";
        File dir = new File(pathToDirectory);

        if(dir.isDirectory() && dir.exists())
        {
            for (File fl : dir.listFiles())
                openingTables.add(IOcontroller.GetSPGN(fl));
        }
        else
        {
            System.out.println("Error the given file path does not lead to the correct place");
        }
    }

    public Move getNextMove()
    {
        return selectTableFrom(getAllTablesCompatible()).GetMove(movesMade.size()-1);
    }
    public void registerMove(Move move)
    {
        movesMade.add(move);
    }

    private List<Ispgn> getAllTablesCompatible()
    {
        List<Ispgn> candidates = new ArrayList<>();
        for(Ispgn table : openingTables)
        {
            boolean b = true;
            for (int i = 0; i < movesMade.size(); i++)
            {
                if(movesMade.get(i) != table.GetMove(i))
                    b = false; break;
            }
            if(b) candidates.add(table);
        }
        return candidates;
    }

    private Ispgn selectTableFrom(List<Ispgn> tables)
    {
        //Replace with whatever method used to choose a table from available candidates
        if(tables.size() == 0)
            return null;
        return  tables.get(new Random().nextInt(tables.size()));
    }
}
