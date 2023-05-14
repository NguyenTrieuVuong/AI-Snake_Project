// package Game;

/*
 This code appears to be a part of a snake game. It is a method called "pathFinder" that is responsible for 
 determining the direction in which the snake should move in order to reach the apple. The algorithm used 
 for determining the path is A* algorithm. The A* algorithm uses a heuristic function to estimate the cost 
 from the current position to the goal position, and then chooses the lowest cost path to reach the goal position.

The code initializes several variables to be used in the algorithm. The variables include hCostA, hCostB, 
hCostC, xDistance, yDistance, blocked, fCostA, fCostB, and fCostC. The variables hCostA, hCostB, and hCostC 
represent the estimated cost from the current position to the goal position when moving in three different 
directions (up, left, and right). The variables xDistance and yDistance represent the distance between the 
current position and the goal position in the x and y directions. The variable blocked is used to check if 
the snake's body is blocking the path. The variables fCostA, fCostB, and fCostC represent the total cost 
from the start position to the goal position when moving in three different directions. 

The code then checks the direction in which the snake is moving. If the direction is up, the code checks 
if the snake can move up, left, or right. It first checks if the snake can move up. If it can move up, it 
checks if there are any body parts blocking the way. If there are no body parts blocking the way, it calculates 
the estimated cost of moving up to the goal position. The estimated cost is calculated by adding the distance 
between the current position and the goal position in the x and y directions, multiplied by 10, to the heuristic 
cost hCostA. The heuristic cost hCostA is set to 4 if the distance in the y direction is not zero. The total 
cost fCostA is then calculated by adding the heuristic cost hCostA and the cost of moving up, which is 10.

The code then checks if the snake can move left or right, and if it can, it calculates the estimated cost 
of moving in that direction and the total cost of moving in that direction. Finally, the code checks which 
direction has the lowest total cost and sets the direction of the snake accordingly.

If the snake is moving down, the code follows a similar procedure as for moving up. The code checks if the 
snake can move down, left, or right. If it can move down, it calculates the estimated cost of moving down 
to the goal position. The heuristic cost hCostA is set to 4 if the distance in the y direction is not zero. 
The total cost fCostA is then calculated by adding the heuristic cost hCostA and the cost of moving down, 
which is 10. The code then checks if the snake can move left or right, and if it can, it calculates the 
estimated cost of moving in that direction and the total cost of moving in that direction. Finally, the code 
checks which direction has the lowest total cost and sets the direction of the snake accordingly.

The code updates the fCostA, fCostB, and fCostC variables to their initial values at the end of the pathFinder 
method, ready for the next iteration.
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.*;

public class SingleAIPanel extends JPanel implements ActionListener {

	final int SCREEN_WIDTH;
	final int SCREEN_HEIGHT;
	static final int UNIT_SIZE = 25;
	final int GAME_UNITS;
	static final int DELAY = 45;
	final int x[];
	final int y[];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	JFrame frame;
	
	public SingleAIPanel(JFrame frame, int w, int h) {
		SCREEN_WIDTH = w;
		SCREEN_HEIGHT = h;
		GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
		this.x = new int[GAME_UNITS];
		this.y = new int[GAME_UNITS];
		this.frame = frame;
		random = new Random();
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		setBackground(Color.BLUE);
		setFocusable(true);
		startGame();
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void restart() {
		bodyParts = 6;
		direction = 'R';
		applesEaten = 0;
		for (int i = bodyParts; i >= 0; i--) {
			x[i] = 0;
			y[i] = 0;
		}
		startGame();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		if (running) {
			g.setColor(Color.RED);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			timer.setDelay(DELAY);
			for (int i = 0; i < bodyParts; i++) {
				if (applesEaten % 10 == 0 && applesEaten != 0) {
					timer.setDelay(45);
					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else if (i == 0) {
					g.setColor(new Color(200, 200, 200));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(255, 255, 255));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.RED);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
		} else {
			gameOver(g);
		}
	}
		
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;	
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;	
		fixApple();
	}
	
	private void fixApple() {
		for(int i = bodyParts; i>0; i--) {
			if(appleX == x[i] && appleY == y[i]) {
				newApple();
			}
		}
	}
	
	public void move() {
		for(int i = bodyParts; i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	
	public void checkCollisions() {
		for(int i = bodyParts; i>0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		if(x[0] < 0) {
			running = false;
		}
		if(x[0] >= SCREEN_WIDTH) {
			running = false;
		}
		if (y[0] < 0) {
			running = false;
		}
		if (y[0] >= SCREEN_HEIGHT) {
			running = false;
		}
		if (!running) {
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) {
		((MyFrame) frame).gameOverSingleAI(new GOSingleAIPanel(applesEaten, SCREEN_WIDTH, SCREEN_HEIGHT, g, frame));
	}
	
	public void actionPerformed(ActionEvent event) {
		if(running) {
			pathFinder();
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	private void pathFinder() {
		int hCostA = 0;
		int hCostB = 0;
		int hCostC = 0;
		int xDistance;
		int yDistance;
		boolean blocked = false;
		/*Initializing the fCostA, fCostB, and fCostC variables to a very large number is a common practice 
		in pathfinding algorithms, particularly in A* algorithm implementations. This is done because fCostA, 
		fCostB, and fCostC represent the estimated total cost from the start node to the current node, and if 
		a better path is found, these values will be updated with a lower cost. By initializing them to a very 
		large number, any other value will be smaller and will become the new minimum cost for that node.

		If we initialize these values to a small number, it could lead to unexpected results, such as the 
		algorithm thinking that the first path it finds is the shortest one, even if a shorter path exists. 
		Initializing them to a very large number ensures that any valid path found will have a lower cost 
		and will be selected as the new shortest path.*/
		int fCostA = Integer.MAX_VALUE;
		int fCostB = Integer.MAX_VALUE;
		int fCostC = Integer.MAX_VALUE;
		
		switch(direction) {
		case 'U':
			hCostA = 0;
			hCostB = 0;
			hCostC = 0;
			
			// If space to go up
			if (y[0] - UNIT_SIZE >= 0) {
				// If no body parts blocking
				for(int i = bodyParts; i>0; i--) {
					if((x[0] == x[i]) && (y[0] - UNIT_SIZE == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going up
					xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
					yDistance = Math.abs((appleY - (y[0] - UNIT_SIZE)) / UNIT_SIZE);
					/* If the yDistance (the absolute value of the difference in y-coordinates between the current node and the 
					goal node) is not equal to zero, the algorithm assigns a heuristic cost of 4 to the movement.  */
					if (yDistance != 0) {
						hCostA = 4;
					}
					/*The movement cost of 10 for vertical/horizontal movement and 14 for diagonal movement 
					is based on the fact that this algorithm is using the A* search algorithm to find 
					the shortest path to the apple for the snake in a grid-like environment. 
					In a grid, the cost of moving in the vertical or horizontal direction is typically set to 1, 
					as it involves moving to the neighboring cell in the same row or column. The cost of moving 
					diagonally is typically set to sqrt(2) (approximately 1.4) as it involves moving to 
					the neighboring cell in a diagonal direction.*/
					hCostA+= (xDistance * 10) + (yDistance * 10);
					fCostA = hCostA + 10;
				}
				blocked = false;
			}
			
			// If space to go left
			if(x[0] - UNIT_SIZE >= 0) {
				// If no body parts blocking
				for(int i = bodyParts; i>0; i--) {
					if((x[0] - UNIT_SIZE == x[i]) && (y[0] == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going left
					xDistance = Math.abs((appleX - (x[0] - UNIT_SIZE)) / UNIT_SIZE);
					yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostB = 4;
					}
					/*The movement cost of 10 for vertical/horizontal movement and 14 for diagonal movement 
					is based on the fact that this algorithm is using the A* search algorithm to find 
					the shortest path to the apple for the snake in a grid-like environment. 
					In a grid, the cost of moving in the vertical or horizontal direction is typically set to 1, 
					as it involves moving to the neighboring cell in the same row or column. The cost of moving 
					diagonally is typically set to sqrt(2) (approximately 1.4) as it involves moving to 
					the neighboring cell in a diagonal direction.*/
					hCostB+= (xDistance * 10) + (yDistance * 10);
					fCostB = hCostB + 14;
				}
				blocked = false;
			}
			
			// If space to go right
			if(x[0] + UNIT_SIZE < SCREEN_WIDTH) {
				// If no body parts blocking
				for(int i = bodyParts; i>0; i--) {
					if((x[0] + UNIT_SIZE == x[i]) && (y[0] == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going right
					xDistance = Math.abs((appleX - (x[0] + UNIT_SIZE)) / UNIT_SIZE);
					yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostC = 4;
					}
					hCostC+= (xDistance * 10) + (yDistance * 10);
					fCostC = hCostC + 14;
				}
				blocked = false;
			}
			
			if(fCostA <= fCostB && fCostA <= fCostC) {
				direction = 'U';
			} else if (fCostB < fCostA && fCostB <= fCostC) {
				direction = 'L';
			} else if(fCostC < fCostB && fCostC < fCostA) {
				direction = 'R';
			}
			// Reinitializing fCost to a large number after switching the direction
			fCostA = 999999999;
			fCostB = 999999999;
			fCostC = 999999999;
			
			break;
		
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		case 'D':
			hCostA = 0;
			hCostB = 0;
			hCostC = 0;
			
			// If space to go down
			if (y[0] + UNIT_SIZE < SCREEN_HEIGHT) {
				// If no body parts blocking
				for (int i = bodyParts; i > 0; i--) {
					if ((x[0] == x[i]) && (y[0] + UNIT_SIZE == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going down
					xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
					yDistance = Math.abs((appleY - (y[0] + UNIT_SIZE)) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostA = 4;
					}
					hCostA += (xDistance * 10) + (yDistance * 10);
					fCostA = hCostA + 10;
				}
				blocked = false;
			}
			
			// If space to go left
			if (x[0] - UNIT_SIZE >= 0) {
				// If no body parts blocking
				for (int i = bodyParts; i > 0; i--) {
					if ((x[0] - UNIT_SIZE == x[i]) && (y[0] == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going left
					xDistance = Math.abs((appleX - (x[0] - UNIT_SIZE)) / UNIT_SIZE);
					yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostB = 4;
					}
					hCostB += (xDistance * 10) + (yDistance * 10);
					fCostB = hCostB + 14;
				}
				blocked = false;
			}
			
			// If space to go right
			if (x[0] + UNIT_SIZE < SCREEN_WIDTH) {
				// If no body parts blocking
				for (int i = bodyParts; i > 0; i--) {
					if ((x[0] + UNIT_SIZE == x[i]) && (y[0] == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going right
					xDistance = Math.abs((appleX - (x[0] + UNIT_SIZE)) / UNIT_SIZE);
					yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostC = 4;
					}
					hCostC += (xDistance * 10) + (yDistance * 10);
					fCostC = hCostC + 14;
				}
				blocked = false;
			}
			
			if (fCostA <= fCostB && fCostA <= fCostC) {
				direction = 'D';
			} else if (fCostB < fCostA && fCostB <= fCostC) {
				direction = 'L';
			} else if (fCostC < fCostB && fCostC < fCostA) {
				direction = 'R';
			}
			// Reinitializing fCost to a large number after switching the direction
			fCostA = 999999999;
			fCostB = 999999999;
			fCostC = 999999999;
			
			break;
		
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		case 'L':
			hCostA = 0;
			hCostB = 0;
			hCostC = 0;
			
			// If space to go left
			if (x[0] - UNIT_SIZE >= 0) {
				// If no body parts blocking
				for (int i = bodyParts; i > 0; i--) {
					if ((x[0] - UNIT_SIZE == x[i]) && (y[0] == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going left
					xDistance = Math.abs((appleX - (x[0] - UNIT_SIZE)) / UNIT_SIZE);
					yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostA = 4;
					}
					hCostA += (xDistance * 10) + (yDistance * 10);
					fCostA = hCostA + 10;
				}
				blocked = false;
			}
			
			// If space to go down
			if (y[0] + UNIT_SIZE < SCREEN_HEIGHT) {
				// If no body parts blocking
				for (int i = bodyParts; i > 0; i--) {
					if ((x[0] == x[i]) && (y[0] + UNIT_SIZE == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going down
					xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
					yDistance = Math.abs((appleY - (y[0] + UNIT_SIZE)) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostB = 4;
					}
					hCostB += (xDistance * 10) + (yDistance * 10);
					fCostB = hCostB + 14;
				}
				blocked = false;
			}
			
			// If space to go up
			if (y[0] - UNIT_SIZE >= 0) {
				// If no body parts blocking
				for (int i = bodyParts; i > 0; i--) {
					if ((x[0] == x[i]) && (y[0] - UNIT_SIZE == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going up
					xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
					yDistance = Math.abs((appleY - (y[0] - UNIT_SIZE)) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostC = 4;
					}
					hCostC += (xDistance * 10) + (yDistance * 10);
					fCostC = hCostC + 14;
				}
				blocked = false;
			}
			
			if (fCostA <= fCostB && fCostA <= fCostC) {
				direction = 'L';
			} else if (fCostB < fCostA && fCostB <= fCostC) {
				direction = 'D';
			} else if (fCostC < fCostB && fCostC < fCostA) {
				direction = 'U';
			}
			// Reinitializing fCost to a large number after switching the direction
			fCostA = 999999999;
			fCostB = 999999999;
			fCostC = 999999999;
			
			break;
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		case 'R':
			hCostA = 0;
			hCostB = 0;
			hCostC = 0;
			
			// If space to go right
			if (x[0] + UNIT_SIZE < SCREEN_WIDTH) {
				// If no body parts blocking
				for (int i = bodyParts; i > 0; i--) {
					if ((x[0] + UNIT_SIZE == x[i]) && (y[0] == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going right
					xDistance = Math.abs((appleX - (x[0] + UNIT_SIZE)) / UNIT_SIZE);
					yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostA = 4;
					}
					hCostA += (xDistance * 10) + (yDistance * 10);
					fCostA = hCostA + 10;
				}
				blocked = false;
			}
			
			// If space to go down
			if (y[0] + UNIT_SIZE < SCREEN_HEIGHT) {
				// If no body parts blocking
				for (int i = bodyParts; i > 0; i--) {
					if ((x[0] == x[i]) && (y[0] + UNIT_SIZE == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going down
					xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
					yDistance = Math.abs((appleY - (y[0] + UNIT_SIZE)) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostB = 4;
					}
					hCostB += (xDistance * 10) + (yDistance * 10);
					fCostB = hCostB + 14;
				}
				blocked = false;
			}
			
			// If space to go up
			if (y[0] - UNIT_SIZE >= 0) {
				// If no body parts blocking
				for (int i = bodyParts; i > 0; i--) {
					if ((x[0] == x[i]) && (y[0] - UNIT_SIZE == y[i])) {
						blocked = true;
						break;
					}
				}
				if (blocked != true) {
					// Going up
					xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
					yDistance = Math.abs((appleY - (y[0] - UNIT_SIZE)) / UNIT_SIZE);
					if (yDistance != 0) {
						hCostC = 4;
					}
					hCostC += (xDistance * 10) + (yDistance * 10);
					fCostC = hCostC + 14;
				}
				blocked = false;
			}
			
			if (fCostA <= fCostB && fCostA <= fCostC) {
				direction = 'R';
			} else if (fCostB < fCostA && fCostB <= fCostC) {
				direction = 'D';
			} else if (fCostC < fCostB && fCostC < fCostA) {
				direction = 'U';
			}
			// Reinitializing fCost to a large number after switching the direction
			fCostA = 999999999;
			fCostB = 999999999;
			fCostC = 999999999;
			
			break;
		}
	}
}
