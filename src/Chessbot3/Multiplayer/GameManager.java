package Chessbot3.Multiplayer;

import Chessbot3.MiscResources.Move;

public class GameManager
{
    Player host;
    Player client;
    Player currPlayer = null;

    boolean hostIsWhite;
    boolean gameInProgress = false;
    Move lastMove;

    public GameManager(Player host, boolean hostIsWhite)
    {
        this.host = host;
        this.hostIsWhite = hostIsWhite;
        if(this.hostIsWhite)
        {
            currPlayer = host;
        }
    }

    public GameManager(Player host, boolean hostIsWhite, Player client)
    {
        this.host = host;
        this.client = client;
        this.hostIsWhite = hostIsWhite;
        if(this.hostIsWhite)
            currPlayer = host;
        else
            currPlayer = client;
    }

    public void addClient(Player client) throws Exception {
        if(!gameInProgress)
        {
            if(client.getCurrentGame().equals(null))
            {
                this.client = client;
                client.setCurrentGame(this);

                if(!hostIsWhite)
                    currPlayer = client;
            }else
                throw new Exception("Error this Client is already part of a game");

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
}
