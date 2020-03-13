package Chessbot3;

import Chessbot3.GameBoard.Board;

import java.util.ArrayList;

import static Chessbot3.Chess.paintPieces;

public class Game {

    private ArrayList<Board> previousBoards = new ArrayList<>();
    private Board currentBoard;
    private ArrayList<Move> madeMoves = new ArrayList<>();


    public Game(){
        currentBoard = new Board();
        previousBoards.add(currentBoard);
    }

    public void goBack(){
        if(previousBoards.size()>2){
            madeMoves.remove(madeMoves.size() - 1);
            madeMoves.remove(madeMoves.size() - 1);
            previousBoards.remove(previousBoards.size()-1);
            previousBoards.remove(previousBoards.size()-1);
            currentBoard = previousBoards.get(previousBoards.size()-1);
            paintPieces();
        }
        else System.err.println("Can't go further back!");
    }

    public void newGame(){
        previousBoards.clear();
        madeMoves.clear();
        currentBoard = new Board();
        previousBoards.add(currentBoard);
        paintPieces();
    }

    public ArrayList<Move> getMoveHistory(){ return madeMoves; }

    public void botMove(){ }

    public void playerMove(){ }

    public Board getCurrentBoard(){ return currentBoard; }


}
