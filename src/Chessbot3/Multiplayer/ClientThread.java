package Chessbot3.Multiplayer;

public class ClientThread implements Runnable
{
    String hostName;
    int portNumber;


    public ClientThread(String hostName, int portNumber)
    {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }
    @Override
    public void run() {

    }

    public boolean Connect()
    {
        try
        {

        }
        catch(Exception e)
        {
            return false;
        }
        return false;
    }
}
