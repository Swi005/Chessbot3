package Chessbot3.GameBoard;

import Chessbot3.GuiMain.Gui;
import Chessbot3.MiscResources.Move;
import Chessbot3.Pieces.WhiteBlack;
import Chessbot3.Pieces.iPiece;
import Chessbot3.bot.Simulate.Tempbot;

import java.util.ArrayList;

import static Chessbot3.GuiMain.Chess.gui;
import static Chessbot3.GuiMain.Gui.*;

public class Game {

    //Liste over tidligere brett. Denne brukes nå du f. eks. vil angre på et trekk.
    private ArrayList<Board> previousBoards = new ArrayList<>();

    //En liste over trekkene som er gjort.
    private ArrayList<Move> madeMoves = new ArrayList<>();

    //Det nåværende brettet.
    private Board currentBoard;

    //En liste over hvilke farger botten skal styre.
    //Om denne kun inneholder BLACK betyr det at botten styrer svart, mens spilleren styrer hvit.
    //Om denne er tom spiller spilleren mot seg selv.
    private ArrayList<WhiteBlack> bots = new ArrayList<>(2);

    //En variabel for å stoppe botten fra å gjøre et trekk, om brukeren har trykket new mens botten tenkte.
    public volatile Boolean stop = false;

    public Boolean reverse = false;

    public Game(){
        //Oppretter et nytt game-objekt, og dermed også et nytt parti.
        //For å starte på nytt kan du bruke newGame(), som resetter alt i dette objektet.
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
        else displayMessage("Can't go further back!");
    }

    public void newGame(){
        //Starter et nytt parti.
        stop = true; //Ber botten om stoppe, om den gjør noe.
        previousBoards.clear();
        madeMoves.clear();
        currentBoard = new Board();
        previousBoards.add(currentBoard);
        paintPieces();
        chooseGamemode();
        stop = false; //Gir botter tilatelse til å gjøre ting igjen.
        if(isBotTurn()) botMove();
    }

    //Printer alle trekkene som har blitt gjort.
    public void printMoveHistory(){
        for(Move move : madeMoves) System.out.println(move);
    }

    //Printer alle tidligere brett.
    public void printBoardHistory() { for(Board bård : previousBoards) System.out.println(bård + "\n"); }

    public void botMove(){
        //Spør en bot om hva det er lurt å gjøre, og gjør trekket.
        //Botten kan lett byttes ut ved å endre på første linje.
        Move move = Tempbot.findRandomMove(currentBoard);
        if(stop) return; //Om noen har trykket på new mens botten tenkte, da skal den ikke gjøre trekket.
        currentBoard.MovePiece(move);
        previousBoards.add(currentBoard.Copy());
        madeMoves.add(move);
        paintPieces();
        if(handleWinCondition()) return;
        else if(isBotTurn()) botMove(); //Om botten spiller mot seg selv. Da må den aktivere seg selv på nytt til noen har vunnet.
    }

    public Boolean playerMove(Move move){
        //Tar inn et trekk, sjekker om det er lovlig, og gjør trekket på brettet.
        //Legger alle tidligere trekk og brett inn previousBoards og madeMoves.
        //Oppdaterer også Gui.
        if(currentBoard.CheckPlayerMove(move)) {
            currentBoard.MovePiece(move);
            previousBoards.add(currentBoard.Copy());
            madeMoves.add(move);
            paintPieces();
            if(handleWinCondition()) return true;
            else if(isBotTurn()) botMove(); //Aktiverer botten, om spilleren spiller mot en bot.
            return true;
        }else {
            System.out.println(currentBoard.GetPassantPos());
            displayMessage("Not a legal move!");
            return false;
        }
    }
    private Boolean handleWinCondition(){
        //En funksjon som returnerer true om spillet er ferdig, false ellers.

        //Sjekker om det er matt eller patt.
        Boolean check = currentBoard.CheckCheckMate();

        //Sjekker om det er patt, eller om begge spillerene har nøyaktig én brikke igjen.
        if(check == null || (currentBoard.GetPieceList(currentBoard.GetColorToMove()).size() == 1 && currentBoard.GetPieceList(currentBoard.GetOppositeColorToMove()).size() == 1)){
            displayMessage("Draw!");
            return true;
        }
        //Sjekker om det er matt.
        if(check){
            displayMessage("Checkmate! " + currentBoard.GetOppositeColorToMove() + " wins!");
            return true;
        }
        return false;
    }

    public Boolean isBotTurn(){
        //Returnerer true om det er botten som skal gjøre et trekk akkurat nå, false ellers.
        //Om stop=true, altså når noen har trykket en knapp og bedt om å avbryte alt, returnerer denne false og stopper botten.
        return !stop && bots.contains(currentBoard.GetColorToMove());
    }

    public void addBotColor(WhiteBlack c){
        //Legger til en farge som bottens skal styre.
        bots.add(c);
    }

    public void clearBotColors(){
        //Klarerer listen over farger som botten skal gjøre trekk for, og lar heller noen skitne mennesker ta seg av tenkingen.
        bots.clear();
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
        for(Move move : currentBoard.GenMoves(currentBoard.GetColorToMove())){
            if(currentBoard.CheckPlayerMove(move)) System.out.println(move);
        }
    }

    //Printer en liste over brikker som tilhører spilleren som skal flytte. Nyttig for debugging.
    public void printPieces() {
        WhiteBlack color = currentBoard.GetColorToMove();
        for(iPiece pie : currentBoard.GetPieceList(color)) System.out.println(pie);
    }

    //Reverserer brettet. Svart er nå nederst!
    public void reverse() {
        reverse = !reverse;
        paintPieces();
    }
}