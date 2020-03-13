package Chessbot3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static Chessbot3.Chess.*;

public class Action extends KeyAdapter implements ActionListener {

    //Hver gang brukeren trykker på skjermen eller på tastaturet, blir det inni tekstfeltet sendt til denne variabelen.
    String usertext = "";

    //En midlertidig variabel. Hver gang brukeren trykker på en rute, blir denne oppdatert. Har brukeren trykket på to ruter er denne klar til å bli sendt til game.playerMove().
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
        for(int x=0; x<8; x++){
            for(int y=0; y<8; y++){
                if(e.getSource() == chessBoardSquares[x][y]) {
                    if(pressedMove.getX() == null){
                        pressedMove.setX(new Tuple(x, y));
                    }else pressedMove.setY(new Tuple(x, y));
                    if(pressedMove.getY() != null){
                        System.out.println("Performing " + pressedMove);
                        game.playerMove(pressedMove);
                        pressedMove = new Move(null, null);
                    }
                }
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        /* Holder styr på hva som skjer hver gang brukeren trykker en tast på tastaturet.
         */
        int key = e.getKeyCode();
        try {
            switch (key) {
                case KeyEvent.VK_ENTER:
                    enter();
                    break;
            }
        } catch (Exception a) {
            System.out.println("Unexpected key");
        }
    }

    private void enter(){
        usertext = textField.getText();
        textField.setText("");
        if (usertext.equals("quit")) System.exit(0);
        else if (usertext.equals("back")) game.goBack();
        else if (usertext.equals("new")) game.newGame();
        else if (usertext.equals("print")) game.printBoard();
    }

}
