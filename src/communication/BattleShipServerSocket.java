package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import view.MyGameGUI;

public class BattleShipServerSocket implements Runnable
{
    private static ServerSocket serverSocket;
    private Socket client;
    private BufferedReader reader;
    private PrintWriter writer;
    private String line;

    public BattleShipServerSocket(int port)
    {
	MyGameGUI.writeToHistory("Opening port: " + port);
	try
	{
	    serverSocket = new ServerSocket(port);
	}
	catch (Exception e)
	{
	    MyGameGUI.writeToHistory("ERROR: Cannot create new server socket: " + e.getMessage());
	}

    }

    public void handleClient()
    {
	client = null;
	try
	{
	    client = serverSocket.accept();
	}
	catch (Exception e)
	{
	    MyGameGUI.writeToHistory("ERROR: client accept failed");
	}

	try
	{
	    reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
	    writer = new PrintWriter(client.getOutputStream(), true);
	}
	catch (IOException e)
	{
	    MyGameGUI.writeToHistory("ERROR: fail creating new reader or writer");
	}

	while (true)
	{
	    try
	    {
		line = reader.readLine(); // read message from client
		String response = Protocol.processReceivedMessage(line); // check if message is a special command or just a normal message
		if (response == null) // normal msg
		{
		    response = line;
		    MyGameGUI.writeToHistory("ENEMY: " + response);
		}
		writer.println(response); // response to client - DO NOT change this		
	    }
	    catch (IOException e)
	    {
		MyGameGUI.writeToHistory("Read failed");
		System.exit(-1);
	    }
	}
    }

    @Override
    public void run()
    {
	handleClient();
    }
}
