package com.DanAnderson.SudokuSolver;

public class Box {
	Cell[][] myCells;
	public Box()
	{
		myCells= new Cell[3][3];
	}
	
	/* Pre: myCells[x][y]==null
	 * Post: myCells[x][y]==theCell
	 * Return: None.
	 */
	public void setCell(int x, int y, Cell theCell)
	{
		if(myCells[x][y]==null)
		{
			myCells[x][y]=theCell;
		}
	}
	
	/* Pre: Called from Board.forceSetValue()
	 * Post: getCell(x,y) has the value, "value", regardless of whether it was already set
	 * Return: None.
	 */
	public void forceSetCell(int x, int y, Cell theCell)
	{
		myCells[x][y]=theCell;
	}
	
	/* Pre: none
	 * Post: no changes
	 * Return: myCells[x][y]
	 */
	public Cell getCell(int x, int y)
	{
		return myCells[x][y];
	}

	/* Pre: The cell at [i,j] has a known value
	 * Post: All adjacent cells to the cell at [i,j] no longer have the value as a possible value
	 * Return: none
	 */
	public void removePossibilities(int i, int j) {
		// TODO Auto-generated method stub
		int valueToRemove = getCell(i,j).getValue();
		for(int x = 0 ; x < 3 ;x++)
		{
			for(int y =0 ; y<3;y++)
			{
				if(!((x==i)&&(y==j)))
				{
					getCell(x,y).removePossibility(valueToRemove);
				}
			}
		}
	}
	
	/* Pre: None
	 * Post: All possible values which can only be in one cell, sets those cells to that value
	 * Return: whether or not a new cell was found
	 */
	public boolean onePossible()
	{
		int[] amountPossible = new int[10];
		boolean cellChanged=false;
		for(Cell[] cGroup:myCells)
		{
			for(Cell c:cGroup)
			{
				if(!c.isKnown()){
					for(int i : c.possibleValues)
					{
						amountPossible[i]++;
					}
				}
			}
		}
		for(int i = 1 ; i < 10 ;i++)
		{
			if(amountPossible[i]==1)//then only one cell can be this value
			{
				for(Cell[] cGroup:myCells)
				{
					for(Cell c:cGroup)
					{
						if(c.possibleValues.contains((Integer)i));//once you've found if, set it equal to that value
						{
							if(c.setValue(i))
							{
								cellChanged=true;
							}
						}
					
					}
				}
			}
		}
		return cellChanged;
	}

	/* Pre: No cells null
	 * Post: No change
	 * Return: If there is a contradiction in this box.
	 */
	public boolean hasContradictions() {
		// TODO Auto-generated method stub
		boolean[] hasKnown = new boolean[10];
		for(Cell[] cells : myCells)
		{
			for(Cell c : cells)
			{
				if(c.isKnown()){//we know the value of c
					if(hasKnown[c.getValue()])
					{
						return true;
					}else
					{
						hasKnown[c.getValue()]=true;
					}
				}
			}
		}
		return false;
	}
	
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		for(Cell[] cells:myCells)
		{
			for(Cell c : cells)
			{
				b.append(c);
			}
			b.append("\n");
		}
		return b.toString();
	}
}
