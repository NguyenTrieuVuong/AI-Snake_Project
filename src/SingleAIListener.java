// package Game;

/*
 The code below allow the Single AI to perform Single AI panel
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class SingleAIListener implements ActionListener {
	JFrame frame;

	public SingleAIListener(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		((MyFrame) frame).SingleAI();
	}

}
