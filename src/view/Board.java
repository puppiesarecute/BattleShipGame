package view;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import application.BattleField;

public class Board
{
    JTable table;
    private Vector<Vector<String>> rowData;
    private Vector<String> colNames;
    BattleField battleField;

    public Board()
    {
	table = new JTable();
	rowData = this.testCreateRowDataWithVector();
	colNames = this.createColNames();
	createTestTableWithVector();
	battleField = new BattleField();
    }

    public void removeAllElements()
    {
	if (rowData != null)
	{
	    rowData.removeAllElements();
	}
	if (colNames != null)
	{
	    colNames.removeAllElements();
	}
	battleField = null;
    }

    private void createTestTableWithVector()
    {
	table.setModel(new DefaultTableModel(rowData, colNames)
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

    private Vector<Vector<String>> testCreateRowDataWithVector()
    {
	Vector<Vector<String>> result = new Vector<Vector<String>>();
	for (int i = 1; i <= 10; i++)
	{
	    result.add(createVector(i));
	}
	return result;
    }

    private Vector<String> createVector(int rowNo)
    {
	Vector<String> result = new Vector<String>();
	result.addElement("    " + rowNo);
	for (int i = 0; i < 10; i++)
	{
	    result.addElement("");
	}
	return result;
    }

    private Vector<String> createColNames()
    {
	Vector<String> columnNames = new Vector<String>();
	columnNames.addElement("");
	columnNames.addElement("A");
	columnNames.addElement("B");
	columnNames.addElement("C");
	columnNames.addElement("D");
	columnNames.addElement("E");
	columnNames.addElement("F");
	columnNames.addElement("G");
	columnNames.addElement("H");
	columnNames.addElement("I");
	columnNames.addElement("J");

	return columnNames;
    }
}
