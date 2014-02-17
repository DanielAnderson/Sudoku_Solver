import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoardCanvas extends Canvas implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static BufferedImage[] numberImages;
	private static int canvasWidth;
	private static int canvasHeight;
	private Board myBoard;
	
	private static JButton loadButton;
	public static void main(String[] args)
	{
		//sets up images for the numbers
		setupImages();
		
		//sets up the canvas that the board goes on
		BoardCanvas theCanvas = setupBoard();
		
		//sets up the right panel.
		JPanel thePanel = setupPanel();
		
		//sets up the main window
		JFrame f = setupJFrame();
		
		//finalizes the window and displays
		f.add(theCanvas);
		f.add(thePanel);
		f.pack();
		f.setVisible(true);
	}
	
	private static JPanel setupPanel() {
		// TODO Auto-generated method stub
		JButton solveButton = new JButton("Solve");
		JButton makeNewBoard = new JButton("Custom Board");
		loadButton = new JButton("Load");
		
		JPanel thePanel = new JPanel();
		thePanel.setLayout(new BoxLayout(thePanel, BoxLayout.Y_AXIS));
		
		thePanel.add(solveButton);
		thePanel.add(javax.swing.Box.createVerticalStrut(100));
		thePanel.add(makeNewBoard);
		thePanel.add(loadButton);
		
		return thePanel;
	}

	private static JFrame setupJFrame() {
		// TODO Auto-generated method stub
		JFrame f = new JFrame();
		f.setLayout(new FlowLayout());
		f.setTitle("Sudoku Solver");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return f;
	}

	private static BoardCanvas setupBoard() {
		// TODO Auto-generated method stub
		canvasWidth=numberImages[1].getWidth()*9+8;
		canvasHeight=numberImages[1].getHeight()*9+8;
		
		BoardCanvas theCanvas = new BoardCanvas();
		theCanvas.setSize(canvasWidth, canvasHeight);
		theCanvas.setBackground(Color.white);
		return theCanvas;
	}

	private static void setupImages()
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

	
	public BoardCanvas()
	{
		myBoard=Main.makeBoard(Main.board1);
		myBoard.fillNullCells();
	}
	
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
			g.drawLine(i*gapBetweenLines-1, 0, i*gapBetweenLines-1,canvasHeight-1);
			g.drawLine(0, i*gapBetweenLines-1, canvasWidth-1, i*gapBetweenLines-1);
		}
		for(int i = 0 ; i < 9;i++)
		{
			for(int j = 0 ; j<9;j++)
			{
				int pictureIndex=myBoard.getCell(i, j).getValue();
				if(pictureIndex>0&&pictureIndex<10)
				{
					g.drawImage(numberImages[pictureIndex], i*gapBetweenLines, j*gapBetweenLines, this);
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==loadButton)
		{
			System.out.println("asdf");
		}
		
	}
}
