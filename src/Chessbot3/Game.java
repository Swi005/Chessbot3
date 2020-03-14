package Chessbot3;

import Chessbot3.GameBoard.Board;

import java.util.ArrayList;

import static Chessbot3.Chess.paintPieces;
import static Chessbot3.Chess.repaintPiece;

public class Game {

    private ArrayList<Board> previousBoards = new ArrayList<>();
    private Board currentBoard;
    private ArrayList<Move> madeMoves = new ArrayList<>();


    public Game(){
        currentBoard = new Board();
        Board copy = currentBoard.Copy();
        previousBoards.add(copy);
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

    public void printMoveHistory(){
        for(Move move : madeMoves) System.out.println(move);
    }
    public void printBoardHistory(){

        for(Board bård : previousBoards) {
            System.out.println(bård);
            System.out.println();
        }
    }

    public void botMove(){ }

    public void playerMove(Move move){
        //if(currentBoard.checkPlayerMove(move)) {
            System.out.println();
            previousBoards.add(currentBoard.Copy());
            ArrayList<Tuple> dirtyLocs = currentBoard.MovePiece(move);
            for (Tuple tup : dirtyLocs) repaintPiece(tup);
            madeMoves.add(move);
        //}else System.err.println("Not a legal move!");
    }

    public void printBoard() { System.out.println(currentBoard); }

    public Board getCurrentBoard(){ return currentBoard; }


}
