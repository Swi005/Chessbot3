package Chessbot3.bot.Simulate;

import Chessbot3.Move;
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