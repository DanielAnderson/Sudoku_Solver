package com.DanAnderson.SudokuSolver;
import java.awt.Point;

public class Board {
	Column[] myColumns;
	Column[] myRows;
	Box[][] myBoxes;
	boolean nullCellsFilled=false;
	
	public Board()
	{
		myColumns = new Column[9];
		myRows = new Column[9];
		myBoxes=new Box[3][3];
		for(int i = 0 ; i < 9 ;i++)
		{
			myColumns[i]=new Column();
			myRows[i]=new Column();
			myBoxes[i/3][i%3]=new Box();
		}
	}
	
	private Board(Board parentBoard, Point pointToAssume)
	{
		//sets up the empty board and initializes data structures
		this();
		//goes through the cells in parentBoard, and copies their values to this board.
		//However, for pointToAssume, set the cell to the first possible value.
		for(int y = 0 ; y<9;y++)
		{
			for(int x = 0 ; x<9;x++)
			{
				Cell newCell;
				if(x==pointToAssume.getX()&&y==pointToAssume.getY()){
					newCell = new Cell(parentBoard.getCell(x, y).possibleValues.get(0));//if it is the cell we want to assume, assume it is the 0th possible value
				}else
				{
					newCell = new Cell(parentBoard.getCell(x, y));
				}
				this.setCell(x, y, newCell);
			}
		}
	}
	
	/* Pre: Board has already been attempted to be solved
	 * Post: It is possible that this.isSolved()==true
	 * Return: The solved version of this
	 */
	public Board bruteForce()
	{
		
		//decides which cell to assume the value of
		Point theCellToAssume = getAssumption();
		
		//generates the new board, identical to this board except for the cell at theCellToAssume
		Board bruteForceBoard=new Board(this, theCellToAssume);
		
		//after assuming, attempt to solve.
		bruteForceBoard.tryToSolve();
		
		
		boolean itsSolved=bruteForceBoard.isSolved();
		boolean itHasContradictions=bruteForceBoard.hasContradictions();
		//if it is solved with no contradictions, we've found a winner, return that board
		if(itsSolved&&!itHasContradictions)
		{
			return bruteForceBoard;
		}
		
		//if it has contradictions, then our assumption was wrong and the cell at theCellToAssume cannot be it's 0th possibility.
		//Remove that possibility and try to solve the current board
		if(itHasContradictions)
		{
			Cell assumedCell = this.getCell((int) theCellToAssume.getX(), (int)theCellToAssume.getY());
			int assumedValue=assumedCell.possibleValues.get(0);
			assumedCell.removePossibility(assumedValue);
			//we know a bit more, try to solve
			tryToSolve();
			if(isSolved())
			{
				return this;
			}else//if our assumption didn't lead to a contradiction yet didn't solve it, we need to assume more.
			{
				return bruteForce();
			}
		}else//has no contradictions, but isn't solved
		{
			return bruteForce();
		}
		
	}
	
	/* Pre: tryToSolve() has already attempted and failed on this board
	 * Post: 
	 * Return: The coordinates at which we should assume a value.
	 */
	private Point getAssumption() {
		// TODO Auto-generated method stub
		return getAssumption(2);
	}
	
	/* Pre: Board not solved
	 * Post: No changes to board
	 * Return: the position of the first cell with <=numPossibilities possibleValues
	 */
	private Point getAssumption(int numPossibilities){
		for(int y = 0 ;y<9;y++)
		{
			for(int x = 0 ; x<9;x++)
			{
				Cell theCell = myRows[y].getCell(x);
				if(!theCell.isKnown()&&theCell.possibleValues.size()<=numPossibilities)
				{
					return new Point(x,y);
				}
			}
		}
		if(numPossibilities<9)
		{
			return getAssumption(numPossibilities+1);
		}else
		{
			return null;
		}

	}
	

	/* Pre: s.length()==81
	 * Post: No changes
	 * Returns: Board with given configuration.
	 */
	public static Board makeBoard(String s)
	{
		if(s.length()!=81)
		{
			return null;
		}
		
		char[] charArray = s.toCharArray();
		Board theAnswer = new Board();
		for(int i = 0 ; i < 81;i++)
		{
			theAnswer.setCell(i%9, i/9, charArray[i]-48);
		}
		return theAnswer;
	}


