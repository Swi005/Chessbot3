package Tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import Chessbot3.MiscResources.Move;
import Chessbot3.Multiplayer.*;
import org.junit.jupiter.api.Test;

public class GameManagerTest
{
    @Test
    public void TestAddClientsToGame()
    {
        //Setup
        Player p1 = new Player("player1");
        Player p2 = new Player("player2");
        GameManager game = new GameManager(p1, true);

        //test
        game.addClient(p2);

        //Checks
        assertEquals(p2, game.getPlayerTwo());
        //assertEquals(game ,p2.getCurrentGame());
    }

    @Test
    public void TestTurnTaking()
    {
        //Setup
        Player p1 = new Player("Player1");
        Player p2 = new Player("Player2");
        GameManager game = new GameManager(p1, true);

        //test
        game.addClient(p2);

        game.startGame();
        game.doTurn(null);
        game.doTurn(null);
        game.doTurn(null);

        //check
        assertEquals(true, game.isMyTurn(p2));
    }
}
