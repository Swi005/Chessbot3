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

import javax.swing.text.html.MinimalHTMLWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static Chessbot3.GuiMain.Chess.gui;

public class Game {

    private SaveController sc = new SaveController();
    spgnIO ioController = new spgnIO();

    //Det nåværende brettet.
    private Board currentBoard;

    //Om botten tenker akkurat nå.
    private boolean isBotThinking = false;

    //En liste over hvilke farger botten skal styre.
    //Om denne kun inneholder BLACK betyr det at botten styrer svart, mens spilleren styrer hvit.
    //Om denne er tom spiller spilleren mot seg selv.
    private ArrayList<WhiteBlack> bots = new ArrayList<>(2);

    //En variabel for å fortelle botten om den skal gjøre noe eller ikke.
    //Main-funksjonen sjekker hele tiden om denne er false, og hvis ja, ber den botten finne og gjøre et trekk.
    //Denne endres hele tiden, for å kontrollere om vi vil at botten skal gjøre noe, eller ikke.
    public boolean stop = true;

    //Endre på denne linjen for å bytte ut botten
    private iBot bot = new MiniMaxBot();

    public Game(){
        //Oppretter et nytt game-objekt, og dermed også et nytt parti.
        //For å starte på nytt kan du bruke newGame(), som resetter alt i dette objektet.
        currentBoard = new Board();
    }

    //Dette skal egnetlig gjøres av SaveController.java
    public Game(File savedGame){
        //Oppretter et nytt game, basert på en fil.
        spgn save = ioController.GetSPGN(savedGame);
        Game tempGame = sc.convertToGame(save);
        this.bots = tempGame.bots;
        this.currentBoard = tempGame.currentBoard;
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
        //Tar tilbake ett trekk og resetter det til en tidligere tilstand.
        //Om det er PvE går du tilbake 2 trekk.
        try{
            stop = true;
            gui.makeButtonsGrey();
            currentBoard.undoMove(false);
            if(bots.size() == 1 && !isBotThinking) currentBoard.undoMove(false);
        }
        catch(IllegalStateException x){
            gui.displayTextFieldMessage("Cannot go further back!");
        }
        finally {
            gui.paintPieces(currentBoard);
            updateStop();
        }
    }

    public void goForward(){
        //Går fremover igjen, om du har ombestemt ombestemmingen fra goBack().
        //Om det er PvE går du frem to trekk.
        try{
            stop = true;
            gui.makeButtonsGrey();
            currentBoard.goForward();
            if (bots.size() == 1) currentBoard.goForward();
        }
        catch (IllegalStateException x){
            gui.displayTextFieldMessage("Cannot go further forward!");
        }
        finally {
            gui.paintPieces(currentBoard);
            updateStop();
        }
    }

    public void newGame(){
        //Starter et nytt parti.
        stop = true;
        currentBoard = new Board();
        gui.reset();
        gui.chooseGamemode();
        if (bots.contains(currentBoard.getColorToMove())){
            stop = false;
        }
    }

    public void botMove(){
        //Spør en bot om hva det er lurt å gjøre, og gjør trekket.
        //Denne tar IKKE hensyn til om trekket er lovlig eller ikke, så botten bør være ærlig.
        //Botten kan lett byttes ut ved å endre på der den blir kalt opp.
        if (stop) throw new IllegalArgumentException("Bot was stopped, but botMove was called anyway");
        try {
            isBotThinking = true;
            if(bots.size() == 1) gui.displayTextFieldMessage("Thinking...");
            Move move = bot.findMove(currentBoard);
            if (stop){
                isBotThinking = false;
                return; //Om noen annet har skjedd mens botten tenkte skal den ikke gjøre trekket.
            }
            currentBoard.movePiece(move);

            gui.paintPieces(currentBoard);
            gui.clearTextField();

            handleWinCondition();
            isBotThinking = false;

        }catch(IllegalStateException x){
            //Botten kaster en exception om stop ble true i løpet av søket, dvs at noe avbrøt botten.
            isBotThinking = false;
            gui.clearTextField();
        }
    }

    public Boolean playerMove(Move move){
        //Tar inn et trekk, sjekker om det er lovlig, og gjør trekket på brettet.
        //Legger alle tidligere trekk og brett inn previousBoards og madeMoves.
        //Oppdaterer også Gui.
        if(isBotThinking) return false;
        stop = true;
        if(currentBoard.checkMoveLegality(move)) {
            currentBoard.movePiece(move, true, true);
            gui.paintPieces(currentBoard);
            handleWinCondition();
            return true;
        }else {
            gui.displayTextFieldMessage("Not a legal move!");
            return false;
        }
    }
    private void handleWinCondition(){
        //Sjekker om spillet er over, sender relevante instrukser til gui, og oppdaterer bottens status.

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
        //Hvis vi kommer hit er spillet fortsatt igang. Da må vi sjekke om det er bottens tur og aktivere den.
        updateStop();
    }
    //Returnerer true om det er botten som skal gjøre et trekk akkurat nå, false ellers.
    //Om stop=true, altså når noen har trykket en knapp og bedt om å avbryte alt, returnerer denne false og stopper botten.
    public Boolean isBotTurn(){ return !stop && bots.contains(currentBoard.getColorToMove()); }

    public void updateStop(){ stop = !bots.contains(currentBoard.getColorToMove()); }

    public boolean isBotThinking(){ return isBotThinking; }

    //Legger til en farge som bottens skal styre.
    public void addBotColor(WhiteBlack c){ bots.add(c); }

    //Klarerer listen over farger som botten skal gjøre trekk for, og lar heller noen skitne mennesker ta seg av tenkingen.
    public void clearBotColors(){ bots.clear(); }

    //printer det nåværende brettet til konsollen.
    public void printCurrentBoard() { System.out.println(currentBoard); }

    //Returnerer det nåværende brettet.
    public Board getCurrentBoard(){ return currentBoard; }

    //Returnerer listen over farger som botten styrer. Vanligivs kun 1 farge.
    public List<WhiteBlack> getBotColors(){ return bots; }

    //Printer hvilken farge som skal flytte.
    public void printTurn() { System.out.println(currentBoard.getColorToMove() + " to move"); }

    //Printer alle trekk som kan bli gjort akkurat nå. Nyttig for debugging.
    public void printPossibleMoves() { for(Move move : currentBoard.genCompletelyLegalMoves()) System.out.println(move); }

    //Printer en liste over brikker som tilhører spilleren som skal flytte. Nyttig for debugging.
    public void printPieces() { for(iPiece pie : currentBoard.getPieceList()) System.out.println(pie); }

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

        spgn game = new spgn(currentBoard.getScore(), 0, isPvp, name, currentBoard.getPreviousMoves().toArray(new Move[]{}));

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
