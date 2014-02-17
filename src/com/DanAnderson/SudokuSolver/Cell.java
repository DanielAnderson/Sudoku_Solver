package com.DanAnderson.SudokuSolver;
import java.util.ArrayList;


public class Cell {

	public ArrayList<Integer> possibleValues;//list of all possible values, originally populated with ints from 1 to 9. once there is only one on the list, it is known
	private boolean updatedAdjacencies;/*once a cell has been found, it needs to eventually have Board.updateAdjacencies on it's x, y
										 *This tells all other cells in it's row, column, box that they can't possibly be the same value as this cell										 
										 *It should only be called once per cell
										 */
	
	//Creates a new cell that has all numbers 1-9 as possibilities
	public Cell()
	{
		updatedAdjacencies=false;
		possibleValues=new ArrayList<Integer>(9);
		for(int i = 1 ; i < 10 ;i++)
		{
			possibleValues.add(i);
		}
	}
	
	//Creates a new cell that only has 1 possibility, assuming
	public Cell(int value)
	{
		updatedAdjacencies=false;
		if(value>0&&value<10){
			possibleValues= new ArrayList<Integer>(1);
			possibleValues.add(value);
		}
		else
		{
			possibleValues = new ArrayList<Integer>(9);
			for(int i = 1 ; i < 10 ;i++)
			{
				possibleValues.add(i);
			}
		}
	}
	
	//creates a new cell, copying the possibleValues of cellToCopy
	public Cell(Cell cellToCopy)
	{
		updatedAdjacencies=false;
		possibleValues=new ArrayList<Integer>();
		for(int i : cellToCopy.possibleValues)
		{
			possibleValues.add(i);
		}
	}
	
	/* PreCondition: The cell value isn't already known, and it is possible for this cell to be the value "i"
	 * PostCondition: The cell has value x, and that is the only element in possibleValues
	 * return: Whether or not the cell could be changed to the value i
	 */
	public boolean setValue(int i)
	{
		if(i==4)
		{
		}
		if(possibleValues.contains((Integer) i))
		{
			possibleValues.clear();
			possibleValues.add(i);
			return true;
		}return false;
	}
	
	/* Precondition: None
	 * Postcondition: No changes made
	 * Return: whether or not the cell's value is known.
	 */
	public boolean isKnown()
	{
		return possibleValues.size()==1;
	}
	
	/* Pre:None
	 * Post: No changes
	 * Return: value if isKnown(), otherwise -1
	 */
	public int getValue()
	{
		if(isKnown())
		{
			return possibleValues.get(0);
			
		}else
		{
			return -1;
		}
	}
	
	/* Pre: None
	 * Post: No changed
	 * Return: whether or not this cell has already had it's adjacencies updated
	 */
	public boolean hasUpdatedAdjacencies()
	{
		return updatedAdjacencies;
		
	}
	
	/* Pre: the adjacencies of this cell have been updated already
	 * Post: updatedAdjacencies is set to true
	 * Return: Nothing
	 */
	public void adjacenciesUpdated()
	{
		updatedAdjacencies=true;
	}
	
	public String toString()
	{
		if(isKnown())
		{
			return Integer.toString(getValue());
		}else 
		{
			return "_";
		}
	}
	
	/* Pre: this cell's value isn't the input value
	 * Post: This cell can no longer be value
	 * Return: none
	 */
	public void removePossibility(int value) {
		if(!isKnown())
		{
			possibleValues.remove((Integer) value);
		}if(isKnown()&&value==getValue())
		{
			//throw new AssertionError("You can't remove the last known possibility!");
		}
		
	}

}
