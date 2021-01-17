package Chessbot3.Multiplayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

//This is the
public class WorkerRunnable implements Runnable {

    Socket socket;
    public WorkerRunnable(Socket socket)
    {
        this.socket = socket;
    }


    @Override
    public void run()
    {
        try
        {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            //Do Stuff here
            out.close();
            in.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
