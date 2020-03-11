package Chessbot3;

public interface iPiece {

    public Move[] getMoves(Board board);
    public Boolean isWhite();
    public Boolean isBlack();
    public Boolean isOppositeColor(iPiece p);
    public Boolean canSprint();
    public int getX();
    public int getY();
    public int getValue();
    public char getSymbol();
}