// package Game;

/*
 The below code's purpose is to run all the Snake AI program
 */
import javax.swing.SwingUtilities;

public class App {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
				new MyFrame();
		      }
		    });
	}

}
