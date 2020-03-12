package Pieces;

import Chessbot3.Move;
import Chessbot3.Tuple;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public interface iPiece {
    public Boolean isWhite();
    public Boolean isBlack();
    public WhiteBlack getColor();
    public Boolean isOppositeColor(iPiece p);
    public Boolean canSprint();

    public Integer getX();
    public Integer getY();
    public Tuple<Integer, Integer> getCoords();
    public BufferedImage getImage();

    public int getValue();
    public Character getSymbol();

    ArrayList<Move> getMoves();
}
