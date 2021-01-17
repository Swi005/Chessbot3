package Chessbot3.GameBoard;

import Chessbot3.MiscResources.Move;
import Chessbot3.Pieces.PieceResources.WhiteBlack;
import Chessbot3.Pieces.PieceResources.iPiece;
import Chessbot3.SaveSystem.SaveController;
import Chessbot3.Simulators.AlphaBota;
import Chessbot3.Simulators.MiniMaxBot;
import Chessbot3.Simulators.iBot;
import Chessbot3.sPGN.Ispgn;
import Chessbot3.sPGN.spgn;
import Chessbot3.sPGN.spgnIO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static Chessbot3.GuiMain.Chess.gui;

public class Game {

    private SaveController sc = new SaveController();
    spgnIO ioController = new spgnIO();
    //Liste over tidligere brett. Denne brukes nå du f. eks. vil angre på et trekk.
    private List<Board> previousBoards = new ArrayList<>();

    //En liste over trekkene som er gjort.
    private List<Move> madeMoves = new ArrayList<>();

    //Hvor på listen over tidligere brett det nåværende brettet er.
    //Vanligvis på slutten, men om brukeren har trykket Go Back eller Go Forward trenger vi denne.
    private int boardIndex = 0;

    //Det nåværende brettet.
    private Board currentBoard;

    //Om botten tenker akkurat nå.
    private boolean isBotThinking;

    //En liste over hvilke farger botten skal styre.
    //Om denne kun inneholder BLACK betyr det at botten styrer svart, mens spilleren styrer hvit.
    //Om denne er tom spiller spilleren mot seg selv.
    private ArrayList<WhiteBlack> bots = new ArrayList<>(2);

    //En variabel for å stoppe botten fra å gjøre et trekk, om brukeren har trykket new mens botten tenkte.
    public boolean stop = false;

    //Endre på denne linjen for å bytte ut botten
    private iBot bot = new AlphaBota();

    public Game(){
        //Oppretter et nytt game-objekt, og dermed også et nytt parti.
        //For å starte på nytt kan du bruke newGame(), som resetter alt i dette objektet.
        currentBoard = new Board();
        previousBoards.add(currentBoard.copy());
    }

    //Dette skal egnetlig gjøres av SaveController.java
    public Game(File savedGame){
        //Oppretter et nytt game, basert på en fil.
        spgn save = ioController.GetSave(savedGame);
        Game tempGame = sc.convertToGame(save);
        this.madeMoves = tempGame.madeMoves;
        this.bots = tempGame.bots;
        this.currentBoard = tempGame.currentBoard;
        this.previousBoards = tempGame.previousBoards;
    }

    public Game(Ispgn ispgn){
        currentBoard = new Board();
        Move[] moves = ispgn.GetAllMoves();
        for (Move move : moves){
            playerMove(move);
        }
        // TODO: 07.10.2020 Skriv resten her
    }

    public void goBack(){
        //Går tilbake ett trekk. Om du spiller mot en bot går den tilbake to trekk.
        stop = true;
        gui.makeButtonsGrey();
        int delta;
        if(bots.size() == 1 && !isBotThinking)delta = 2;
        else delta = 1;

        if(boardIndex - delta >= 0){
            boardIndex -= delta;
            currentBoard = previousBoards.get(boardIndex).copy();
            gui.paintPieces(currentBoard);
        }
        else gui.displayTextFieldMessage("Can't go further back!");
    }

    public void goForward(){
        //Går fremover igjen ett trekk, og angrer på anringen til goBack().
        //Om du spiller mot botten går den frem to trekk.
        stop = true;
        gui.makeButtonsGrey();
        int delta;
        if(bots.size() == 1) delta = 2;
        else delta = 1;

        if(boardIndex + delta < previousBoards.size()){
            boardIndex += delta;
            currentBoard = previousBoards.get(boardIndex).copy();
            gui.paintPieces(currentBoard);
        }
        else gui.displayTextFieldMessage("Can't go further forward!");
    }

    public void newGame(){
        //Starter et nytt parti.
        stop = true; //Ber botten om stoppe, om den gjør noe.
        previousBoards.clear();
        madeMoves.clear();
        currentBoard = new Board();
        previousBoards.add(currentBoard.copy());
        boardIndex = 0;
        gui.reset();
        gui.chooseGamemode();
        stop = false; //Gir botter tilatelse til å gjøre ting igjen.
    }

    public void botMove(){
        //Spør en bot om hva det er lurt å gjøre, og gjør trekket.
        //Denne tar IKKE hensyn til om trekket er lovlig eller ikke, så botten bør være ærlig.
        //Botten kan lett byttes ut ved å endre på der den blir kalt opp.
        try {
            isBotThinking = true;
            if(bots.size() == 1) gui.displayTextFieldMessage("Thinking...");
            Move move = bot.findMove(currentBoard);
            if (stop){
                isBotThinking = false;
                return; //Om noen annet har skjedd mens botten tenkte skal den ikke gjøre trekket.
            }
            currentBoard.movePiece(move, false);

            //Oppdaterer lister over brett, trekk, etc
            previousBoards = previousBoards.subList(0, boardIndex+1);
            madeMoves = madeMoves.subList(0, boardIndex);
            previousBoards.add(currentBoard.copy());
            madeMoves.add(move);
            boardIndex += 1;

            gui.paintPieces(currentBoard);
            gui.clearTextField();

            handleWinCondition();
            isBotThinking = false;
        }catch(IllegalStateException x){
            isBotThinking = false;
            gui.clearTextField();
        }
    }

