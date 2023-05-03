// package Game;
/*
"The code below defines a "Node" class to represent a point in a grid used in pathfinding algorithms. 
The Node class is implemented according to the Comparable interface to allow Node objects to be compared 
with each other based on fCost (the total cost from the start point to the destination). The properties 
of the Node class are explained as follows:

- xAxis: x-coordinate of the Node in the grid.
- yAxis: y-coordinate of the Node in the grid.
- gCost: cost from start Node to current Node.
- hCost: estimated cost from current Node to destination (heuristic).
- fCost: total cost (gCost + hCost) to go from start Node to current Node and to destination.
- parent: parent Node of current Node in best path from start Node.
- walkable: boolean variable representing ability to pass through this Node.
- direction: direction to go to current Node from parent Node (L, R, U, D).
- Closed: boolean variable representing state of Node in pathfinding algorithm."
The code below also provides methods for manipulating the properties of the Node class such as set/get 
cost values, set parent Node, calculate fCost, close Node, compare Nodes with each other, determine state 
of Node, etc.
 */

public class Node implements Comparable<Node> {
	
	int xAxis;
	int yAxis;
	//int width;
	int gCost = 0;
	int hCost;
	int fCost;
	Node parent;
	boolean walkable;
	char direction = 'x';
	boolean Closed = false;

	
	public Node(int xAxis, int yAxis, int gCost, int hCost) {
		this.xAxis = xAxis;
		this.yAxis = yAxis;
		this.gCost = gCost;
		this.hCost = hCost;
		calcFCost();
	}
	
	public char getDirection() {
		return direction;
	}

	public void setDirection(char direction) {
		this.direction = direction;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		if (direction == 'x') {
			if (parent.getxAxis() - this.xAxis > 0) {
				direction = 'L';
			} else if (parent.getxAxis() - this.xAxis < 0) {
				direction = 'R';
			} else {
				if (parent.getyAxis() - this.yAxis > 0) {
					direction = 'U';
				} else {
					direction = 'D';
				}
			}
		}
		this.parent = parent;
	}
	
	public void setgCost(int gCost) {
		this.gCost = gCost;
		calcFCost();
	}
	
	private void calcFCost() {
		fCost = gCost + hCost;
	}
	
	public int getFCost() {
		return fCost;
	}

	@Override
	public int compareTo(Node o) {
		if (this.fCost > o.fCost)
			return 1;
		else if (this.fCost < o.fCost)
			return -1;
		
		return 0;
	}

	public int getxAxis() {
		return xAxis;
	}

	public int getyAxis() {
		return yAxis;
	}

	public boolean isClosed() {
		return Closed;
	}
	
	public void close() {
		Closed = true;
	}
	
	public boolean same(Node o) {
		if (this.xAxis == o.xAxis && this.yAxis == o.yAxis) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
	    // self check
	    if (this == o)
	        return true;
	    // null check
	    if (o == null)
	        return false;
	    // type check and cast
	    if (getClass() != o.getClass())
	        return false;
	    Node node = (Node) o;
	    // field comparison
	    return this.xAxis == node.xAxis && this.yAxis == node.yAxis;
	}

	public int getgCost() {
		return gCost;
	}

}
