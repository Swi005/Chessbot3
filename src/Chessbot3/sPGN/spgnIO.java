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

    //Deprecated do not use, use WriteToFile() instead
    @Override
    public void WriteMoveToFile(Move move, File file) {
        try {
            boolean isNew = file.createNewFile();
            FileWriter writer = new FileWriter(file, true);

            if (isNew) {
                writer.write("[MODE 0] \n");
                writer.write("[SCORE 0] \n");
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

            String moveString = move.toAlgebraicNotation();
            writer.write(lineNm + "." + moveString + "\n");
            writer.close();
        } catch (IOException e) {
            gui.displayPopupMessage("An error occured when attempting to write to " + file.toString());
            //System.out.println("While writing to file: " + file.toString() + " an error occured.");
            e.printStackTrace();
        }
    }

    @Override
    public boolean WriteSPGNtoFile(Ispgn spgn, File dir) {
        try{
            this.WriteToFile(spgn.GetScore(), spgn.GetType(), spgn.GetName(), spgn.GetIsPvP(), spgn.GetBotColor(), spgn.GetAllMoves(), dir);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public void WriteToFile(int score, int type, String name, int pvp, WhiteBlack bot, Move[] moves, File file) {
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file, true);

            writer.write("[TYPE " + type + "] \n");
            writer.write("[SCORE " + score + "] \n");
            writer.write("[PVP " + pvp + "] \n");
            writer.write("[NAME " + name + "] \n");
            if(bot == WhiteBlack.BLACK)
                writer.write("[BOT " + 1 + "] \n");
            else if(bot == WhiteBlack.WHITE)
                writer.write("[BOT " + 0 + "] \n");

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
    public spgn GetSPGN(File file)
    {
        Move[] moves = ReadMovesFromFile(file);
        int score = Integer.parseInt(ReadVar("SCORE", file));
        int type = Integer.parseInt(ReadVar("TYPE", file));
        int pvp = Integer.parseInt(ReadVar("PVP", file));
        WhiteBlack bot;
        if(ReadVar("BOT", file) == "0")
            bot = WhiteBlack.WHITE;
        else
            bot = WhiteBlack.BLACK;

        String name = ReadVar("NAME", file);

        return new spgn(score, type, pvp, bot, name, moves);
    }

    @Override
    public spgn GetSPGN(String path) {
        return GetSPGN(new File(path));
    }
}
