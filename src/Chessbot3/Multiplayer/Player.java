package Chessbot3.Multiplayer;

public class Player
{
    GameManager currGame = null;

    private String userName;

    public Player(String userName)
    {
        this.userName = userName;
    }

    public void setCurrentGame(GameManager game)
    {
        if(currGame != null)
            this.currGame = game;
    }

    public GameManager getCurrentGame()
    {
        return currGame;
    }
    public String getUserName()
    {
        return userName;
    }
}
