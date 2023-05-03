// package Game;
/*
The code below allows A* AI Snake to play again when the game is over
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class PAAStarListener implements ActionListener {

	JFrame frame;
	
	public PAAStarListener(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		((MyFrame) frame).playAgainAStar();
	}

}