    public Boolean playerMove(Move move){
        //Tar inn et trekk, sjekker om det er lovlig, og gjør trekket på brettet.
        //Legger alle tidligere trekk og brett inn previousBoards og madeMoves.
        //Oppdaterer også Gui.
        if(isBotThinking) return false;
        if(currentBoard.checkMoveLegality(move)) {
            stop = false;
            currentBoard.movePiece(move, true);

            previousBoards = previousBoards.subList(0, boardIndex+1);
            madeMoves = madeMoves.subList(0, boardIndex);
            previousBoards.add(currentBoard.copy());
            madeMoves.add(move);
            boardIndex += 1;

            gui.paintPieces(currentBoard);
            handleWinCondition();
            return true;
        }else {
            gui.displayTextFieldMessage("Not a legal move!");
            return false;
        }
    }
    private void handleWinCondition(){
        //Returnerer true om spillet er ferdig, false ellers.

        //Sjekker om det er matt eller patt.
        Boolean check = currentBoard.checkCheckMate();

        //Sjekker om det er patt, eller om begge spillerene har nøyaktig én brikke igjen.
        if(check == null || (currentBoard.getPieceList(currentBoard.getColorToMove()).size() == 1 && currentBoard.getPieceList(currentBoard.getOppositeColorToMove()).size() == 1)){
            gui.makeButtonsGrey();
            gui.displayPopupMessage("Draw!");
            stop = true;
        }
        //Sjekker om det er matt.
        else if(check){
            gui.makeButtonsGrey();
            gui.displayPopupMessage("Checkmate! " + currentBoard.getOppositeColorToMove() + " wins!");
            stop = true;
        }
        //Hvis ingen ble aktivert er spillet fortsatt i gang.
    }
    //Returnerer true om det er botten som skal gjøre et trekk akkurat nå, false ellers.
    //Om stop=true, altså når noen har trykket en knapp og bedt om å avbryte alt, returnerer denne false og stopper botten.
    public Boolean isBotTurn(){ return !stop && bots.contains(currentBoard.getColorToMove()); }

    public boolean isBotThinking(){ return isBotThinking; }

    //Legger til en farge som bottens skal styre.
    public void addBotColor(WhiteBlack c){ bots.add(c); }

    //Pauser botten. Til bruk i EvE.
    public void pauseTheBot(){ stop = !stop; }

    //Klarerer listen over farger som botten skal gjøre trekk for, og lar heller noen skitne mennesker ta seg av tenkingen.
    public void clearBotColors(){ bots.clear(); }

    //printer det nåværende brettet til konsollen.
    public void printCurrentBoard() { System.out.println(currentBoard); }

    //Returnerer det nåværende brettet.
    public Board getCurrentBoard(){ return currentBoard; }

    //Returner listen over trekk som har blitt gjort så langt.
    public List<Move> getMadeMoves(){ return madeMoves; }

    //Returnerer listen over farger som botten styrer. Vanligivs kun 1 farge.
    public List<WhiteBlack> getBotColors(){ return bots; }

    //Printer hvilken farge som skal flytte.
    public void printTurn() { System.out.println(currentBoard.getColorToMove() + " to move"); }

    //Printer alle trekk som kan bli gjort akkurat nå. Nyttig for debugging.
    public void printPossibleMoves() { for(Move move : currentBoard.genCompletelyLegalMoves()) System.out.println(move); }

    //Printer en liste over brikker som tilhører spilleren som skal flytte. Nyttig for debugging.
    public void printPieces() { for(iPiece pie : currentBoard.getPieceList()) System.out.println(pie); }

    public void printBoardIndex(){ System.out.println(boardIndex); }

    //Printer alle trekkene som har blitt gjort.
    public void printMoveHistory(){ for(Move move : madeMoves) System.out.println(move); }

    //Printer alle tidligere brett.
    public void printBoardHistory() { for(Board bård : previousBoards) System.out.println(bård + "\n"); }

    public void testGetValue(){
        System.out.println(currentBoard.getColorToMove());
        for(Move move : currentBoard.genMoves()){
            System.out.println(move + ": " + currentBoard.getValue(move));
        }
    }

    /**
     * Converts a game to an spgn object
     * @param name - name of current game
     * @return - spgn representation of game
     */
    public Ispgn toSpgn(String name)
    {
        int isPvp = 1;
        WhiteBlack botColor = WhiteBlack.WHITE;
        if (bots.size() > 0){
            isPvp = 0;
            botColor = bots.get(0);
        }
        if(bots.size() > 1)
            System.out.println("Error: Spgn does not support bot v bot at this time");

        spgn game = new spgn(currentBoard.getScore(), isPvp, name, getMadeMoves().toArray(new Move[]{}));

        return game;
    }
    /**
     * Saves game to file
     */
    public void saveGame(String name)
    {
        sc.save(this.toSpgn(name));
    }
}
