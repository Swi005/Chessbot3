package Chessbot3.GameBoard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static Chessbot3.Chess.game;
import static Chessbot3.Chess.textField;

public class Action extends KeyAdapter implements ActionListener {
    String usertext = "";

    @Override
    public void actionPerformed(ActionEvent e) { };

    public void keyPressed(KeyEvent e){
        /* Holder styr på hva som skjer hver gang brukeren trykker en tast på tastaturet.
         */
        int key = e.getKeyCode();
        try {
            switch (key) {
                case KeyEvent.VK_ENTER:
                    usertext = textField.getText();
                    textField.setText("");
                    if (usertext.equals("quit")) System.exit(0);
                    else if (usertext.equals("back")) game.goBack();
                    else if (usertext.equals("new")) game.newGame();
                    break;
            }
        } catch(Exception a) {
            System.out.println("Unexpected button");
        }
    }

}
