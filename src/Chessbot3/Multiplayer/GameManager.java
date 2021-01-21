package Chessbot3.Multiplayer;

import Chessbot3.MiscResources.Move;

public class GameManager
{
    private Player host;
    private Player client;
    private Player currPlayer = null;

    private boolean hostIsWhite;
    private boolean gameInProgress = false;
    private Move lastMove;

    public GameManager(Player host, boolean hostIsWhite)
    {
        this.host = host;
        host.setCurrentGame(this);
        this.hostIsWhite = hostIsWhite;
        if(this.hostIsWhite)
        {
            currPlayer = host;
        }
    }

    public GameManager(Player host, boolean hostIsWhite, Player client)
    {
        this.host = host;
        host.setCurrentGame(this);
        this.client = client;
        this.hostIsWhite = hostIsWhite;
        if(this.hostIsWhite)
            currPlayer = host;
        else
            currPlayer = client;
    }

    public void addClient(Player client){
        if(!gameInProgress)
        {
            if(client.getCurrentGame() == null)
            {
                this.client = client;
                client.setCurrentGame(this);

                if(!hostIsWhite)
                    currPlayer = client;
            }
        }
    }

    public void startGame()
    {
        gameInProgress = true;
    }

    public synchronized boolean isMyTurn(Player plyr)
    {
        if(gameInProgress)
            return plyr.equals(currPlayer);
        else
            return false;
    }
    //TODO: Move to Player class
    public synchronized Move waitMyTurn(Player plyr)
    {
        //TODO: Move to player class and place in loop.
        return null;
    }
    public synchronized void doTurn(Move mv){
        if(gameInProgress)
        {
            if(currPlayer.equals(host))
                currPlayer = client;
            else currPlayer = host;
            lastMove = mv;
        }
    }
    public synchronized Player getPlayerOne()
    {
        return host;
    }
    public synchronized Player getPlayerTwo()
    {
        return client;
    }
}
