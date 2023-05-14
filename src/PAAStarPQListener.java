// package Game;
/*
The code below allows A* AI Snake to play again when the game is over
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class PAAStarPQListener implements ActionListener {

	JFrame frame;
	
	public PAAStarPQListener(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		((MyFrame) frame).playAgainAStarPQ();
	}

}
