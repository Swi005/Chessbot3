package Chessbot3;

import Chessbot3.GameBoard.Board;
import Pieces.WhiteBlack;
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
        //Oppretter et nytt game-objekt, og dermed også et nytt parti.
        //For å starte på nytt kan du bruke game.newGame(), som resetter alt i dette objektet.

        currentBoard = new Board();
        previousBoards.add(currentBoard.Copy());
    }

    public void goBack(){
        //Går tilbake to trekk, et trekk fra hver spiller. Dvs at begge spillerens nyeste trekk blir resatt. *insert King Crimson reference here*
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
        //Starter et nytt spill.
        previousBoards.clear();
        madeMoves.clear();
        currentBoard = new Board();
        previousBoards.add(currentBoard);
        paintPieces();
    }

    //Printer alle trekkene som har blitt gjort.
    public void printMoveHistory(){
        for(Move move : madeMoves) System.out.println(move);
    }

    //Printer alle tidligere brett.
    public void printBoardHistory() { for(Board bård : previousBoards) System.out.println(bård + "\n"); }

    public void botMove(Move move){

    }

    public Boolean playerMove(Move move){
        //Tar inn et trekk, sjekker om det er lovlig, og gjør trekket på brettet.
        //Legger alle tidligere trekk og brett inn previousBoards og madeMoves.
        //Oppdaterer også GUI.
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

    //printer det nåværende brettet til konsollen.
    public void printBoard() { System.out.println(currentBoard); }

    //Returnerer det nåværende brettet.
    public Board getCurrentBoard(){ return currentBoard; }

    //Printer hvilken farge som skal flytte.
    public void printTurn() {
        System.out.println(currentBoard.GetColorToMove() + " to move");
    }

    //Printer alle trekk som kan bli gjort akkurat nå. Nyttig for debugging.
    public void printMoves() {
        for(Move move : currentBoard.GenMoves(WHITE)) System.out.println(move);;
    }

    //Printer en liste over brikker som tilhører spilleren som skal flytte. Nyttig for debugging.
    public void printPieces() {
        WhiteBlack color = currentBoard.GetColorToMove();
        for(iPiece pie : currentBoard.GetPieceList(color)) System.out.println(pie);
    }
}
