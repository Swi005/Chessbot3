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
        gui.closeMenu();
        if (e.getSource() == quit) System.exit(0);
        else if (e.getSource() == enter) enter();
        else if (e.getSource() == back) game.goBack();
        else if (e.getSource() == forward) game.goForward();
        else if (e.getSource() == neww) game.newGame();
        else if (e.getSource() == openMenu) gui.openMenu();
        else if (e.getSource() == load) gui.loadGame();
        else if (e.getSource() == save) gui.saveGame();
        else findSquare(e);
    }

    private void findSquare(ActionEvent e) {
        //Holder styr på hva som gjøres hver gang spilleren trykker på en rute på brettet.
        if (game.isBotThinking()) return;
        gui.clearTextField();
        Board bård = game.getCurrentBoard();
        WhiteBlack colorToMove = bård.getColorToMove();

        //Looper igjennom alle knappene i listen til den finner den riktige.
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (e.getSource() == chessBoardSquares[x][y]) {
                    Tuple<Integer, Integer> pos;
                    if (!gui.reverse) pos = new Tuple(x, y);
                    else pos = new Tuple(7 - x, 7 - y);

                    iPiece pressedPiece = bård.getPiece(pos);
                    if (pressedPiece == null) {
                        if (pressedMove.getFrom() != null) {
                            pressedMove.setTo(pos);
                            if (game.playerMove(pressedMove)) {
                                pressedMove = new Move(null, null);
                            }else gui.displayTextFieldMessage("Not a legal move");
                        }
                        gui.makeButtonsGrey();
                    } else if (pressedPiece.getColor() == colorToMove) {
                        pressedMove.setFrom(pos);
                        gui.makeButtonsGrey();
                        gui.lightUpButtons(pos);
                    } else {
                        if (pressedMove.getFrom() != null) {
                            pressedMove.setTo(pos);
                            if (game.playerMove(pressedMove)) {
                                pressedMove = new Move(null, null);
                            }else gui.displayTextFieldMessage("Not a legal move");
                        }
                        gui.makeButtonsGrey();
                    }
                }
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        //Holder styr på hva som skjer hver gang brukeren trykker en tast på tastaturet.
        gui.closeMenu();
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
        else if(usertext.equals("move")) game.printPossibleMoves();
        else if(usertext.equals("turn")) game.printTurn();
        else if(usertext.equals("pieces")) game.printPieces();
        else if(usertext.equals("paint")) gui.paintPieces(game.getCurrentBoard());
        else if(usertext.equals("reverse")) gui.reverse();
        else if(usertext.equals("bot")){ game.stop = false; game.botMove(); }
        else if(usertext.equals("score")) System.out.println(game.getCurrentBoard().getScore());
        else if(usertext.equals("start")) game.stop = false;
        else if(usertext.equals("stop")) game.stop = true;
        else if(usertext.equals("gamemode")) gui.chooseGamemode();
        else if(usertext.equals("value")) game.testGetValue();
        else if(usertext.equals("check")) System.out.println(game.getCurrentBoard().checkCheckMate());
        else if(usertext.equals("positions")) game.getCurrentBoard().printPiecePosition();
        else if(usertext.equals("scores")) game.getCurrentBoard().printScores();
        else if(usertext.equals("moves")) game.getCurrentBoard().printMoveHistory();
        else if(usertext.equals("index")) game.getCurrentBoard().printBoardIndex();
        else if(usertext.equals("castle")) game.getCurrentBoard().printCurrentCastle();
        else if(usertext.equals("castles")) game.getCurrentBoard().printCastles();
        else if(usertext.equals("assert")) game.getCurrentBoard().positionTest();

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
