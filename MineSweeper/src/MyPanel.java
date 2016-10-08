import java.awt.Color;
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
	private Random generator = new Random();
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	public int[][] mineArray = new int[TOTAL_COLUMNS][TOTAL_ROWS];
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
		//		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //Top row
		//			colorArray[x][0] = Color.LIGHT_GRAY;
		//		}
		//		for (int y = 0; y < TOTAL_ROWS; y++) {   //Left column
		//			colorArray[0][y] = Color.LIGHT_GRAY;
		//		}
		Color cellColor = new Color(205, 205, 205);
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //The rest of the grid
			for (int y = 0; y < TOTAL_ROWS; y++) {
				colorArray[x][y] = Color.WHITE;
			}
		}
		//Paint Mines
		int counter = 0;
		do {
			int x = generator.nextInt(9);
			int y = generator.nextInt(9);
			mineArray[x][y] = 1;
			colorArray[x][y] = Color.BLACK;
			counter++;
		} while (counter < 10);

		//Paint Numbers
//		do {
//			for (int x = 0; x < TOTAL_COLUMNS; x++) {
//				for (int y = 0; y < TOTAL_ROWS; y++) {
//					if (mineArray[x][y] == 1) {
//						for (int m = x-1; m<x+2; m++) {
//							for (int n = y-1; n<y+2; n++) {
//								if ((m>=0 && n>=0) && (m<=8 && n<=8)) {
//									colorArray[m][n] = Color.BLUE;
//									System.out.println("[" + m + ", " + n + "]");
//								}
//								counter++;
//							}
//						}
//					}
//				}				
//			}
//		} while (counter < 10);


		for (int y = 0; y < TOTAL_ROWS; y++) {
			for (int x = 0; x < TOTAL_COLUMNS; x++) {
				if (!(mineArray[x][y] == 1)) {
					mineArray[x][y] = 0;
				}
			}
		}

	}
public boolean isMine(int x, int y){
		if(mineArray[x][y]==1){
			
		return true;
		}
		
		else return false;
	}
public int countMines(int x, int y){
	int counter =0;
	// The for loops should not consider indexes that are not in the grid
	for (int i=-1; i<2; i++){
		if(x+i<0|| x+i > TOTAL_COLUMNS-1){
			continue;
		}
		for(int j=-1; j<2;j++){
			if(y+j<0 || y+j > TOTAL_ROWS-1){
				continue;
			}
			else if (isMine((x+i),(y+j))){
				counter++;
			}
			
		}
	}
	System.out.println(counter);
	return counter;  
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

		//Draw the grid minus the bottom row (which has only one cell)
		//By default, the grid will be 10x10 (see above: TOTAL_COLUMNS and TOTAL_ROWS)
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
	}
	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		//		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
		//			return x;
		//		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 1) {   //Outside the rest of the grid
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
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		//		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
		//			return y;
		//		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 1) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}
}