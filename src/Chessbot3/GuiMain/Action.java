package Chessbot3.GuiMain;

import Chessbot3.GameBoard.Board;
import Chessbot3.MiscResources.Move;
import Chessbot3.MiscResources.Tuple;
import Chessbot3.Pieces.WhiteBlack;
import Chessbot3.Pieces.iPiece;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static Chessbot3.GuiMain.Chess.gui;
import static Chessbot3.GuiMain.Gui.*;

public class Action extends KeyAdapter implements ActionListener {

    //Hver gang brukeren trykker på skjermen eller på tastaturet, blir det inni tekstfeltet sendt til denne variabelen.
    private static String usertext = "";

    //En midlertidig variabel. Hver gang brukeren trykker på en rute, blir denne oppdatert.
    //Har brukeren trykket på to ruter er denne klar til å bli sendt til game.playerMove().
    private static Move pressedMove = new Move(null, null);

    @Override
    public void actionPerformed(ActionEvent e) {
        //Hver gang du trykker på en virtuell knapp blir denne kalt opp. Den forsøker å finne knappen og handle deretter.
        if(e.getSource() == quit) System.exit(0);
        else if(e.getSource() == enter) enter();
        else if(e.getSource() == back) game.goBack();
        else if(e.getSource() == neww) game.newGame();
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
                    if(pressedPiece != null && colorToMove == pressedPiece.getColor()){
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
            }
        } catch (Exception a) {
            a.printStackTrace();
            System.out.println("Unexpected key");
        }
    }

    private void enter(){
        //Hva som skjer hver gang brukeren trykker på enter enten på tastaturet eller skjermen.
        //Her finner vi også er komplett liste over juksekoder.
        usertext = gui.getTextField();
        if (usertext.equals("quit")) System.exit(0);
        else if (usertext.equals("back")) game.goBack();
        else if (usertext.equals("new")) game.newGame();
        else if (usertext.equals("board")) game.printBoard();
        else if (usertext.equals("boards")) game.printBoardHistory();
        else if (usertext.equals("move")) game.printMoves();
        else if (usertext.equals("moves")) game.printMoveHistory();
        else if (usertext.equals("turn")) game.printTurn();
        else if (usertext.equals("pieces")) game.printPieces();
        else if (usertext.equals("paint")) gui.paintPieces();
        else if (usertext.equals("reverse")) gui.reverse();
        else if (usertext.equals("bot")) game.botMove();
    }
}