	/* Pre: All cells created. 
	 * Post: All cells which can be solved for without bruteforce are known
	 * Return: True if the board has been solved, false if it has not or is not possible 
	 */
	public boolean tryToSolve()
	{
		if(!nullCellsFilled)
		{
			fillNullCells();
			nullCellsFilled=true;
		}
		if(eliminateAdjacencies())
		{
			return tryToSolve();
		}
		if(checkColumns())
		{
			return tryToSolve();
		}
		if(checkRows())
		{
			return tryToSolve();
		}
		if(checkBoxes())
		{
			return tryToSolve();
		}
		
		return isSolved();
	}
	

	/* Pre: None
	 * Post: None
	 * Return: Whether or not a box determined the value of a cell
	 */
	private boolean checkBoxes() {
		// TODO Auto-generated method stub
		boolean cellFound = false;
		for(Box[] bRow:myBoxes)
		{
			for(Box b : bRow)
			{
				if(b.onePossible())
				{
					cellFound=true;
				}
			}
		}
		
		return cellFound;
	}


	/* Pre: None
	 * Post: None
	 * Return: whether or not a row determined the value of a cell
	 */
	private boolean checkRows() {
		boolean cellFound=false;
		for(Column c: myRows)
		{
			if(c.onePossible())
			{
				cellFound = true;
			}
		}
		return cellFound;
	}

	/* Pre: None
	 * Post: All columns which had only one cell that could possibly have any certain value have that cell set to the value.
	 * Return: Whether or not a new cell's value was found.
	 */
	private boolean checkColumns() {
		boolean cellFound=false;
		for(Column c: myColumns)
		{
			if(c.onePossible())
			{
				cellFound = true;
			}
		}
		return cellFound;
	}

	/* Pre: None
	 * Post: If cell "c" has a known value yet has not removed it's adjacencies, remove those adjacencies. 
	 * Return: Whether or not a new cell has been found.
	 */
	private boolean eliminateAdjacencies() {
		// TODO Auto-generated method stub
		boolean oneCellFound=false;
		for(int yValue = 0 ; yValue<9;yValue++)
		{
			for(int xValue=0;xValue<9;xValue++)
			{
				Cell currentCell = getCell(xValue,yValue);
				if(currentCell.isKnown()&&!currentCell.hasUpdatedAdjacencies())//if the cell is known and the cell has not updated it's adjacencies, it should update them
				{
					oneCellFound=true;
					updateAdjacencies(xValue, yValue);
				}
			}
		}
		return oneCellFound;
	}

	/* Pre: None
	 * Post: All indices of the columns, rows and boxes that were previously 
	 * 		 filled with null values now have cells with unknown values
	 * Return: None
	 */
	public void fillNullCells() {
		// TODO Auto-generated method stub
		//For all cells, if they are currently null, set them to a cell with an unknown value.
		for(int i = 0 ; i < 9 ; i++)
		{
			for(int j = 0; j<9;j++)
			{
				if(getCell(i,j)==null)
				{
					setCell(i,j,-1);
				}
			}
		}
	}

	/* Pre: None
	 * Post: No changes
	 * Return: Whether or not the puzzle has been solved, that is, if all cells have known values
	 */
	public boolean isSolved() {
		// TODO Auto-generated method stub
		fillNullCells();
		for(Column theColumn :myColumns)//iterate through all columns
		{
			for(int i = 0 ; i < 9;i++)//iterate through all cells within a column
			{
				if(!theColumn.getCell(i).isKnown()){//if a single cell is not known, then the whole board isn't known
					return false;
				}
			}
		}
		return true;
	}

