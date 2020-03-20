package Pieces;

import Chessbot3.GameBoard.Board;
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

    public Integer getX(Board bård);
    public Integer getY(Board bård);
    public Tuple<Integer, Integer> getCoords(Board bård);
    public BufferedImage getImage();

    public int getValue();
    public Character getSymbol();
    public String toString();

    ArrayList<Move> getMoves(Board bård);
}
