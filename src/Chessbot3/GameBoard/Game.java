package Chessbot3.GameBoard;

import Chessbot3.GameBoard.Board;
import Chessbot3.Move;
import Pieces.WhiteBlack;
import Pieces.iPiece;

import java.util.ArrayList;
import java.util.List;

import static Chessbot3.Chess.paintPieces;
import static Pieces.WhiteBlack.WHITE;

public class Game {

    private ArrayList<Board> previousBoards = new ArrayList<>();
    private Board currentBoard;
    private ArrayList<Move> madeMoves = new ArrayList<>();


    public Game(){
        //Oppretter et nytt game-objekt, og dermed også et nytt parti.
        //For å starte på nytt kan du bruke game.newGame(), som resetter alt i dette objektet.

        currentBoard = new Board();
        previousBoards.add(currentBoard.Copy());
    }

    public void goBack(){
        //Går tilbake to trekk, et trekk fra hver spiller. Dvs at begge spillerenes nyeste trekk blir resatt. *insert Bites Za Dusto reference here*
        if(previousBoards.size()>1){
            previousBoards.remove(previousBoards.size()-1);
            previousBoards.remove(previousBoards.size()-1);
            madeMoves.remove(madeMoves.size()-1);
            madeMoves.remove(madeMoves.size()-1);
            currentBoard = previousBoards.get(previousBoards.size()-1).Copy();

            paintPieces();
        }
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
        if(currentBoard.CheckPlayerMove(move)) {
            currentBoard.MovePiece(move);
            previousBoards.add(currentBoard.Copy());
            if(currentBoard.CheckCheckMate() == null) System.out.println("Draw!");
            else if(currentBoard.CheckCheckMate()) System.out.println("Checkmate!");
            madeMoves.add(move);
            paintPieces();
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
        for(Move move : currentBoard.GenMoves(currentBoard.GetColorToMove())) System.out.println(move);;
    }

    //Printer en liste over brikker som tilhører spilleren som skal flytte. Nyttig for debugging.
    public void printPieces() {
        WhiteBlack color = currentBoard.GetColorToMove();
        for(iPiece pie : currentBoard.GetPieceList(color)) System.out.println(pie);
    }

    //Reverserer bretttet. Svart er nå nederst!
    public void reverse() {
        currentBoard.Reverse();
        paintPieces();
    }
}
