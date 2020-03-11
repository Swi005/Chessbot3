package Chessbot3.GameBoard;

import Chessbot3.Move;

/**
 * IBoard
 */
public interface IBoard 
{

    public IPice GetPice(Move move);

    public void MovePiece(IPice pice, Move move);

    public boolean IsMate();
    
    public Move[] GenMoves(IPice pice);

    public int GetScore(boolean isWhite);
    
}