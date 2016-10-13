import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {
	public static void main(String[] args) {
		JFrame myFrame = new JFrame("MineSweeper!");
		myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		myFrame.setLocation(400, 150);
		myFrame.setSize(340, 360);

		MyPanel myPanel = new MyPanel();
		myFrame.add(myPanel);

		MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
		myFrame.addMouseListener(myMouseAdapter);

		myFrame.setVisible(true);

		while(!myPanel.playerFinished) {
			myFrame.repaint();
		}

		Object[] options = {"Retry Again", "Quit"};
		int n = JOptionPane.showOptionDialog(myFrame,
				"Do you want to try again or quit?",
				"End of Game",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,     //do not use a custom Icon
				options,  //the titles of buttons
				options[0]); //default button title
		System.out.println(n);

		if (n == 0) {
			//Restart
		}
		else {
			System.exit(0);
		};
	}
}