package Chessbot3.sPGN;

import Chessbot3.MiscResources.Move;
import Chessbot3.Pieces.PieceResources.WhiteBlack;

import java.util.HashMap;

public interface Ispgn
{
    /**Return the score of the game
     *
     * @return - gamescore
     */
    int GetScore();

    /**Get move at turn index
     *
     * @param index -
     * @return - move at turn index
     */
    Move GetMove(int index);

    /**Get the next move
     *
     * @return - return the next move
     */
    Move GetNextMove();

    /**Returns an array of all moves done
     *
     * @return - Array of all the moves done
     */
    Move[] GetAllMoves();

    /**Returns the path to where the spgn file will be saved
     *
     * @return - patht to where the file is stored
     */
    String GetPathToFile();
    /**Get name of spgn
     *
     * @return - name of file
     */
    String GetName();
    int GetPvP();
    HashMap<String, Object> getVars();
}
