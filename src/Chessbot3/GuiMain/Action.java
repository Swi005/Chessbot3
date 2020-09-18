package Chessbot3.GuiMain;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.PieceResources.WhiteBlack;
import Chessbot3.Pieces.PieceResources.iPiece;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static Chessbot3.GuiMain.Chess.gui;
import static Chessbot3.GuiMain.Gui.*;

public class Action extends KeyAdapter implements ActionListener {
    //Action har som jobb å ta imot input fra brukeren, og oversette det til kommandoer til Gui og Game.

    //En midlertidig variabel. Hver gang brukeren trykker på en rute, blir denne oppdatert.
    //Har brukeren trykket på to ruter er denne klar til å bli sendt til game.playerMove().
    private static Move pressedMove = new Move(null, null);

    //Hjelpestrenger, for å kunne parse det brukeren skriver inn til trekk.
    private static String chars = "abcdefgh";
    private static String nums = "12345678";

    @Override
    public void actionPerformed(ActionEvent e) {
        //Hver gang du trykker på en virtuell knapp(inkludert knappene i rutenettet) blir denne kalt opp.
        //Den forsøker å finne knappen og handle deretter.
        gui.removeErrorsFromTextField();
        if(e.getSource() == quit) System.exit(0);
        else if(e.getSource() == enter) enter();
        else if(e.getSource() == back) game.goBack();
        else if(e.getSource() == forward) game.goForward();
        else if(e.getSource() == neww) game.newGame();
        else if(e.getSource() == botstop) game.pauseTheBot();
        else findSquare(e);
    }

    private void findSquare(ActionEvent e) {
        //Holder styr på hva som gjøres hver gang spilleren trykker på en rute på brettet.
        Board bård = game.getCurrentBoard();
        WhiteBlack colorToMove = bård.GetColorToMove();

        //Looper igjennom alle knappene i listen til den finner den riktige.
        for(int x=0; x<8; x++){
            for(int y=0; y<8; y++){
                if(e.getSource() == chessBoardSquares[x][y]) {
                    Tuple pos;
                    if(!gui.reverse) pos = new Tuple(x, y);
                    else pos = new Tuple(7-x, 7-y);

                    iPiece pressedPiece = bård.GetPiece(pos);

                    //Aktiveres når spilleren trykker på en av sine egne brikker.
                    if(pressedPiece != null && colorToMove == pressedPiece.getColor() && !game.isBotThinking()){
                        gui.makeButtonsGrey();
                        gui.lightUpButtons(pos);
                        pressedMove.setX(pos);
                        gui.clearTextField();
                    }
                    //Aktiveres når spilleren allerede har valgt en brikke han vil flytte.
                    else if(pressedMove.getX() != null){

                        //Når spilleren vil flytte til en tom rute, eller ta en fiendtlig brikke. (Eller begge, i en passant)
                        if(pressedPiece == null || pressedPiece.getColor() != colorToMove){
                            gui.clearTextField();
                            pressedMove.setY(pos);
                        }
                    }
                    //Når spilleren har valgt både en brikke å flytte, og en rute å flytte til, blir denne aktivert.
                    if(pressedMove.getY() != null) {

                        //Hvis trekket var lovlig.
                        if(game.playerMove(pressedMove)){
                            gui.makeButtonsGrey();
                            pressedMove = new Move(null, null);
                        }
                        //Om trekket er ulovlig, vil pressedMove huske hvilken brikke spilleren ville flytte.
                        //Hvor han vil flytte til må velges på nytt.
                        else pressedMove.setY(null);
                    }
                }
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        //Holder styr på hva som skjer hver gang brukeren trykker en tast på tastaturet.
        int key = e.getKeyCode();
        try {
            switch (key) {
                case KeyEvent.VK_ENTER:
                    enter();
                    break;
                default: //Sørger for at alle feilmeldinger i tekstfeltet forsvinner når brukeren trykker på en hvilken som helst knapp.
                    gui.removeErrorsFromTextField();
            }
        } catch (Exception a) {
            a.printStackTrace();
            System.out.println("Unexpected key");
        }
    }

    private void enter(){
        //Hva som skjer hver gang brukeren trykker på enter enten på tastaturet eller skjermen.
        //Her finner vi også er komplett liste over juksekoder.
        String usertext = gui.getTextField();
        if (usertext.equals("quit")) System.exit(0);
        else if(usertext.equals("back")) game.goBack();
        else if(usertext.equals("forward")) game.goForward();
        else if(usertext.equals("new")) game.newGame();
        else if(usertext.equals("board")) game.printCurrentBoard();
        else if(usertext.equals("boards")) game.printBoardHistory();
        else if(usertext.equals("move")) game.printPossibleMoves();
        else if(usertext.equals("moves")) game.printMoveHistory();
        else if(usertext.equals("turn")) game.printTurn();
        else if(usertext.equals("pieces")) game.printPieces();
        else if(usertext.equals("paint")) gui.paintPieces(game.getCurrentBoard());
        else if(usertext.equals("reverse")) gui.reverse();
        else if(usertext.equals("bot")) game.botMove();
        else if(usertext.equals("score")) System.out.println(game.getCurrentBoard().GetScore());
        else if(usertext.equals("index")) game.printBoardIndex();
        else if(usertext.equals("start")) game.stop = false;
        else if(usertext.equals("stop")) game.stop = true;
        else if(usertext.equals("pause")) game.pauseTheBot();
        else if(usertext.equals("gamemode")) gui.chooseGamemode();
        else if(usertext.equals("value")) game.testGetValue();

        //Om det brukeren skrev kan tolkes som et trekk (f. eks 'e2e4'), prøver spillet å gjøre trekket.
        else if(isAMove(usertext)) game.playerMove(parse(usertext));

        else gui.displayPopupMessage("Unrecognized command");
    }

    public static Move parse(String input) {
        //Tar en streng fra brukeren og oversetter det til et trekk.
        //f. eks 'e2 e4' blir til new Move((4, 6) (4, 4)).
        input = input.replace(" ", "").toLowerCase();
        Tuple fra = new Tuple(-1, -1);
        fra.setX(chars.indexOf(input.charAt(0)));
        fra.setY(7 - nums.indexOf(input.charAt(1)));

        Tuple til = new Tuple(-1, -1);
        til.setX(chars.indexOf(input.charAt(2)));
        til.setY(7 - nums.indexOf(input.charAt(3)));

        return new Move(fra, til);
    }

    public static boolean isAMove(String input) {
        //Sjekker om det brukeren skrev kan tolkes som et trekk eller ikke.
        //Standard sjakknotasjon (som Nf3) fungerer ikke, man må skrive to koordinater etter hverandre (som g1f3).
        //f. eks 'e2 e4' returnerer true, 'tcfvyguy76ftviyubv7ughbij' returnerer false.
        input = input.replace(" ", "").toLowerCase();
        if(input.length() < 4) return false;
        return (chars.contains(input.substring(0, 1)) && nums.contains(input.substring(1, 2))
                && chars.contains(input.substring(2, 3)) && nums.contains(input.substring(3, 4)));
    }
}
