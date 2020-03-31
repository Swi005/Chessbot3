package Chessbot3.Pieces.PieceResources;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;

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

    int getValueAt(int x, int y);
    int getCombinedValue(Tuple<Integer, Integer> XY);
    public int getValueAt(Tuple<Integer, Integer> XY);
    public int getInherentValue();

    public Character getSymbol();

    public String toString();

    ArrayList<Move> getMoves(Board bård);
}
