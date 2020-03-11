package Pieces;

import Chessbot3.GameBoard.Board;
import Chessbot3.Tuple;

public interface iPiece {

    public getMoves(Board);
    public Boolean isWhite();
    public Boolean isBlack();
    public Boolean isOppositeColor(iPiece p);
    public Boolean canSprint();
    public int getX();
    public int getY();
    public int getValue();
    public char getSymbol();
    public Tuple<Integer, Integer> getCoords();
}