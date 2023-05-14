// package Game;

/*
The code below displays start game GUI, there are 2 choices: Single AI and A* AI. The code also calls some functions
in that 2 choices Single AI and A* AI 
 */
import java.awt.CardLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MyFrame extends JFrame {
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	private JPanel contentPane;
	private GamePanel gamePanel;
	private AStarAIPanel singleAIPanel;
	private AStarPQAIPanel aStarAIPanel;
	private GOAStarPanel GOSAI;
	private GOAStarPQPanel GOAS;
	private MenuPanel MPanel = new MenuPanel(SCREEN_WIDTH, SCREEN_HEIGHT, this);
	CardLayout cardLayout = new CardLayout();

	public MyFrame() {
		setTitle("Snake");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setLayout(cardLayout);
		contentPane.add(MPanel, "Menu Panel"); 
		setResizable(false);
		setContentPane(contentPane);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	public void AStarAI() {
		singleAIPanel = new AStarAIPanel(this, SCREEN_WIDTH, SCREEN_HEIGHT);
		contentPane.add(singleAIPanel, "A* AI Panel");
        cardLayout.next(contentPane);
		contentPane.remove(MPanel);
		singleAIPanel.requestFocusInWindow();
	}
	
	public void gameOverAStarAI(GOAStarPanel GOSAI) {
		this.GOSAI = GOSAI;
		contentPane.add(GOSAI, "GameOver A* AI Panel");
        cardLayout.next(contentPane);
	}
	
	public void playAgainAStarAI() {
		cardLayout.next(contentPane);
		singleAIPanel.restart();
		contentPane.remove(GOSAI);
		singleAIPanel.requestFocusInWindow();
	}

	public void AStarPQAI() {
		aStarAIPanel = new AStarPQAIPanel(this, SCREEN_WIDTH, SCREEN_HEIGHT);
		contentPane.add(aStarAIPanel, "A* Priority Queue AI Panel");
        cardLayout.next(contentPane);
		contentPane.remove(MPanel);
		aStarAIPanel.requestFocusInWindow();
	}

	public void gameOverAStarPQ(GOAStarPQPanel GOAS) {
		this.GOAS = GOAS;
		contentPane.add(GOAS, "GameOver A* Priority Queue Panel");
        cardLayout.next(contentPane);
	}
	
	public void playAgainAStarPQ() {
		cardLayout.next(contentPane);
		aStarAIPanel.restart();
		contentPane.remove(GOAS);
		aStarAIPanel.requestFocusInWindow();
	}
}
