package Chessbot3.GameBoard;

import Chessbot3.Move;
import Chessbot3.Tuple;
import Chessbot3.Piece.iPiece;

/**
 * IBoard
 */
public interface IBoard 
{

    public iPiece GetPiece(Tuple<Integer,Integer> pos);

    public void MovePiece(Move move);

    public boolean IsMate();
    
    public Move[] GenMoves(iPiece pice);

    public int GetScore(boolean isWhite);

    public Board Copy();

    public iPiece[][] GetGrid();
}