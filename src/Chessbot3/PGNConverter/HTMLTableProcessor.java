package Chessbot3.PGNConverter;

import Chessbot3.sPGN.LookupTable;
import Chessbot3.sPGN.spgnIO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HTMLTableProcessor
{
    static File file = new File("C:\\Users\\Sande\\Documents\\INF122\\Ukesoppgaver\\uke7\\table1.html");

    static LookupTable[] processFile(File file)
    {
        List<LookupTable> out = new ArrayList<>();
        String allTheData = "";
        String[] dataPoint = new String[11];
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine())
            {
                allTheData += scanner.nextLine();
            }
        }catch (IOException e)
        {
            System.out.println(e.getMessage());
        }

        //allTheData = allTheData.replaceAll("[\\s]", "");
        String[] rows = allTheData.split("<tr ([\\s\\S]*?)>");//Seperate out each table
        for (String str:rows)
        {
            String[] data = str.split("<td ([\\s\\S]*?)>");//separeate out each data point in table

            int count = 0;
            for(String s : data)
            {
                if (s.matches(".*\\w.*"))
                {
                    if(s.contains("black"))
                    {
                        s = "black";
                    }
                    else if (s.contains("white"))
                    {
                        s = "white";
                    }
                    else
                    {
                        s = s.replaceAll("<([\\s\\S]*?)>", "");
                        s = s.replaceAll("\\s+\\s", " "); // removes multiple spaces with one space
                        s = s.replaceAll("^\\s", "");
                        s = s.replaceAll("\\s$", "");
                        s = s.replaceAll( "%", "");
                    }
                    dataPoint[count] = s;
                    count++;
                }
            }
            if(dataPoint[0] != null)
                out.add(new LookupTable(dataPoint[0], dataPoint[1], dataPoint[5], dataPoint[7], dataPoint[8], dataPoint[9], dataPoint[10]));
        }

        return out.toArray(new LookupTable[0]);
    }

    public static void main(String[] args)
    {
        spgnIO io = new spgnIO();
        LookupTable[] xs = processFile(file);
        for (LookupTable table:xs) {
            io.WriteSPGNtoFile(table, new File(table.GetPathToFile()));
        }
    }
}
