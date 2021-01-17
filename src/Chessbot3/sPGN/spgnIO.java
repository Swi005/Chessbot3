package Chessbot3.sPGN;

import Chessbot3.GuiMain.Action;
import Chessbot3.MiscResources.Move;
import Chessbot3.Pieces.PieceResources.WhiteBlack;

import java.io.*;
import java.util.*;

import static Chessbot3.GuiMain.Chess.gui;

public class spgnIO implements IspgnIO {
    @Override
    public Move[] ReadMovesFromFile(File file) {
        List<Move> moves = new LinkedList<>();
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();

                //Check if the line starts with a number
                //Only lines with moves start with numbers
                if (line.length() > 0 && Character.isDigit(line.toCharArray()[0])) {
                    String[] tmpStr = line.split("[.]");
                    String[] mv = tmpStr[1].split(":");
                    moves.add(Action.parse(mv[0] + " " + mv[1]));
                }
            }
            return moves.toArray(new Move[]{});
        } catch (IOException e) {
            gui.displayPopupMessage("An error occured when attempting to read file: " + file.toString());
            System.out.println("An error occured while attempting to read file: " + file.toString());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String ReadVar(String name, File file) {
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();

                if (line.contains(name.toUpperCase())) {
                    String tmpStr = "";
                    for (char c : line.toCharArray()) {
                        if (Character.isDigit(c)) {
                            tmpStr += c;
                        } else if (']' == c) {
                            break;
                        }
                    }
                    reader.close();
                    return tmpStr;
                }
            }
        } catch (IOException e) {
            //gui.displayPopupMessage("An error occured while attempting to read file: " + file.toString());
            System.out.println("An error occured while attempting to read file: " + file.toString());
            e.printStackTrace();
        }
        //gui.displayPopupMessage("Error: could not find field with the name " + name);
        System.out.println("Could not find field with name: " + name);
        return "";
    }

    @Override
    public boolean WriteSPGNtoFile(Ispgn spgn, File dir) {
        try{
            if(spgn.getClass() == Chessbot3.sPGN.spgn.class)
            {
                this.WriteToFile(spgn.getVars(), spgn.GetAllMoves(), dir);
                return true;
            }else{
                LookupTable tbl = (LookupTable)spgn;
                this.WriteToFile(tbl.getVars(), tbl.GetAllMoves(), dir);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public void WriteToFile(HashMap<String, Object> vars, Move[] moves, File file) {
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file, true);

            for (String str : vars.keySet())
            {
                writer.write("[ " + str + " " + vars.get(str) + "] \n");
            }


            int lineNm = 1;

            //Read last line and get its move number
            try {
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    String lastLine = reader.nextLine();
                    if (!reader.hasNextLine()) {
                        String[] tempArr = lastLine.split("[.]");
                        lineNm = Integer.parseInt(tempArr[0]) + 1;
                    }
                }
                reader.close();
            } catch (IOException e) {
            }
            for (Move move : moves) {
                String moveString = move.toAlgebraicNotation();
                writer.write(lineNm + "." + moveString + "\n");
                lineNm++;
            }
            writer.close();
        } catch (IOException e) {
            gui.displayPopupMessage("An error occured when attempting to write to " +file.toString());
            //System.out.println("While writing to file: " + file.toString() + " an error occured.");
            e.printStackTrace();
        }
    }

    @Override
    public spgn GetSave(File file)
    {
        Move[] moves = ReadMovesFromFile(file);
        int score = Integer.parseInt(ReadVar("SCORE", file));
        int pvp = Integer.parseInt(ReadVar("PVP", file));
        String name = ReadVar("NAME", file);

        return new spgn(score, pvp, name, moves);
    }

    @Override
    public spgn GetSave(String path) {
        return GetSave(new File(path));
    }

    @Override
    public LookupTable GetTable(File file) {

        String name = ReadVar("NAME", file);
        String color = ReadVar("COLOR", file);
        String perfRating = ReadVar("PERFRATING", file);
        String plyrWin = ReadVar("PLYRWIN", file);
        String draw = ReadVar("DRAW", file);
        String opWin = ReadVar("OPWIN", file);
        Move[] moves = ReadMovesFromFile(file);
        return new LookupTable(name, color, perfRating,plyrWin,draw,opWin,moves);
    }

}
