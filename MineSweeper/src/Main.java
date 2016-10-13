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

		while(!myPanel.playerFinished()) {
			myFrame.repaint();
		}

		Object[] options = {"Play Again!", "Quit"};
		int n = JOptionPane.showOptionDialog(myFrame,
				myPanel.endMessage + "\nDo you want to play again or quit?",
				"End of Game.",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
		if (n == 0) {
			myFrame.dispose();
			myPanel.resetMineField();
			myPanel.repaint();
			main(args);
		}
		else {
			System.exit(0);
		}
	}
}