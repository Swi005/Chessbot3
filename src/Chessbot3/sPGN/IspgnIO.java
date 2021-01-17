package Chessbot3.sPGN;

import Chessbot3.MiscResources.Move;
import Chessbot3.Pieces.PieceResources.WhiteBlack;

import java.io.File;
import java.util.HashMap;

public interface IspgnIO
{
    /** Reads a file and returns a list of all moves
     *
     * @return
     */
    Move[] ReadMovesFromFile(File file);

    /**Returns the variable specified in type
     *
     * @param name - var name
     * @param file
     * @return - Variable as a string
     */
    String ReadVar(String name, File file);

    /** Converts an spgn object to a file
     *
     * @param spgn - object that is to be written to a file
     * @return true if succsessfull
     */
    boolean WriteSPGNtoFile(Ispgn spgn, File file);

    /**Writes a sequence of moves to a file
     *
     * @param moves
     * @return
     */
    void WriteToFile(HashMap<String, Object> vars, Move[] moves, File file);

    /**Reads a file and return its content as a ispgn object
     *
     * @param file
     * @return
     */
    Ispgn GetSave(File file);
    Ispgn GetSave(String path);
    LookupTable GetTable(File file);

}
