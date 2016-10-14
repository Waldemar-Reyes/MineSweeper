import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Random;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 25;
	private static final int GRID_Y = 25;
	private static final int INNER_CELL_SIZE = 29;
	private static final int TOTAL_COLUMNS = 9;
	private static final int TOTAL_ROWS = 9;
	private static int numberOfMines =10;
	private Random generator = new Random();
	public int x = -1;
	public int y = -1;
	public boolean playerFinished = false;
	public String endMessage = "";
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	public int[][] mineArray = new int[TOTAL_COLUMNS][TOTAL_ROWS];
	public Color pressedCellColor = new Color(177, 177, 177);
	public MyPanel() {   //This is the constructor... this code runs first to initialize
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}

		//Paint blank(unplayed) cells
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				colorArray[x][y] = Color.WHITE;
			}
		}
		//Paint Mines
		int counter = 0;
		do {
			int x = generator.nextInt(9);
			int y = generator.nextInt(9);
			if(isMine(x, y)){
				continue;
			}
			mineArray[x][y] = 1;
			// -- Optional line | For Test Purposes
			colorArray[x][y] = Color.WHITE;
			// --
			counter++;
		} while (counter < numberOfMines);
		//Set cells without mines
		for (int y = 0; y < TOTAL_ROWS; y++) {
			for (int x = 0; x < TOTAL_COLUMNS; x++) {
				if (!(mineArray[x][y] == 1)) {
					mineArray[x][y] = 0;
				}
			}
		}
	}

	public boolean playerFinished() { return playerFinished; }
	public String endMessage() { return endMessage; }

	
	// 1 = mine ** 4 = flagged | mine
	public boolean isMine(int x, int y) {
		return (mineArray[x][y] == 1 || mineArray[x][y] == 4);
	}

	public int counterNonMines() {
		int countNonMines = 0;
		for (int y = 0; y < TOTAL_ROWS; y++) {
			for (int x = 0; x < TOTAL_COLUMNS; x++) {
				if(colorArray[x][y].equals(pressedCellColor)) {
					countNonMines++;
				}
			}
		}
		return countNonMines;
	}

	public void resetAllMinesToDefault() {
		for (int y = 0; y < TOTAL_ROWS; y++) {
			for (int x = 0; x < TOTAL_COLUMNS; x++) {
				mineArray[x][y] = 0;
				colorArray[x][y] = Color.WHITE;
			}
		}
	}

	public void resetMineField() {
		resetAllMinesToDefault();
		int counter = 0;
		do {
			int x = generator.nextInt(9);
			int y = generator.nextInt(9);
			if(isMine(x, y)) {
				continue;
			}
			mineArray[x][y] = 1;
			
			counter++;
		} while (counter < numberOfMines);
	}

	public boolean isCovered(int x, int y) {
		return (colorArray[x][y].equals(Color.WHITE));
	}

	public boolean isFillable(int x, int y) {
		for (int i = -1; i < 2; i++){
			if((x+i) < -1 || (x+i) > TOTAL_COLUMNS ) {
				return false;
			}
			for(int j = -1; j < 2; j++) {
				if((y+j) < -1 || (y+j) > TOTAL_ROWS) {
					return false;
				}

			}
		}
		return (!isMine(x,y)&&isCovered(x,y));
	}
	// 3 = flagged | no mine ** 4 = flagged | mine
	public boolean isFlagged(int x, int y){
		return (mineArray[x][y] == 3 || mineArray[x][y] == 4);
	}
	public boolean checkWin(int checkNumber){
		return (checkNumber == TOTAL_COLUMNS*TOTAL_ROWS-numberOfMines);
	}
	/* The countMines method counts the mines that are adjacent to the selected cell.
	The approach is similar to that of finding the adjacent cell in a Cartesian plane.
	The countMines ignores cells that are outside the panel.*/
	public int countMines(int x, int y) {
		int counter = 0;
		// The for loops do not consider indexes that are not in the grid.
		for (int i = -1; i < 2; i++){
			if((x+i) < 0 || (x+i) > TOTAL_COLUMNS-1) {
				continue;
			}
			for(int j = -1; j < 2; j++) {
				if((y+j) < 0 || (y+j) > TOTAL_ROWS-1) {
					continue;
				}
				else if (isMine((x+i), (y+j))) {
					counter++;
				}
			}
		}
		return counter;  
	}
	// Implementation of the flood fill algorithm
	public void floodFillAdjacent(int x, int y) {
		if(isFillable(x,y) && countMines(x,y)==0) {
			colorArray[x][y]= pressedCellColor;
			floodFillAdjacent( x+1, y );
			floodFillAdjacent( x-1, y );
			floodFillAdjacent( x, y+1 );
			floodFillAdjacent( x, y-1 );
		}
		else if (countMines(x,y)>0) {
			colorArray[x][y]= pressedCellColor;
		}
		else return;
	}

	public void revealAllBombs() {
		for (int y = 0; y < TOTAL_ROWS; y++) {
			for (int x = 0; x < TOTAL_COLUMNS; x++) {
				if(isMine(x,y)) {
					colorArray[x][y] = Color.BLACK;
				}
			}
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		int x2 = getWidth() - myInsets.right - 1;
		int y2 = getHeight() - myInsets.bottom - 1;
		int width = x2 - x1;
		int height = y2 - y1;

		//Paint the background
		Color bgColor = new Color(136, 136, 136);
		g.setColor(bgColor);
		g.fillRect(x1, y1, width + 1, height + 1);

		//Draw the grid
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS)));
		}

		//Paint cell colors
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				Color c = colorArray[x][y];
				g.setColor(c);
				g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
			}
		}

		Font times = new Font("Arial", Font.PLAIN, 22);
		g.setFont(times);
		for (int i = 0;  i< TOTAL_COLUMNS;  i++) {
			for (int j = 0; j < TOTAL_ROWS; j++) {
				if(isFlagged(i,j)) {
					g.setColor(Color.RED);
				}
				else if (!isMine(i,j) && !isCovered(i,j) && colorArray[i][j] != Color.RED) {
					g.setColor(Color.MAGENTA);
					switch(countMines(i,j)) {
					case 0:
						g.setColor(Color.MAGENTA);
						g.drawString("", i*INNER_CELL_SIZE+38, j*INNER_CELL_SIZE+50);
						break;
					case 1:
						g.setColor(Color.BLUE);
						g.drawString("1", i*INNER_CELL_SIZE+38, j*INNER_CELL_SIZE+50);
						break;
					case 2:
						Color darkGreen = new Color(0,153,0);
						g.setColor(darkGreen);
						g.drawString("2", i*INNER_CELL_SIZE+38, j*INNER_CELL_SIZE+50);
						break;
					case 3:
						g.setColor(Color.RED);
						g.drawString("3", i*INNER_CELL_SIZE+38, j*INNER_CELL_SIZE+50);
						break;
					case 4:
						Color darkBlue = new Color (0,0,102);
						g.setColor(darkBlue);
						g.drawString("4", i*INNER_CELL_SIZE+38, j*INNER_CELL_SIZE+50);
						break;	
					}
					if (countMines(i,j) == 0) {
					}
				}
			}
		}
	}

	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {
			//To the left of the grid
			return -1;
		}
		if (y < 0) {
			//Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {
			//Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 1) {
			//Outside the rest of the grid
			return -1;
		}
		return x;
	}

	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {
			//To the left of the grid
			return -1;
		}
		if (y < 0) {
			//Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {
			//Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 1) {
			//Outside the rest of the grid
			return -1;
		}
		return y;
	}
}