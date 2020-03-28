package Chessbot3.GameBoard;

import Chessbot3.Move;
import Pieces.WhiteBlack;
/**
 * BoardController is a class for controlling the board
 * 
 * It acts as an interface between the commands given and the underlying board
 */
public class BoardController 
{   
    Board board;
    public BoardController(Board board) 
    {
        this.board = board;
    }
    //This will be the actual method for giving commands to the board.
    public boolean ExcecuteCommand() 
    {
        return false;
    }
    
    public Boolean checkPlayerMove(Move playerMove) 
    {
        Move[] availableMoves;
        if (board.getPlayerToMove() == WHITE)
            availableMoves = GenMoves(WHITE);
        else availableMoves = GenMoves(BLACK);
        Boolean ret = false;
        for (Move move : availableMoves) {
            System.out.println(move);
            if (move.equals(playerMove)) {
                ret = true;
            }
        }
        /* // TODO: 15.03.2020 Fiks denne, så det ikke blir lov å sette seg selv i sjakk 
        if(ret) {
            Board copy = this.Copy();
            copy.MovePiece(playerMove);
            Move[] counterMoves;
            if (copy.isWhitesTurn) counterMoves = GenMoves(WHITE);
            else counterMoves = GenMoves(BLACK);
            for (Move counter : counterMoves) {
                iPiece target = copy.GetGrid()[counter.getY().getX()][counter.getY().getY()];
                if (target instanceof King) return false;
            }
            return true;
        */
        return ret;
    }
}