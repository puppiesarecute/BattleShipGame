package communication;

import view.MyGameGUI;
import application.CellInfo;

/**
 * Protocol: Shoot message: *S + <coordinate> - for example *SA7 Hit message: *H + <coordinate> - for example *HA7 Miss message: *M + <coordinate> - for example *MA7 Surrender message: **S Game over (I win): ***W Normal message: other
 * Surrender is unimplemented
 * @author ale
 *
 */
public class Protocol
{
    public static String startGameMessage()
    {
	return "**Start";
    }

    public static String shootMessage()
    {
	return "*S";
    }

    public static String hitMessage()
    {
	return "*H";
    }

    public static String missMessage()
    {
	return "*M";
    }

    public static String surrenderMessage()
    {
	return "**Surrender";
    }

    public static String gameOverMessage()
    {
	return "***W";
    }

    public static boolean isStartGameMessage(String message)
    {
	boolean result = message.equals("**Start");
	return result;
	// return message.equals("**Start");
    }

    public static boolean isShootMessage(String message)
    {
	return (message.length() >= 4 && message.substring(0, 2).equals(shootMessage()));
    }

    public static boolean isHitMessage(String message)
    {
	return (message.length() >= 4 && message.substring(0, 2).equals(hitMessage()));
    }

    public static boolean isMissMessage(String message)
    {
	return (message.length() >= 4 && message.substring(0, 2).equals(missMessage()));
    }

    public static boolean isSurrenderMessage(String message)
    {
	return message.equals("*Surrender");
    }

    public static boolean isGameOverMessage(String message)
    {
	return message.equals("***W");
    }

    public static boolean isNormalMessage(String message)
    {
	return (!(isStartGameMessage(message) || isShootMessage(message) || isHitMessage(message) || isMissMessage(message) || isSurrenderMessage(message) || isGameOverMessage(message)));
    }

    /**
     * if message is a shoot/hit/miss message, return the cell info from message
     * 
     * @param message
     * @return null if other types of message
     */
    public static CellInfo getCellInfo(String message)
    {
	if (isShootMessage(message) || isHitMessage(message) || isMissMessage(message))
	{
	    char collumnName = message.charAt(2);
	    int rowNo = -1;
	    try
	    {
		rowNo = Integer.parseInt(message.substring(3, 5)); // try to read row no if it's a 2-digit number
	    }
	    catch (Exception e)
	    {
		rowNo = Integer.parseInt(message.substring(3, 4)); // row no is a 1-digit number
	    }
	    CellInfo cell = new CellInfo(rowNo, collumnName);
	    return cell;
	}
	return null;
    }

    /**
     * check if message is a shoot, hit, miss or other special types of msg
     * 
     * @param message
     * @return null is message is normal
     */
    public static String processReceivedMessage(String message)
    {
	CellInfo cell = getCellInfo(message);
	if (cell != null) // message is hit or is miss
	{
	    String response = null;
	    if (isHitMessage(message))
	    {
		cell.setIsHit();
		response = MyGameGUI.updateGUIonEnemyGrid(cell);
	    }
	    else if (isMissMessage(message))
	    {
		cell.setIsSplashed();
		response = MyGameGUI.updateGUIonEnemyGrid(cell);
	    }
	    else if (isShootMessage(message))
	    {
		response = MyGameGUI.updateGUIOnHomeGrid(cell);
	    }
	    return response;
	}
	else
	// other type of message
	{
	    if (isStartGameMessage(message))
	    {
		MyGameGUI.setOpponentStartGame(true);
		return "";
	    }
	    else
	    {
		return null;
	    }	    
	}
    }
}
