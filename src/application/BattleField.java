package application;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class BattleField
{
    public static final int TOTAL_SHIP = 17;
    private Set<CellInfo> plan;

    public BattleField()
    {
	plan = new TreeSet<CellInfo>();
    }

    public Set<CellInfo> getPlan()
    {
	return plan;
    }

    public CellInfo getCell(CellInfo cell)
    {
	CellInfo result = null;
	if (!this.plan.isEmpty() && this.plan.contains(cell))
	{
	    Iterator<CellInfo> itr = this.plan.iterator();
	    while (itr.hasNext())
	    {
		CellInfo localCell = itr.next();
		if (localCell.equals(cell))
		{
		    return localCell;
		}
	    }
	}

	return result;
    }

    public void placeAllShips()
    {
	Random r = new Random();
	if (this.plan.isEmpty())
	{
	    plan.addAll(placeARandomShip(5, r.nextBoolean()));
	    plan.addAll(placeARandomShip(4, r.nextBoolean()));
	    plan.addAll(placeARandomShip(3, r.nextBoolean()));
	    plan.addAll(placeARandomShip(3, r.nextBoolean()));
	    plan.addAll(placeARandomShip(2, r.nextBoolean()));
	}

	// make sure the total number of cells with ship is TOtAL_SHIP
	if (plan.size() != TOTAL_SHIP)
	{
	    System.err.println("Total number of cells occupied by ship is wrong!!!");
	}
    }

    public Set<CellInfo> placeARandomShip(int shipSize, boolean isVerticle)
    {
	Random r = new Random();
	Set<CellInfo> result = new TreeSet<CellInfo>();

	// get a random row no between 0 and 9
	int rowNo = r.nextInt(10);
	// get a random col no between 1 and 10
	int columnNo = r.nextInt(10) + 1;

	// if random cell is in the first quarter of the grid
	if (rowNo <= (10 - shipSize) && columnNo <= (11 - shipSize))
	{
	    while (result.size() < shipSize)
	    {
		if (isVerticle)
		{
		    CellInfo cell = new CellInfo(rowNo++, columnNo);
		    result.add(cell);
		}
		else
		{
		    CellInfo cell = new CellInfo(rowNo, columnNo++);
		    result.add(cell);
		}
	    }
	}
	// if random cell is in the 2nd quarter of the grid
	else if (rowNo <= (10 - shipSize) && columnNo > (11 - shipSize))
	{
	    while (result.size() < shipSize)
	    {
		if (isVerticle)
		{
		    CellInfo cell = new CellInfo(rowNo++, columnNo);
		    result.add(cell);
		}
		else
		{
		    CellInfo cell = new CellInfo(rowNo, columnNo--);
		    result.add(cell);
		}
	    }
	}
	// if random cell is in the 3rd quarter of the grid
	else if (rowNo > (10 - shipSize) && columnNo <= (11 - shipSize))
	{
	    while (result.size() < shipSize)
	    {
		if (isVerticle)
		{
		    CellInfo cell = new CellInfo(rowNo--, columnNo);
		    result.add(cell);
		}
		else
		{
		    CellInfo cell = new CellInfo(rowNo, columnNo++);
		    result.add(cell);
		}
	    }
	}
	// if random cell is in the 4th quarter of the grid
	else if (rowNo > (10 - shipSize) && columnNo > (11 - shipSize))
	{
	    while (result.size() < shipSize)
	    {
		if (isVerticle)
		{
		    CellInfo cell = new CellInfo(rowNo--, columnNo);
		    result.add(cell);
		}
		else
		{
		    CellInfo cell = new CellInfo(rowNo, columnNo--);
		    result.add(cell);
		}
	    }
	}

	// check ship placement
	if (isShipPlacementOk(result))
	{
	    plan.addAll(result);
	}
	else
	{
	    result.clear();
	    result = placeARandomShip(shipSize, isVerticle);
	}

	return result;
    }

    private boolean isShipPlacementOk(Set<CellInfo> set)
    {
	if (plan.size() > 0)
	{
	    for (CellInfo cellInfo : set)
	    {
		if (plan.contains(cellInfo))
		{
		    // cannot put ship here because it overlaps old ships
		    return false;
		}
	    }
	}
	return true;
    }

    public int size()
    {
	return this.plan.size();
    }
}
