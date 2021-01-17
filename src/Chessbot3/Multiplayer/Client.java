package Chessbot3.Multiplayer;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * Class for handeling comms with server
 */
public class Client //Maybe rename later.
{
    String hostName;
    int port;
    String password;
    Socket socket = null;
    public Client(String hostName, int port, String password)
    {
        this.hostName = hostName;
        this.port = port;
        this.password = password;
    }

}