	/* Pre: cell at [xValue, yValue] has known value
	 * Post: all adjacent cells to above noted cell can no longer have the value of the cell
	 * Return: None.
	 */
	private void updateAdjacencies(int xValue, int yValue) {
		//column, row and box that contain the cell [xvalue, yvalue]
		boolean correctX=false;
		boolean correctY=false;
		if(xValue==0||xValue==1)
		{
			correctX=true;
		}
		if(yValue==8)
		{
			correctY=true;
		}
		Column currentRow=myRows[yValue];
		Column currentColumn = myColumns[xValue];
		Box currentBox = myBoxes[xValue/3][yValue/3];
		
		//provided that the current cell at [xvalue, yvalue] is actually known, removes that value from the possible values of other cells
		currentRow.removePossibilities(xValue);
		currentColumn.removePossibilities(yValue);
		currentBox.removePossibilities(xValue%3, yValue%3);

		
		getCell(xValue,yValue).adjacenciesUpdated();

		
	}

	/* Pre: All cells are initialized
	 * Post: No changes
	 * Return: If there is a contradiction in this board.
	 */	
	public boolean hasContradictions()
	{
		for(Box[] boxes :myBoxes)
		{
			for(Box b : boxes)
			{
				if(b.hasContradictions())
				{
					return true;
				}
			}
		}
		for(int i = 0 ; i < 9 ; i++)
		{
			if(myColumns[i].hasContradictions()||myRows[i].hasContradictions())
			{
				return true;
			}
		}
		
		return false;
	}

	/* Pre: myColumns[x].getCell(y)==myRows[y].getCell(x)==myBoxes[x/3][y/3].getCell(x%3,y%3)==null
	 * Post: All of the above getCell() calls refer to the same cell with a value of value, as long as value is in [1,9]
	 * return:none
	 */
	public void setCell(int x, int y, int value)
	{
		Cell theCell = new Cell(value);
		setCell(x,y,theCell);
	}
	

	private void setCell(int x, int y, Cell theCell) {
		// TODO Auto-generated method stub
		if(myColumns[x].getCell(y)==null&&myRows[y].getCell(x)==null&&myBoxes[x/3][y/3].getCell(x%3, y%3)==null)
		{
			myColumns[x].setCell(y, theCell);
			myRows[y].setCell(x, theCell);
			myBoxes[x/3][y/3].setCell(x%3, y%3, theCell);
		}
	}

	/* Pre: None
	 * Post: No changes
	 * Return: How many cells have known values
	 */
	public int getKnownCount()
	{
		int count=0;
		this.fillNullCells();
		for(Column column:myColumns)
		{
			for(Cell c : column.myCells)
			{
				if(c.isKnown())
				{
					count++;
				}
			}
		}
		return count;
	}
	
	/* Pre: Called externally to change the incorrect value of a cell
	 * Post: getCell(x,y) has the value, "value", regardless of whether it was already set
	 * Return: None.
	 */
	public void forceSetCell(int x, int y, int value)
	{
		Cell theCell=new Cell(value);
		myColumns[x].forceSetCell(y, theCell);
		myRows[y].forceSetCell(x, theCell);
		myBoxes[x/3][y/3].forceSetCell(x%3, y%3, theCell);
	}
	
	/* Pre: Cell at x,y exists
	 * Post: No changes
	 * Return: cell at x,y, thus, myColumns[x].getCell(y)
	 */
	public Cell getCell(int x, int y)
	{
		
		return myColumns[x].getCell(y);
	}
	
	/* Pre:For debugging purposes only. All cells exist 
	 * Post: No changes
	 * return: whether for all (x,y), myColumns[x].getCell(y)==myRows[y].getCell(x)==myBoxes[x/3][y/3].getCell(x%3,y%3)
	 */
	public boolean checkIntegrity()
	{
		for(int x=0;x<9;x++)
		{
			for(int y = 0 ; y<9;y++)
			{
				if(!(myColumns[x].getCell(y)==myRows[y].getCell(x)&&myColumns[x].getCell(y)==myBoxes[x/3][y/3].getCell(x%3, y%3)))
				{
					return false;
				}
			}
		}
		return true;
	}
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		for(Column c:myRows)
		{
			s.append(c);
			s.append("\n");
		}
		return s.toString();
	}
}
