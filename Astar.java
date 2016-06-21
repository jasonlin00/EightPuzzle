import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;


public class Astar {
	
	public static int search(int[][] board, int heuristic) {
		
		Node root = new Node(heuristic, board);
		NodeComparator comparator = new NodeComparator();
		PriorityQueue<Node> q = new PriorityQueue<Node>(10, comparator);
		q.add(root);
		System.out.println("Search using Heurisitic " + heuristic);
		System.out.println("Initial Board State: ");
		
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board.length; j++)
				System.out.print(board[i][j] + " ");
			System.out.println();
		}
	
		
		int searchCounter = 1;
		
		while(!q.isEmpty())
		{
			Node tempNode = q.poll();
			
			if(!tempNode.isGoal()) {
				ArrayList<Node> tempChildren = tempNode.generateChildren();
				ArrayList<Node> children = new ArrayList<Node>();
				
				for(Node n : tempChildren) {
					if(!checkRepeat(n)) {
						children.add(n);
					}
				}
				
				if(children.size() == 0)
					continue;
				
				q.addAll(children);
				
				searchCounter++;
				
				
			} else {
				System.out.println("Steps taken: ");
				
				Stack<Node> solutionPath = new Stack<Node>();
				solutionPath.push(tempNode);
				tempNode = tempNode.getParentNode();
				
				while(tempNode != null && tempNode.getParentNode() != null) {
					solutionPath.push(tempNode);
					tempNode = tempNode.getParentNode();
				}
				
				
				while(!solutionPath.isEmpty()) {
					tempNode = solutionPath.pop();
					for(int i = 0; i < tempNode.getCurrentState().length; i++) {
						for(int j = 0; j < tempNode.getCurrentState().length; j++) {
							System.out.print(tempNode.getCurrentState()[i][j] + " ");
						}
						System.out.println();
					}
					System.out.println();
				}
				
				
				System.out.println("The step cost was: " + tempNode.getG());
				System.out.println("Number of nodes used: " + searchCounter);
				System.out.println();
				
				//Reached goal, print path
				
				return searchCounter;
			}
			
			
			
		}
		return searchCounter;
		
	}


	private static boolean checkRepeat(Node n) {
		// TODO Auto-generated method stub
		boolean ret = false; 
		Node node = n;
		while(n.getParentNode() != null && !ret) {
			if(Arrays.deepEquals(n.getParentNode().getCurrentState(), node.getCurrentState()))
				ret = true;
			n = n.getParentNode();
		}
		return ret;
	}
	
	private static class NodeComparator implements Comparator<Node> {
		public int compare(Node node1, Node node2) {
			return Integer.compare(node1.getF(), node2.getF());
		}
	}

	public static class Node {
		
		private Node parentNode;
		
		private int[][] currentState;
		private int heuristic;

		private int g; // g(n) = cost so far to reach n
		private int h; // h(n) = estimated cost from n to goal
		private int f; // f(n) = estimated total cost of path through n
		
		
		//Root node
		public Node(int heuristic, int[][] state) {
			this.g = 0;
			this.currentState = state;
			this.parentNode = null;
			this.setHeuristic(heuristic);
			if(heuristic == 1) 
				this.h = calculateHeuristic1();
			else 
				this.h = calculateHeuristic2();
			
			this.f = this.h + this.g;
		}
		
		//constructor that allows initialization of g
		public Node(Node parentNode, int heuristic, int[][] state) {
			this.g = parentNode.getG() + 1;
			this.parentNode = parentNode;
			this.currentState = state;
			this.setHeuristic(heuristic);
			
			if(heuristic == 1) 
				this.h = calculateHeuristic1();
			else 
				this.h = calculateHeuristic2();
			
			this.f = this.h + this.g;
		}
		
		public ArrayList<Node> generateChildren() {
			int xPosition = 0;
			int yPosition = 0;
			for(int i = 0; i < this.currentState.length; i++ ) {
				for(int j = 0; j < this.currentState[i].length; j++) {
					if(currentState[i][j] == 0)
					{
						xPosition = i;
						yPosition = j;
					}
				}
			}
			ArrayList<Node> childrenNodes = new ArrayList<Node>();
			Node childNode = null;
			
			if(xPosition != 0) {

				int temp[][] = new int[3][3];
				for(int i = 0 ; i < currentState.length; i++)
					for(int j = 0; j < currentState[i].length; j++)
						temp[i][j] = currentState[i][j];
				temp[xPosition][yPosition] = currentState[xPosition - 1][yPosition];
				temp[xPosition - 1][yPosition] = 0;
				childNode = new Node(this, getHeuristic(),temp);
				childrenNodes.add(childNode);
				
			}
			if(xPosition != 2) {

				int temp[][] = new int[3][3];
				for(int i = 0 ; i < currentState.length; i++)
					for(int j = 0; j < currentState[i].length; j++)
						temp[i][j] = currentState[i][j];
				temp[xPosition][yPosition] = currentState[xPosition + 1][yPosition];
				temp[xPosition + 1][yPosition] = 0;
				childNode = new Node(this, getHeuristic(),temp);
				childrenNodes.add(childNode);
			}
			if(yPosition != 0) {

				int temp[][] = new int[3][3];
				for(int i = 0 ; i < currentState.length; i++)
					for(int j = 0; j < currentState[i].length; j++)
						temp[i][j] = currentState[i][j];
				temp[xPosition][yPosition] = currentState[xPosition][yPosition - 1];
				temp[xPosition][yPosition - 1] = 0;
				childNode = new Node(this, getHeuristic(),temp);
				childrenNodes.add(childNode);
			}
			if(yPosition != 2) {

				int temp[][] = new int[3][3];
				for(int i = 0 ; i < currentState.length; i++)
					for(int j = 0; j < currentState[i].length; j++)
						temp[i][j] = currentState[i][j];
				temp[xPosition][yPosition] = currentState[xPosition][yPosition + 1];
				temp[xPosition][yPosition + 1] = 0;
				childNode = new Node(this, getHeuristic(),temp);
				childrenNodes.add(childNode);
			}
			
			return childrenNodes;
		}
		
		public boolean isGoal() {
			return this.h == 0;
		}

		public Node getParentNode() {
			return parentNode;
		}
		
		public void setParentNode(Node parentNode) {
			this.parentNode = parentNode;
		}
		
		private int calculateHeuristic1() {
			
			int misplacedTiles = 0;
			
			for(int i = 0; i < currentState.length; i++) {
				for(int j = 0; j < currentState[i].length; j++) {
					if(currentState[i][j] != 0 && currentState[i][j] != i * 3 + j) 
						misplacedTiles++;
				}
			}
			return misplacedTiles;
		}
		
		private int calculateHeuristic2() {
			
			int manhattanDistance = 0;
			
			for(int i = 0; i < currentState.length; i++) {
				for(int j = 0; j < currentState[i].length; j++) {
					if(currentState[i][j] != 0 && currentState[i][j] != i * 3 + j) {
						
						int value = currentState[i][j];
						int targetX = value / 3;
						int targetY = value % 3;
						int dx = i - targetX;
						int dy = j - targetY;
						manhattanDistance += Math.abs(dx) + Math.abs(dy);
						
					}	
				}
			}
			return manhattanDistance;
			
		}
		

		public int[][] getCurrentState() {
			return currentState;
		}

		public void setCurrentState(int[][] currentState) {
			this.currentState = currentState;
		}
		
		public int getG() {
			return g;
		}
		
		public void setG(int g) {
			this.g = g;
		}
		
		public int getH() {
			return h;
		}
		
		public void setH(int h) {
			this.h = h;
		}
		
		public int getF() {
			return f;
		}
		
		public void setF(int f) {
			this.f = f;
		}

		public int getHeuristic() {
			return heuristic;
		}

		public void setHeuristic(int heuristic) {
			this.heuristic = heuristic;
		}
		
	}
	
}
