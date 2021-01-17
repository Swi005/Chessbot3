package Chessbot3.Multiplayer;

public class Player
{
    GameManager currGame = null;

    public Player()
    {
    }

    public void setCurrentGame(GameManager game)
    {
        if(!currGame.equals(null))
            this.currGame = game;
    }

    public GameManager getCurrentGame()
    {
        return currGame;
    }
}
