// package Game;

/*
The code below allows Single AI Snake to play again when the game is over
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class PASingleAIListener implements ActionListener  {

	JFrame frame;

	public PASingleAIListener(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		((MyFrame) frame).playAgainSingleAI();
	}

}