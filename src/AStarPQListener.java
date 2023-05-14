// package Game;

/*
 The code below allow the A* to perform A* AI panel
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class AStarPQListener implements ActionListener {

	JFrame frame;
	
	public AStarPQListener(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Thread thread = new Thread(){
		    public void run(){
				((MyFrame) frame).AStarPQAI();
		    }
		  };

		  thread.start();
	}

}
