import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
//import java.util.Random;

import javax.swing.JFrame;


// 0 = no mine
// 1 = mine
// 2 = played
// 3 = flagged | no mine
// 4 = flagged | mine

public class MyMouseAdapter extends MouseAdapter {
	//	private Random generator = new Random();
	public void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
		case 1:	{	//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame) c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			myPanel.mouseDownGridX = myPanel.getGridX(x, y);
			myPanel.mouseDownGridY = myPanel.getGridY(x, y);
			myPanel.repaint();
			break;
		}
		case 3:	{	//Right mouse button
			//Do nothing
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame) c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			myPanel.mouseDownGridX = myPanel.getGridX(x, y);
			myPanel.mouseDownGridY = myPanel.getGridY(x, y);
			myPanel.repaint();
			break;
		}
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
	public void mouseReleased(MouseEvent e) {
		int counter =0;
		switch (e.getButton()) {
		case 1:	{	//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame)c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			int gridX = myPanel.getGridX(x, y);
			int gridY = myPanel.getGridY(x, y);

			if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
				
				
				
			} else {
				if ((gridX == -1) || (gridY == -1)) {
					//Is releasing outside
					//Do nothing
				} else {
					if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
						//Released the mouse button on a different cell where it was pressed
						//Do nothing
					} else {
						Color pressedCell = new Color(177, 177, 177);
						if (!myPanel.isMine(gridX, gridY)) {
							myPanel.mineArray[gridX][gridY] = 2;
							// Color changes to Gray because it was not a mine and was uncovered
							
							myPanel.floodFillAdjacent(gridX, gridY);
							myPanel.countMines(gridX, gridY);
							myPanel.colorArray[gridX][gridY] = pressedCell;
							if(myPanel.checkWin(myPanel.counterNonMines())){
								JOptionPane.showMessageDialog(null, "You won! :) ", "", JOptionPane.WARNING_MESSAGE);
							}
							myPanel.repaint();
						}
						//Player lost!
						else if (myPanel.isMine(gridX, gridY)) {
							myPanel.mineArray[gridX][gridY] = 1;
							// Changes the color to Black and the person loses the game.
							myPanel.revealAllBombs();
							
							myPanel.repaint();
							JOptionPane.showMessageDialog(null, "You lost :( ", "", JOptionPane.WARNING_MESSAGE);
						}
						//						else if (myPanel.mineArray[gridX][gridY] == 2) {
						//						}
						//						else if (myPanel.mineArray[gridX][gridY] == 3 
						//								|| myPanel.mineArray[gridX][gridY] == 4) {
						//						}
					}
				}
			}
			break;
		}
		case 3:	{	//Right mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame)c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			int gridX = myPanel.getGridX(x, y);
			int gridY = myPanel.getGridY(x, y);

			if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
				//Had pressed outside
				//Do nothing
			} else {
				if ((gridX == -1) || (gridY == -1)) {
					//Is releasing outside
					//Do nothing
				} else {
					if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
						//Released the mouse button on a different cell where it was pressed
						//Do nothing
					} else {
						//						Color cellColor = new Color(205, 205, 205);
						//						Color pCell = new Color(177, 177, 177);
						if (myPanel.mineArray[gridX][gridY] == 0) {
							myPanel.mineArray[gridX][gridY] = 3;
							myPanel.colorArray[gridX][gridY] = Color.RED;
							myPanel.repaint();
						}
						else if (myPanel.mineArray[gridX][gridY] == 1) {
							myPanel.mineArray[gridX][gridY] = 4;
							myPanel.colorArray[gridX][gridY] = Color.RED;
							myPanel.repaint();
						}
						else if (myPanel.mineArray[gridX][gridY] == 2) {
						}
						else if (myPanel.mineArray[gridX][gridY] == 3) {
							myPanel.mineArray[gridX][gridY] = 0;
							myPanel.colorArray[gridX][gridY] = Color.WHITE;
							myPanel.repaint();
						}
						else if (myPanel.mineArray[gridX][gridY] == 4) {
							myPanel.mineArray[gridX][gridY] = 1;
							myPanel.colorArray[gridX][gridY] = Color.WHITE;
							myPanel.repaint();
						}
					}
				}
			}
			// 0 = no mine
			// 1 = mine
			// 2 = played
			// 3 = flagged | no mine
			// 4 = flagged | mine
			break;
		}
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
}