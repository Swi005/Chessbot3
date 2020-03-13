package Chessbot3.GameBoard;

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
    
}