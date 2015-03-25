package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import view.MyGameGUI;

public class BattleShipClientSocket implements Runnable
{
    private Socket socket = null;
    private PrintWriter writer = null;
    BufferedReader reader = null;

    public BattleShipClientSocket(String IP, int port)
    {
	try
	{
	    socket = new Socket(IP, port);
	    writer = new PrintWriter(socket.getOutputStream(), true);
	    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	catch (UnknownHostException e)
	{
	    MyGameGUI.writeToHistory("ERROR: Unknown host:");
	}

	catch (IOException e)
	{
	    MyGameGUI.writeToHistory("ERROR: No I/O");
	}
    }

    /**
     * message from client - will include shoot message and normal message
     * 
     * @param message
     */
    public void sendMessage(String message)
    {
	// Send data over socket
	writer.println(message);
	if(Protocol.isNormalMessage(message)) // only write to GUI if this is a normal msg
	{
	    MyGameGUI.writeToHistory("ME   : " + message);
	}

	// process msg from server
	try
	{
	    String line = reader.readLine(); // read msg from server
	    String s = Protocol.processReceivedMessage(line);
	    if(s != null && Protocol.isNormalMessage(s))
	    {
		MyGameGUI.writeToHistory("ENEMY: " + s);
	    }
	    
	}
	catch (IOException e)
	{
	    MyGameGUI.writeToHistory("Read failed");
	    System.exit(-1);
	}
    }

    @Override
    public void run()
    {
	sendMessage("**CLIENT STARTS...");
    }
}
