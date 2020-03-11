package Chessbot3.GameBoard;

import Chessbot3.Color;
import Chessbot3.Move;
import Chessbot3.Tuple;
import Pieces.iPiece;

import java.util.List;

/**
 * IBoard
 */
public interface IBoard 
{

    public iPiece GetPiece(Tuple<Integer,Integer> pos);

    public void MovePiece(Move move);

    public boolean IsMate();
    
    public List<Move> GenMoves(Color c);

    public int GetScore(boolean isWhite);

    public Board Copy();

    public iPiece[][] GetGrid();
}