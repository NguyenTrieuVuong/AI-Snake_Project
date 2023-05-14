// package Game;

/*
 The code below implements the A* algorithm to find the shortest path from the current position to the target 
 on a 2D grid. The nodes in the grid are represented by Node objects, each containing information about the 
 node's coordinates, the cost values (gCost) and heuristic function (hCost) required for calculating its total 
 cost (fCost).

The A* algorithm is implemented using a priority queue to store the nodes that need to be considered. When 
starting, the current node is created from the initial position and added to the queue. During the search, 
the algorithm looks for neighboring nodes of the current node to add to the priority queue. The neighboring 
nodes are added to the priority queue based on their total cost (fCost). When the target node is found, the 
algorithm returns a list of the parent nodes of the target node to determine the path.

Some variables used in the code are:
- parents: A list of the parent nodes of the target node (returned when the path is found).
- open: A priority queue containing the nodes that need to be considered.
- closed: A list of nodes that have been considered.
- count: The number of times nodes are traversed during the search.
- gCost: The cost of moving from the current node to the next node (values updated based on the direction of movement).
- startNode: The initial node created from the initial position and added to the priority queue.
- goalNode: The target node created from the coordinates of the target and used to check if the path has been found.
- SCREEN_WIDTH and SCREEN_HEIGHT: The screen size.
- UNIT_SIZE: The size of each cell in the grid.
- x[] and y[]: Arrays containing the coordinates of cells in the grid.
- isBlocked(): A method to check if a cell is blocked or not.
- findHCost(): A heuristic function to calculate the heuristic cost function for each node.

The `findHCost(int xAxis, int yAxis)` method calculates the "heuristic" cost (h-cost) of reaching the apple 
at a particular `xAxis` and `yAxis` position. This cost is calculated as the Manhattan distance between the 
current position and the apple position, which is the sum of the absolute differences in the x-axis and y-axis 
between the two positions. The method also takes into account whether the character must move vertically to 
reach the apple, in which case it adds 4 to the cost.

The `pathFinder()` method determines the best direction for the character to move to reach the apple. It 
first calculates the h-cost for three possible moves: up, left, and right. For each possible move, it checks 
whether the character's path would be blocked by its own body or the edges of the screen. If the path is not 
blocked, it calculates the f-cost for that move. The f-cost is the sum of the g-cost (distance from current 
position to new position) and the h-cost (calculated by calling `findHCost()`). It then chooses the move with 
the lowest f-cost, updates the character's direction, and resets the f-cost variables. 
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import javax.swing.*;

public class AStarPQAIPanel extends JPanel implements ActionListener  {

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
	int xDistance;
	int yDistance;
	int hCost;
	int numDirections = 0;
	char directions[];
	int count = 0;
	int gCost;
	
	public AStarPQAIPanel(JFrame frame, int w, int h) {
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

		List<Node> path = aStar();
		if (path == null) {
			numDirections = -1;
			return;
		}
		numDirections = path.size();
		directions = new char[numDirections];
		for (int i = 0; i < numDirections; i++) {
			directions[i] = path.get(i).getDirection();
		}
	}
	
	private void fixApple() {
		for(int i = bodyParts; i>0; i--) {
			if(appleX == x[i] && appleY == y[i]) {
				newApple();
			}
		}
	}
	
	public void move() {
		if (numDirections != -1) {
			direction = directions[numDirections - 1];
			numDirections--;
		}
		
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
		((MyFrame) frame).gameOverAStarPQ(new GOAStarPQPanel(applesEaten, SCREEN_WIDTH, SCREEN_HEIGHT, g, frame));
	}
	
	public void actionPerformed(ActionEvent event) {
		if(running) {
			if (numDirections == -1) {
				pathFinder();
			}
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	private boolean isBlocked(char d, int x, int y) { 
		if (d == 'R' ) {
			if (x >= SCREEN_WIDTH) {
				return true;
			}
			for (int i = bodyParts; i > 0; i--) {
				if ((x == this.x[i]) && (y == this.y[i])) {
					return true;
				}
			}
		} else if (d == 'L') {
			if (x < 0) {
				return true;
			}
			for (int i = bodyParts; i > 0; i--) {
				if ((x == this.x[i]) && (y == this.y[i])) {
					return true;
				}
			}
		} else if (d == 'D') {
			if (y >= SCREEN_WIDTH) {
				return true;
			}
			for (int i = bodyParts; i > 0; i--) {
				if ((x == this.x[i]) && (y == this.y[i])) {
					return true;
				}
			}
		} else {
			if (y < 0) {
				return true;
			}
			for (int i = bodyParts; i > 0; i--) {
				if ((x == this.x[i]) && (y == this.y[i])) {
					return true;
				}
			}
		}
		return false;
	}
	
	private List<Node> aStar() {

		List<Node> parents = new ArrayList<Node>();
		PriorityQueue<Node> open = new PriorityQueue<Node>();
		List<Node> closed = new ArrayList<Node>();
		
		count = 0;
		gCost = 0;
		Node startNode = new Node(x[0], y[0], gCost, findHCost(x[0], y[0]));
		startNode.setDirection(direction);
		Node goalNode = new Node(appleX, appleY, findHCost(x[0], y[0]), 0);

		open.add(startNode);
		
		while (!open.isEmpty()) { 
			
			count++;
			
			Node current = open.poll();
			current.close();
			closed.add(current);
			
			if (count > (SCREEN_WIDTH / UNIT_SIZE) * (SCREEN_HEIGHT / UNIT_SIZE) * 10) {
				return null;
			}
			
			if (current.same(goalNode)) {
				//backtrack and create parents list
				boolean finished = false;
				Node n = current;
				while (!finished) {
					parents.add(n);
					n = n.getParent();
					if (n.getParent() == null) {
						finished = true;
					}
				}
				return parents;
			}
			
			// check neighbours
			for (int i = 0; i < 3; i++) {
				/*The movement cost of 10 for vertical/horizontal movement and 14 for diagonal movement 
				is based on the fact that this algorithm is using the A* search algorithm to find 
				the shortest path to the apple for the snake in a grid-like environment. 
				In a grid, the cost of moving in the vertical or horizontal direction is typically set to 1, 
				as it involves moving to the neighboring cell in the same row or column. The cost of moving 
				diagonally is typically set to sqrt(2) (approximately 1.4) as it involves moving to 
				the neighboring cell in a diagonal direction.*/
				if (i == 0) {
					gCost = 10; // if current direction
				} else {
					gCost = 14; // if change direction, costs more
				}

				boolean exists = false;
				Node n;
				if (i == 0) {
					if (current.getDirection() == 'R') { // Continue Right
						// CHECK IF BLOCKED
						if (!isBlocked(current.getDirection(), current.getxAxis() + UNIT_SIZE, current.getyAxis())) {
							n = new Node(current.getxAxis() + UNIT_SIZE, current.getyAxis(), gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					} else if (current.getDirection() == 'L') { // Continue Left
						if (!isBlocked(current.getDirection(), current.getxAxis() - UNIT_SIZE, current.getyAxis())) {
							n = new Node(current.getxAxis() - UNIT_SIZE, current.getyAxis(), gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					} else if (current.getDirection() == 'D') { // Continue Down
						if (!isBlocked(current.getDirection(), current.getxAxis(), current.getyAxis() + UNIT_SIZE)) {
							n = new Node(current.getxAxis(), current.getyAxis() + UNIT_SIZE, gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					} else { // Continue Up
						if(!isBlocked(current.getDirection(), current.getxAxis(), current.getyAxis() - UNIT_SIZE)) {
							n = new Node(current.getxAxis(), current.getyAxis() - UNIT_SIZE, gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					}
				} else if (i == 1) {
					if (current.getDirection() == 'R') { // Turn Down
						if(!isBlocked('D', current.getxAxis(), current.getyAxis() + UNIT_SIZE)) {
							n = new Node(current.getxAxis(), current.getyAxis() + UNIT_SIZE, gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					} else if (current.getDirection() == 'L') { // Turn Up
						if(!isBlocked('U', current.getxAxis(), current.getyAxis() - UNIT_SIZE)) {
							n = new Node(current.getxAxis(), current.getyAxis() - UNIT_SIZE, gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					} else if (current.getDirection() == 'D') { // Turn Left
						if(!isBlocked('L', current.getxAxis() - UNIT_SIZE, current.getyAxis())) {
							n = new Node(current.getxAxis() - UNIT_SIZE, current.getyAxis(), gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					} else { // Turn Right
						if(!isBlocked('R', current.getxAxis() + UNIT_SIZE, current.getyAxis())) {
							n = new Node(current.getxAxis() + UNIT_SIZE, current.getyAxis(), gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					}
				} else {
					if (current.getDirection() == 'R') { // Turn Up
						if(!isBlocked('U', current.getxAxis(), current.getyAxis() - UNIT_SIZE)) {
							n = new Node(current.getxAxis(), current.getyAxis() - UNIT_SIZE, gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					} else if (current.getDirection() == 'L') { // Turn Down
						if(!isBlocked('D', current.getxAxis(), current.getyAxis() + UNIT_SIZE)) {
							n = new Node(current.getxAxis(), current.getyAxis() + UNIT_SIZE, gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					} else if (current.getDirection() == 'D') { // Turn Right
						if(!isBlocked('R', current.getxAxis() + UNIT_SIZE, current.getyAxis())) {
							n = new Node(current.getxAxis() + UNIT_SIZE, current.getyAxis(), gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					} else { // Turn Left
						if(!isBlocked('L', current.getxAxis() - UNIT_SIZE, current.getyAxis())) {
							n = new Node(current.getxAxis() - UNIT_SIZE, current.getyAxis(), gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					}
				}
				
				if (exists && n.isClosed()) {
					continue;
				}
				
				if (n.getFCost() <= current.getFCost() || !open.contains(n)) {
					n.setParent(current);
					if (!open.contains(n)) {
						n.setgCost(n.getParent().getgCost() + gCost);
						open.add(n);
					}
				}
			}
		}
		return null;
	}
	
	private int findHCost(int xAxis, int yAxis) {
		hCost = 0;
		xDistance = Math.abs((appleX - xAxis) / UNIT_SIZE);
		yDistance = Math.abs((appleY - yAxis) / UNIT_SIZE);
		/* If the yDistance (the absolute value of the difference in y-coordinates between the current node and the 
		goal node) is not equal to zero, the algorithm assigns a heuristic cost of 4 to the movement.  */ 
		if (yDistance != 0) {
			hCost = 4;
		}
		hCost += (xDistance * 10) + (yDistance * 10);
		return hCost;
	}
	
	private void pathFinder() {
		int hCostA = 0;
		int hCostB = 0;
		int hCostC = 0;
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
					if (yDistance != 0) {
						hCostA = 4;
					}
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
