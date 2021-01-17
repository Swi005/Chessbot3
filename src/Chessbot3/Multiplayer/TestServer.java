package Chessbot3.Multiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TestServer implements Runnable
{
    private int serverPort = 8080;
    private ServerSocket serverSocket = null;
    private boolean isStopped = false;
    private Thread runningThread = null;
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    @Override
    public void run()
    {
        synchronized(this) {
            runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(!isStopped)
        {
            Socket clientSocket = null;
            try
            {
                clientSocket = serverSocket.accept();
            }
            catch (IOException e)
            {
                if(isStopped())
                {
                    System.out.println("Server Stopped.");
                    break;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            threadPool.execute(new WorkerRunnable(clientSocket));
        }
        this.threadPool.shutdown();
        System.out.println("Server Stopped.");
    }
    public synchronized int getPort(){return serverPort;}
    public synchronized boolean isStopped(){return isStopped;}

    public synchronized void stop()
    {
        isStopped = true;
        try{
            serverSocket.close();
        }catch(IOException e)
        {
            throw new RuntimeException("Error closing server", e);
        }
    }
    private void openServerSocket()
    {
        try
        {
            serverSocket = new ServerSocket(serverPort);
        }catch(IOException e)
        {
            throw new RuntimeException("Cannot open port: " + serverPort + ".", e);
        }
    }
}
