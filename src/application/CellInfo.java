package application;

public class CellInfo implements Comparable<CellInfo>
{
    /*
     * Game rule: HomeGrid: need to know if a BattleField (cell) is occupied by
     * a ship EnemyGrid: need to know if a BattleField (cell) is hit or is
     * splashed (missed) and then put some value on cell, if isHit == false &&
     * isSplashed == false then cell has not been used First cell on the grid
     * has comlumNo = 1 and rowNo = 0 last cell on the grid has columnNo = 10
     * and rowNo = 9
     */

    private int rowNum;
    private char columnName;
    private int originalRowNo;
    private int originalColumnNo;
    private boolean isHit;
    private boolean isSplashed;

    public CellInfo(int rowNum, char columnName)
    {
	if (rowNum >= 1 && rowNum <= 10 && columnName >= 'A' && columnName <= 'J')
	{
	    this.originalColumnNo = (int) columnName - 64;
	    this.originalRowNo = rowNum - 1;
	    this.rowNum = rowNum;
	    this.columnName = columnName;
	    this.isHit = false;
	    this.isSplashed = false;
	}
	else
	{
	    throw new IllegalArgumentException("Input out of bound");
	}
    }

    public CellInfo(int rowNo, int columnNo)
    {
	if (rowNo >= 0 && rowNo <= 9 && columnNo >= 1 && columnNo <= 10)
	{
	    this.originalColumnNo = columnNo;
	    this.originalRowNo = rowNo;
	    this.rowNum = rowNo + 1;
	    this.columnName = (char) (columnNo + 64);
	    this.isHit = false;
	    this.isSplashed = false;
	}
	else
	{
	    throw new IllegalArgumentException("Input out of bound - row: " + rowNo + " col: " + columnNo);
	}
    }

    public boolean equals(CellInfo cell)
    {
	if (cell != null)
	{
	    return (this.getColumnName() == cell.getColumnName() && this.getRowNum() == cell.getRowNum());
	}
	return false;
    }

    public char getColumnName()
    {
	return columnName;
    }

    public String getLocation()
    {
	return this.columnName + "" + this.rowNum;
    }

    public int getOriginalColumnNo()
    {
	return originalColumnNo;
    }

    public int getOriginalRowNo()
    {
	return originalRowNo;
    }

    public int getRowNum()
    {
	return rowNum;
    }

    public boolean isHit()
    {
	return isHit;
    }

    public boolean isSplashed()
    {
	return isSplashed;
    }

    public void setIsHit()
    {
	this.isHit = true;
    }

    public void setIsSplashed()
    {
	this.isSplashed = true;
    }
    
    public String toString()
    {
	return this.columnName + "" + this.rowNum;
    }

    @Override
    public int compareTo(CellInfo cell)
    {
	if(this.originalColumnNo == cell.originalColumnNo)
	{
	    return(this.originalRowNo - cell.originalRowNo);
	}
	else
	{
	    return this.originalColumnNo - cell.originalColumnNo;
	}
    }

}
