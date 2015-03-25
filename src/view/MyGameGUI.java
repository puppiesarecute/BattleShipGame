package view;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import communication.BattleShipClientSocket;
import communication.BattleShipServerSocket;
import communication.Protocol;
import application.BattleField;
import application.CellInfo;

public class MyGameGUI extends JFrame
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private static JTable myGrid;
    private static JTable enemyGrid;
    private JTextArea chatArea;
    private static JTextArea historyArea;
    private JButton btnStart;
    private String columnNames[] = { "", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
    private String dataValues[][] = { { "    1", "", "", "", "", "", "", "", "", "", "" }, { "    2", "", "", "", "", "", "", "", "", "", "" }, { "    3", "", "", "", "", "", "", "", "", "", "" }, { "    4", "", "", "", "", "", "", "", "", "", "" }, { "    5", "", "", "", "", "", "", "", "", "", "" }, { "    6", "", "", "", "", "", "", "", "", "", "" }, { "    7", "", "", "", "", "", "", "", "", "", "" }, { "    8", "", "", "", "", "", "", "", "", "", "" }, { "    9", "", "", "", "", "", "", "", "", "", "" }, { "    10", "", "", "", "", "", "", "", "", "", "" }, };
    private static BattleField myBattleField;
    private static int shootCount = 0;
    private static BattleShipClientSocket client = null;
    public static String IPAddress = "";
    public static int port = -1;
    private static boolean isMyGameStarted = false;
    private static boolean isOpponentsGameStarted = false;
    private static boolean isMyTurn = true;

    public MyGameGUI()
    {
	try
	{
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}
	catch (Exception e1)
	{
	    e1.printStackTrace();
	}

	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(200, 10, 935, 1000);
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(contentPane);
	contentPane.setLayout(null);

	// constructing the myGrid table
	JLabel lblHome = new JLabel("HOME");
	lblHome.setBounds(10, 11, 46, 14);
	contentPane.add(lblHome);

	myGrid = new JTable();
	createTableModel(myGrid);
	JScrollPane scrollPane = new JScrollPane(myGrid);
	scrollPane.setBounds(10, 33, 440, 417);
	contentPane.add(scrollPane);

	// constructing the enemygrid
	JLabel lblEnemy = new JLabel("ENEMY");
	lblEnemy.setBounds(470, 11, 46, 14);
	contentPane.add(lblEnemy);

	enemyGrid = new JTable();
	createTableModel(enemyGrid);
	JScrollPane scrollPane_1 = new JScrollPane(enemyGrid);
	scrollPane_1.setBounds(470, 33, 440, 417);
	contentPane.add(scrollPane_1);

	// Connect button
	JButton btnConnect = new JButton("Connect");
	// when clicking "connect", create a client
	btnConnect.addActionListener(new ActionListener()
	{
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		while (IPAddress.equals(""))
		{
		    IPAddress = JOptionPane.showInputDialog("Connect to this IP address:");
		    if (IPAddress.equals(""))
		    {
			IPAddress = "localhost";
		    }
		    while (port < 0)
		    {
			try
			{
			    port = Integer.parseInt(JOptionPane.showInputDialog("Please enter port number:"));
			}
			catch (Exception e2)
			{
			    JOptionPane.showMessageDialog(null, "Valid port number is from 1025 to 65535, you idiot!");
			}
		    }
		}

		client = new BattleShipClientSocket(IPAddress, port);
	    }
	});
	btnConnect.setBounds(260, 465, 89, 23);
	contentPane.add(btnConnect);

	// start button
	btnStart = new JButton("Start");
	btnStart.addActionListener(new ActionListener()
	{
	    @Override
	    public void actionPerformed(ActionEvent arg0)
	    {
		startGame();
	    }
	});
	btnStart.setBounds(360, 465, 89, 23);
	contentPane.add(btnStart);

	// chat area
	JLabel lblChat = new JLabel("CHAT");
	lblChat.setBounds(10, 490, 46, 14);
	contentPane.add(lblChat);

	JScrollPane scrollPane_3 = new JScrollPane();
	scrollPane_3.setBounds(10, 510, 440, 110);
	contentPane.add(scrollPane_3);
	chatArea = new JTextArea();
	scrollPane_3.setViewportView(chatArea);

	// send button
	JButton btnSend = new JButton("Send");
	btnSend.addActionListener(new ActionListener()
	{
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		if (!chatArea.getText().equals(""))
		{
		    // TODO: this might change according to protocol
		    sendMessageFromClient(chatArea.getText());
		    chatArea.setText("");
		}
	    }
	});
	btnSend.setBounds(360, 630, 89, 23);
	contentPane.add(btnSend);

	// restart button
	JButton btnRestart = new JButton("Restart");
	btnRestart.addActionListener(new ActionListener()
	{
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		// TODO: add unimplemented method
	    }
	});
	btnRestart.setBounds(360, 670, 89, 23);
	contentPane.add(btnRestart);

	// chat history area
	JScrollPane scrollPane_2 = new JScrollPane();
	scrollPane_2.setBounds(470, 510, 440, 443);
	contentPane.add(scrollPane_2);

	historyArea = new JTextArea();
	historyArea.setText("chat history...");
	scrollPane_2.setViewportView(historyArea);

	JLabel lblChatHistory = new JLabel("CHAT HISTORY");
	lblChatHistory.setBounds(470, 490, 89, 14);
	contentPane.add(lblChatHistory);

	this.setVisible(true);
	this.setResizable(false);

	// add listener on enemy grid - a mouse click on the cell is a shoot
	enemyGrid.addMouseListener(new MouseListener()
	{

	    @Override
	    public void mouseReleased(MouseEvent e)
	    {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void mousePressed(MouseEvent e)
	    {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void mouseExited(MouseEvent e)
	    {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void mouseEntered(MouseEvent e)
	    {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void mouseClicked(MouseEvent e)
	    {
		selectCellOnEnemy(e);

	    }
	});

	// get server port
	String portString = JOptionPane.showInputDialog("Port Number?");
	// create server socket
	Thread t = new Thread(new BattleShipServerSocket(Integer.parseInt(portString)));
	t.start();
    }

    private void createTableModel(JTable table)
    {
	table.setModel(new DefaultTableModel(dataValues, columnNames)
	{
	    private static final long serialVersionUID = 1L;

	    public boolean isCellEditable(int rowIndex, int columnIndex)
	    {
		return false;
	    }
	});
	table.setRowHeight(39);
	table.setCellSelectionEnabled(true);
	table.setDefaultRenderer(Object.class, new CustomCellRenderer());
    }

    /**
     * When clicking on a cell in enemy's grid it's qualified as a shoot This methoda handles the shoot event
     * 
     * @param Mouse
     *            event
     */
    private void selectCellOnEnemy(MouseEvent e)
    {
	// make sure that game is ready (both sides have pressed start game)
	if (!isGameReady())
	{
	    if (!isMyGameStarted)
	    {
		JOptionPane.showMessageDialog(null, "You haven't started the game - Press OK to start game.");
		startGame();
	    }
	    else
	    {
		JOptionPane.showMessageDialog(null, "Your opponent have not started the game.");
	    }
	}
	else
	{
	    // make sure that I can only shoot when it is my turn
	    if (!isMyTurn)
	    {
		JOptionPane.showMessageDialog(null, "Wait for your opponent to shoot.");
	    }
	    else
	    {
		Point p = e.getPoint();
		int rowNo = enemyGrid.rowAtPoint(p);
		int columnNo = enemyGrid.columnAtPoint(p);

		// TODO: this might change according to protocol
		String message = Protocol.shootMessage() + (char) (columnNo + 64) + "" + (rowNo + 1);
		sendMessageFromClient(message);
		isMyTurn = false;
	    }
	}
    }

    private static void drawFire(CellInfo cell, JTable grid)
    {
	new JLabel();
	String txt = "<html>" + "<img src='file:C:\\Users\\ale\\Pictures\\ships\\fire.png' >" + "</html>";
	grid.getModel().setValueAt(txt, cell.getOriginalRowNo(), cell.getOriginalColumnNo());
    }

    private static void drawWater(CellInfo cell, JTable grid)
    {
	new JLabel();
	String txt = "<html>" + "<img src='file:C:\\Users\\ale\\Pictures\\ships\\water.png' >" + "</html>";
	grid.getModel().setValueAt(txt, cell.getOriginalRowNo(), cell.getOriginalColumnNo());
    }

    private static void drawShip(CellInfo cell, JTable grid)
    {
	new JLabel();
	String txt = "<html>" + "<img src='file:C:\\Users\\ale\\Pictures\\ships\\ship.png' >" + "</html>";
	grid.getModel().setValueAt(txt, cell.getOriginalRowNo(), cell.getOriginalColumnNo());
    }

    private static void sendMessageFromClient(String message)
    {
	if (client == null)
	{
	    client = new BattleShipClientSocket(IPAddress, port);
	}
	client.sendMessage(message);
    }

    private void startGame()
    {
	myBattleField = new BattleField();
	// Place random ships on the battlefield
	myBattleField.placeAllShips();
	putShipOnGUI();
	btnStart.setEnabled(false);
	// send message start game
	sendMessageFromClient(Protocol.startGameMessage());
	setMyStartGame(true);
    }

    public static void setOpponentStartGame(boolean isStarted)
    {
	isOpponentsGameStarted = isStarted;
    }

    public static void setMyStartGame(boolean isStarted)
    {
	isMyGameStarted = isStarted;
    }

    public boolean isGameReady()
    {
	return isMyGameStarted && isOpponentsGameStarted;
    }

    /**
     * check my battle field if it contains input cell, if yes I have been hit draw GUI
     * 
     * @param cellInfo
     */
    public static String updateGUIOnHomeGrid(CellInfo cellInfo)
    {
	isMyTurn = true;
	if (myBattleField.getPlan().contains(cellInfo)) // hit
	{
	    CellInfo c = myBattleField.getCell(cellInfo);
	    c.setIsHit();
	    drawFire(cellInfo, myGrid);
	    return Protocol.hitMessage() + cellInfo.getColumnName() + "" + cellInfo.getRowNum();
	}
	else
	// miss
	{
	    drawWater(cellInfo, myGrid);
	    return Protocol.missMessage() + cellInfo.getColumnName() + "" + cellInfo.getRowNum();
	}
    }

    public void putShipOnGUI()
    {
	for (CellInfo cell : myBattleField.getPlan())
	{
	    drawShip(cell, myGrid);
	}
    }

    /**
     * append input string to Chat History text area
     * 
     * @param string
     *            text input
     */
    public static void writeToHistory(String string)
    {
	historyArea.append("\n" + string);
	historyArea.setCaretPosition(historyArea.getText().length() - 1);
    }

    public static String updateGUIonEnemyGrid(CellInfo cellInfo)
    {
	if (cellInfo.isHit())
	{
	    drawFire(cellInfo, enemyGrid);
	    shootCount++;
	    if (shootCount == 17)
	    {
		JOptionPane.showMessageDialog(null, "Congratulations, you won! Click OK to restart the game.");
		// TODO: Unimplemented method to restart the gamewith the OK button
		return Protocol.gameOverMessage();
	    }
	    else
	    {
		return "OUCH!";
	    }
	}
	else if (cellInfo.isSplashed())
	{
	    drawWater(cellInfo, enemyGrid);
	    return "SPLASH!";
	}
	else
	{
	    return "SOMETHING MIGHT BE WRONG HERE...";
	}
    }
}
