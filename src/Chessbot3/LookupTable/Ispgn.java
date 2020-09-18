package Chessbot3.LookupTable;

import Chessbot3.MiscResources.Move;

public interface Ispgn
{
    /**Return the score of the game
     *
     * @return - gamescore
     */
    int GetScore();

    /**Returns type of pgn file it is
     *
     * @return - Type of file
     */
    int GetType();

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
}
