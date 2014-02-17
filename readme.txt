Author: Daniel Anderson
Email: Daniel.r.anderson2013@gmail.com
Name: Sudoku Solver
Version: 1.0

To run this program, double click on "Sudoku Solver.jar" or run from the command line. 
Do not move the jar file without moving the "numbers" and "Sudoku Saves" directories to the same directory.

There are two ways to import a new puzzle:
	1. Enter the program and press the "Custom Board" button. After, click on the number (from the bottom) that you want to place in a cell, and then click on the cell you want that number to be in. You do not have to reclick on a number from the bottom to place it in multiple cells.
	2. Enter into your text-editor of choice. Type so that you have 81 characters. The first 9 are the first row, the second 9 are the next row, etc. Linebreaks are optional between rows and 0's or underscores are acceptable as unknown numbers. Save the text file as a .sud file.

The "Solve" button only works when there are at least 17 clues in the board, otherwise the puzzle has been proven to be impossible. If there is a contradiction (such as two 1's in one row) the solve button will not work either.

To brute force the puzzle, first try to solve it. If the normal solving algorithm doesn't solve it, you can try to brute force it. This will not always work, sometimes it takes too much memory.
