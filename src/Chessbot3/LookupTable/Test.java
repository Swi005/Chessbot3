package Chessbot3.LookupTable;

import Chessbot3.GuiMain.Action;
import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;

import java.io.File;

public class Test
{
    public static void main(String[] args)
    {
        String path = "C:/Users/Sande/Documents/INF101/Chessbot3/src/Chessbot3/files/test2.spgn";
        File file = new File(path);
        System.out.println("-----------SPGN-IO TEST------------");

        spgnIO tstIO = new spgnIO();
        System.out.println("Part one: Write a move");
        tstIO.WriteMoveToFile(new Move(new Tuple<>(0,1), new Tuple<>(0,2)), file);
    }
}
