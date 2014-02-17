package com.DanAnderson.SudokuSolver;
import java.util.ArrayList;


public class Column {
	public Cell[] myCells;
	public Column()
	{
		myCells =new Cell[9];
	}
	
	/* Pre: myCells[index]==null
	 * Post: myCells[index]==theCell
	 * Return: None.
	 */
	public void setCell(int index, Cell theCell)
	{
		if(myCells[index]==null)
		{
			myCells[index]=theCell;
		}
	}
	
	/* Pre: Called from Board.setCell()
	 * Post: getCell(index) has the value, "value", regardless of whether it was already set
	 * Return: None.
	 */
	public void forceSetCell(int index, Cell theCell)
	{
		myCells[index]=theCell;
	}
	public Cell getCell(int x)
	{
		return myCells[x];
	}
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		for(Cell c : myCells)
		{
			s.append(c);
		}
		return s.toString();
	}

	/* Pre: the cell at position has a known value and hasn't updated it's adjacencies yet
	 * Post: No cells in this group have a possible value of myCells[position].getValue(), other than the myCells[position]
	 * Return: None
	 */
	public void removePossibilities(int position) {
		
		Cell theCell=myCells[position];
		int value = theCell.getValue();
		if(theCell.isKnown())
		{
			for(int i = 0 ; i < 9 ; i ++)
			{
				if(i!=position)
				{
					myCells[i].removePossibility(value);
				}
			}
		}
		
	}
	/* Pre: None
	 * Post: All possible values which can only be in one cell, sets those cells to that value
	 * Return: whether or not a new cell was found
	 */
	public boolean onePossible() {
		// TODO Auto-generated method stub
		int[] amountPossible = new int[10];
		boolean cellChanged=false;
		for(Cell c : myCells)
		{
			if(!c.isKnown()){
				for(int i : c.possibleValues)
				{
					amountPossible[i]++;
				}
			}
		}
		for(int i = 1 ; i<10 ;i++)
		{
			if(amountPossible[i]==1)//then only one cell can be this value
			{
				for(Cell c :myCells)//go through all cells- find the cell which can be  of the value i
				{
					if(c.possibleValues.contains((Integer)i))//once you've found if, set it equal to that value
					{
						if(c.setValue(i))
						{
							cellChanged=true;
						}
					}
				}
			}
		}
		return cellChanged;
		
	}

	/* Pre: No cells null
	 * Post: No change
	 * Return: If there is a contradiction in this column
	 */
	public boolean hasContradictions() {
		// TODO Auto-generated method stub
		boolean[] hasKnown = new boolean[10];
		for(Cell c:myCells)
		{
			if(c.isKnown())
			{
				if(hasKnown[c.getValue()]){
					return true;
				}else
				{
					hasKnown[c.getValue()]=true;
				}
			}
		}
		return false;
	}
}
