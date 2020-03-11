package Chessbot3.Piece;

import Chessbot3.GameBoard.Board;

public abstract class SimplePiece implements iPiece 
{
    Boolean isWhite;
    int value;
    char symbol;
    Boolean canSprint;
    Board gameBoard;


    public Boolean isWhite() { return isWhite; }
    public Boolean isBlack() { return !isWhite; }
    public Boolean isOppositeColor(iPiece p){ return isWhite != p.isWhite(); }

    public Boolean canSprint(){ return canSprint; }

    public int getX(){ return 0; } // TODO: 11.03.2020 Fiks disse!
    public int getY(){ return 0; }

    public int getValue() { return value; }
    public char getSymbol(){ return symbol; }

}