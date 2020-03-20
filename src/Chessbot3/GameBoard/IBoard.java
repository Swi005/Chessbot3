package Chessbot3.GameBoard;

import Pieces.WhiteBlack;
import Chessbot3.Move;
import Chessbot3.Tuple;
import Pieces.iPiece;

import java.util.ArrayList;
import java.util.List;

/**
 * IBoard
 */
public interface IBoard 
{

    public iPiece GetPiece(Tuple<Integer,Integer> pos);

    public ArrayList<Tuple> MovePiece(Move move);
    
    public List<Move> GenMoves(WhiteBlack c);

    public int GetScore(boolean isWhite);

    public Board Copy();

    public iPiece[][] GetGrid();
}