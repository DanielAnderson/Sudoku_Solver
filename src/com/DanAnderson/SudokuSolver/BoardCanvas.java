package com.DanAnderson.SudokuSolver;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class BoardCanvas extends Canvas implements ActionListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  BufferedImage[] numberImages;
	private  int canvasWidth;
	private  int canvasHeight;
	private Board myBoard;
	private JButton solveButton;
	private JButton saveButton;
	private JButton newBoardButton;
	private int boardHeight;
	private int mySelection;
	private int heightOfSelections;
	private int gapwidth=10;
	private AbstractButton bruteForceButton;
	
	private static JButton loadButton;
	
	public static void main(String[] args)
	{
		BoardCanvas theCanvas = new BoardCanvas();
	}
	
	//Sets everything up
	public BoardCanvas()
	{
		myBoard=new Board();
		setupImages();
		setupCanvas();
		
		
		
		
		JPanel thePanel = setupPanel();
		
		JFrame theFrame = setupJFrame();
		
		theFrame.add(this);
		theFrame.add(thePanel);
		
		theFrame.pack();
		theFrame.setVisible(true);
	}

	
	//sets up the panel as well as the components in the panel
	private JPanel setupPanel() {
		// TODO Auto-generated method stub
		solveButton = new JButton("Solve");
		solveButton.addActionListener(this);
		
		bruteForceButton = new JButton("Brute Force");
		bruteForceButton.addActionListener(this);
		bruteForceButton.setEnabled(false);
		
		newBoardButton = new JButton("Custom Board");
		newBoardButton.addActionListener(this);
		
		loadButton = new JButton("Load");
		loadButton.addActionListener(this);
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		
		JPanel thePanel = new JPanel();
		thePanel.setLayout(new BoxLayout(thePanel, BoxLayout.Y_AXIS));
		
		
		thePanel.add(solveButton);
		thePanel.add(javax.swing.Box.createVerticalStrut(5));
		thePanel.add(bruteForceButton);
		thePanel.add(javax.swing.Box.createVerticalStrut(5));

		thePanel.add(newBoardButton);
		thePanel.add(javax.swing.Box.createVerticalStrut(5));

		thePanel.add(loadButton);
		thePanel.add(javax.swing.Box.createVerticalStrut(5));

		thePanel.add(saveButton);		
		thePanel.add(javax.swing.Box.createVerticalStrut(347));

		
		return thePanel;
	}

	//sets up the basic properties of the main frame
	private static JFrame setupJFrame() {
		// TODO Auto-generated method stub
		JFrame f = new JFrame();
		f.setLayout(new FlowLayout());
		f.setTitle("Sudoku Solver");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return f;
	}

	//sets up the basic properties of the canvas
	private  void setupCanvas() {
		// TODO Auto-generated method stub
		canvasWidth=numberImages[1].getWidth()*9+8;//width of the canvas
		boardHeight = numberImages[1].getHeight()*9+8;//height of the sudoku board itself
		heightOfSelections=boardHeight+gapwidth;
		canvasHeight=heightOfSelections+numberImages[1].getHeight();//height of the whole canvas including the numbers at the bottom
		
		this.setSize(canvasWidth, canvasHeight);
		this.setBackground(Color.white);
	}

	//sets up the images for the numbers
	private void setupImages()
	{
		numberImages=new BufferedImage[10];
		for(int i = 1;i<10;i++)
		{
			try{
				File numberFile = new File("numbers/"+i+".PNG");
				numberImages[i]=ImageIO.read(numberFile);
			}catch(IOException e)
			{
				e.printStackTrace();
			}
		}

	}

	
	//repaints the canvas. Done pretty much whenever myBoard is changed or references a different board.
	public void paint(Graphics g)
	{
		int gapBetweenLines=numberImages[1].getWidth()+1;
		
		for(int i = 1; i < 9;i++)
		{
			if(i%3==0)
			{
				g.setColor(Color.black);
			}else
			{
				g.setColor(Color.gray);
			}
			g.drawLine(i*gapBetweenLines-1, 0, i*gapBetweenLines-1,boardHeight-1);
			g.drawLine(0, i*gapBetweenLines-1, canvasWidth-1, i*gapBetweenLines-1);
		}
		for(int i = 0 ; i < 9;i++)
		{
			for(int j = 0 ; j<9;j++)
			{
				if(myBoard.getCell(i, j)!=null)
				{
					int pictureIndex=myBoard.getCell(i, j).getValue();
					if(pictureIndex>0&&pictureIndex<10)
					{
						g.drawImage(numberImages[pictureIndex], i*gapBetweenLines, j*gapBetweenLines, this);
					}

				}
			}
		}
		for(int i = 0 ;i < 9 ; i++)
		{
			g.drawImage(numberImages[i+1], i*canvasWidth/9, boardHeight+10,this);
		}
		if(mySelection!=0)
		{
			g.setColor(Color.red);
			g.drawRect(((mySelection-1)*49), heightOfSelections, numberImages[1].getWidth(), numberImages[1].getHeight()-1);
		}
		
		g.setColor(Color.gray);
		g.fillRect(0, boardHeight, canvasWidth, 10);

	}

	//determines which operation to do based on which button was pressed.
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==loadButton)
		{
			loadFile();
		}else if(arg0.getSource()==solveButton)
		{
			solveBoard();
		}
		else if(arg0.getSource()==saveButton)
		{
			saveBoard();
		}else if(arg0.getSource()==newBoardButton)
		{
			makeCustomBoard();
		}else if(arg0.getSource()==bruteForceButton)
		{
			bruteForce();
		}
		if(myBoard.isSolved()){
			solveButton.setEnabled(false);
			bruteForceButton.setEnabled(false);
		}else
		{
			solveButton.setEnabled(true);
		}

	}

	//Goes through the process of trying to solve a board without using brute force methods
	private void solveBoard() {
		// TODO Auto-generated method stub
		
		if(myBoard.getKnownCount()<17)
		{
			JOptionPane.showMessageDialog(this, "No sudoku puzzle with less than 17 clues has a unique solution, please input at least 17 clues.", "Oops", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(myBoard.hasContradictions())
		{
			JOptionPane.showMessageDialog(this, "There appears to be contradictions in this puzzle. Make sure no rows, columns, or boxes have more than 1 of any number","Oops!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//if we get here, we are actually trying to solve it.
		mySelection=0;
		this.removeMouseListener(this);

		//try{
			myBoard.tryToSolve();
//		}catch(AssertionError e)
//		{
//			
//		}

		if(!myBoard.isSolved())
		{
			bruteForceButton.setEnabled(true);
		}
		repaint();

	}

	//goes through the process of trying to brute force the puzzle
	private void bruteForce() {
		try
		{
			Board newBoard=myBoard.bruteForce();
			myBoard=newBoard;
			repaint();
		}catch(StackOverflowError e)//some puzzles are so tough that this algorithm throws a StackOverflowError. If so, it can't be solved this way
		{
			JOptionPane.showMessageDialog(this, "Sorry, this puzzle couldn't be brute-forced!","Oops!", JOptionPane.ERROR_MESSAGE);
		}
		
	}

	
	private void makeCustomBoard() {
		if(!areYouSure("Are you sure? This will delete the current board"))
		{
			return;
		}
		bruteForceButton.setEnabled(false);
		
		addMouseListener(this);
		myBoard=new Board();
		repaint();
	}

	
	//Saves the current board as a .sud file
	private void saveBoard() {
		// TODO Auto-generated method stub
		File saveLocation = new File("Sudoku Saves");
		if(!saveLocation.exists())
		{
			saveLocation.mkdir();
		}
		
		JFileChooser fc = new JFileChooser(saveLocation);
		fc.setFileFilter(new FileNameExtensionFilter("Sudoku Boards","sud"));
		int returnValue=fc.showSaveDialog(getParent());
		if(returnValue==JFileChooser.APPROVE_OPTION)
		{
			File f = fc.getSelectedFile();
			if(!f.toString().contains("sud"))
			{
				f=new File(f.toString()+".sud");
			}
			
			if(f.exists()&&!areYouSure("Are you sure? This will overwrite " +f.toString())){
				return;
			}
			try {
				f.createNewFile();
				BufferedWriter bWriter = new BufferedWriter( new FileWriter(f));
				String boardString = myBoard.toString();
				bWriter.write(boardString);
				bWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	
	//loads a file with a .sud extension (really just a txt file with a different extension) and makes that the current board.
	private void loadFile() {
		// TODO Auto-generated method stub
		File saveLocation = new File("Sudoku Saves");
		if(!saveLocation.exists())
		{
			saveLocation.mkdir();
		}

		if(!areYouSure("Are you sure? This will close the current board without saving."))
		{
			return;
		}
		bruteForceButton.setEnabled(false);
		mySelection=0;
		this.removeMouseListener(this);
		JFileChooser fc = new JFileChooser(saveLocation);
		fc.setFileFilter(new FileNameExtensionFilter("Sudoku Boards","sud"));
		int returnValue=fc.showOpenDialog(getParent());
		
		if(returnValue==JFileChooser.APPROVE_OPTION)
		{
			File file = fc.getSelectedFile();
			try {
				BufferedReader bReader=new BufferedReader( new FileReader(file));
				try {
					StringBuilder builder = new StringBuilder();
					String nextLine=bReader.readLine();
					while(nextLine!=null)
					{
						builder.append(nextLine);
						nextLine=bReader.readLine();
					}
					myBoard=Board.makeBoard(builder.toString());
					repaint();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//sends a generic confirmation message
	private boolean areYouSure(String message)
	{
		return JOptionPane.YES_OPTION==JOptionPane.showConfirmDialog(this, message,"Warning!", JOptionPane.YES_NO_OPTION);
	}

	public void mouseClicked(MouseEvent arg0) {
		int gapBetweenCells=49;
		if(arg0.getSource()==this)
		{
			
			int x = arg0.getX();
			int y = arg0.getY();
			
			if(y>=boardHeight+10)
			{
				mySelection=x/gapBetweenCells+1;
				repaint();
			}else{
				int xCell=x/49;
				int yCell = y/49;
				
				myBoard.forceSetCell(xCell, yCell, mySelection);
				repaint();
			}
			
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
