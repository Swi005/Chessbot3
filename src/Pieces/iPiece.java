package Pieces;

import Chessbot3.Tuple;

import java.awt.*;

public interface iPiece {
    public Boolean isWhite();
    public Boolean isBlack();
    public Boolean isOppositeColor(iPiece p);
    public Boolean canSprint();

    public Integer getX();
    public Integer getY();
    public Tuple<Integer, Integer> getCoords();

    public int getValue();
    public Character getSymbol();


}
