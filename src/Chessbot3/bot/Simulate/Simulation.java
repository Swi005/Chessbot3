package Chessbot3.bot.Simulate;

import Chessbot3.MiscResources.Move;
import Chessbot3.GameBoard.Board;

/**
 * simulate
 */
public class Simulation 
{
    Board board;
    public Simulation(Board board) 
    {
        this.board = board.Copy();
    }
    public void simulateMove( Move move)
    {
        board.MovePiece(move);
    }
}