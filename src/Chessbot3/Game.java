package Chessbot3;

import Chessbot3.GameBoard.Board;
import Pieces.iPiece;

import java.util.ArrayList;
import java.util.List;

import static Chessbot3.Chess.paintPieces;
import static Chessbot3.Chess.repaintPiece;
import static Pieces.WhiteBlack.WHITE;

public class Game {

    private List<Board> previousBoards = new ArrayList<>();
    private Board currentBoard;
    private List<Move> madeMoves = new ArrayList<>();


    public Game(){
        currentBoard = new Board();
        //Board copy = currentBoard.Copy();
        //previousBoards.add(copy);
    }

    public void goBack(){
        int size = previousBoards.size();
        if(size>2){
            currentBoard = previousBoards.get(size-2);
            previousBoards = previousBoards.subList(0, size-1);
            madeMoves = madeMoves.subList(0, size-1);
            paintPieces();
        }
            /*
            //madeMoves.remove(madeMoves.size() - 1);
            madeMoves.remove(madeMoves.size() - 1);
            //previousBoards.remove(previousBoards.size()-1);
            previousBoards.remove(previousBoards.size()-1);
            currentBoard = previousBoards.get(previousBoards.size()-1);
            paintPieces();
        }

             */
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
    public void printBoardHistory() { for(Board bård : previousBoards) System.out.println(bård + "\n"); }

    public void botMove(Move move){

    }

    public Boolean playerMove(Move move){
        if(currentBoard.checkPlayerMove(move)) {
            previousBoards.add(currentBoard.Copy());
            ArrayList<Tuple> dirtyLocs = currentBoard.MovePiece(move);
            for (Tuple tup : dirtyLocs) repaintPiece(tup);
            madeMoves.add(move);
            return true;
        }else {
            System.err.println("Not a legal move!");
            return false;
        }
    }

    public void printBoard() { System.out.println(currentBoard); }

    public Board getCurrentBoard(){ return currentBoard; }

    public void printTurn() {
        if(currentBoard.IsWhitesTurn()) System.out.println("White to move");
        else System.out.println("Black to move");
    }

    public void printMoves() {
        for(Move move : currentBoard.GenMoves(WHITE)) System.out.println(move);;
    }
    /* // TODO: 20.03.2020 Rework 
    public void printPieces() {
        if(currentBoard.IsWhitesTurn()) for(iPiece pie : currentBoard.wPieces) System.out.println(pie);
        else for(iPiece pie : currentBoard.bPieces) System.out.println(pie);
    }
    
     */
}
