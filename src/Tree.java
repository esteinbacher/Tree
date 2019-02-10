import java.util.ArrayList;
import java.util.Random;

public class Tree {
	public Node myRoot;

	public Tree(Node root) {
		myRoot = root;
	}


	public double evaluate(Node current, double x) {

		if(current.myLeft == null && current.myRight == null) {
			if(current.getConstant() != 0) {
				return (double) current.getConstant();
			}
			else {
				return x;
			}

		}
		double value;
		double left = evaluate(current.myLeft, x);
		double right = evaluate(current.myRight, x);
		if(current.getOperator() == "+") {
			value = left + right;
		}
		else if(current.getOperator() == "-") {
			value = left - right;
		}
		else if(current.getOperator() == "*") {
			value = left * right;
		}
		else {
			value = left / right;
		}
		return value;

	}
	
	
	public int size() {
		return sizeHelper(this.myRoot);
	}
	public int sizeHelper(Node current) {
		if(current == null) {
			return 0;
		}
		else {
			return sizeHelper(current.myLeft) + sizeHelper(current.myRight) + 1;
		}
	}
	
	
	public Node randomNode() {
		Random rand = new Random();
		ArrayList<Node> nodes = new ArrayList<Node>();
		if(myRoot != null) {
			randomNodeHelper(myRoot, nodes);
		}
		int index = rand.nextInt(nodes.size()-1);
		return nodes.get(index);
		
	}
	private void randomNodeHelper(Node currentRoot, ArrayList<Node> nodes) {
		if(currentRoot == null) {
			return;
		}

		randomNodeHelper(currentRoot.myLeft, nodes);
		nodes.add(currentRoot);
		
		randomNodeHelper(currentRoot.myRight, nodes);
		
	}
	
	public static void crossover(Tree newTree1, Tree newTree2) {
		Node node1 = newTree1.randomNode();
		System.out.println(node1.getOperator());
		
		
		Node node2 = newTree2.randomNode();
		System.out.println(node2.getOperator());
		Node temp = node2;
		System.out.println(node1.myParent.myRight == node1);
		System.out.println(node1.myParent.myLeft == node1);
		if(node1.myParent.myRight == node1) {
			node1.myParent.myRight = node2;
			
		}
		else {
			
			node1.myParent.myLeft = node2;
		}
		
		node2.myParent = node1.myParent;
		
		if(node2.myParent.myRight == node2) {
			node2.myParent.myRight = node1;
		}
		else {
			node2.myParent.myLeft = node1;
		}
		
		node1.myParent = temp.myParent;
	}
	public Tree copy() {
		Node newRoot = new Node(this.myRoot.getOperator(), new Node(this.myRoot));
		copyHelper(newRoot, this.myRoot);
		Tree t = new Tree(newRoot);
		return t;

	}
	
	
	private void copyHelper(Node newRoot, Node oldRoot) {
		if(oldRoot.myLeft == null && oldRoot.myRight == null) {
			if(oldRoot.getConstant() != 0) {
				newRoot = new Node(oldRoot.getConstant(), new Node(oldRoot.myParent));
			}
			else {
				newRoot = new Node("x", new Node(oldRoot.myParent));
			}
			return;
		}
		if(oldRoot.myLeft.getConstant() != 0) {
			newRoot.myLeft = new Node(oldRoot.myLeft.getConstant(), new Node(oldRoot));
		}
		else {
			newRoot.myLeft = new Node(oldRoot.myLeft.getOperator(), new Node(oldRoot));
		}
		copyHelper(newRoot.myLeft, oldRoot.myLeft);

	
		if(oldRoot.myRight.getConstant() != 0) {
			newRoot.myRight = new Node(oldRoot.myRight.getConstant(), new Node(oldRoot));
		}
		else {
			newRoot.myRight = new Node(oldRoot.myRight.getOperator(), new Node(oldRoot));
		}
		copyHelper(newRoot.myRight, oldRoot.myRight);
	}

	private void printTree() {
		if(myRoot != null) {
			printInOrder(myRoot, 0);
		}
	}
	
	
	private void printInOrder(Node currentRoot, int indentLevel) {
		if(currentRoot == null) {
			return;
		}

		printInOrder(currentRoot.myRight, indentLevel + 1);
		for(int i = 0; i < indentLevel; i++) {
			System.out.print("   ");
		}
		if(currentRoot.getConstant() != 0) {
		System.out.println(currentRoot.getConstant());
		}
		else {
			System.out.println(currentRoot.getOperator());
		}
		printInOrder(currentRoot.myLeft, indentLevel + 1);

	}
	public static void main(String[] args) {
		Tree t = new Tree(new Node("*", null));
		t.myRoot.myLeft = new Node("+", t.myRoot);
		t.myRoot.myRight = new Node("x", t.myRoot);
		t.myRoot.myLeft.myLeft = new Node(10, t.myRoot.myLeft);
		t.myRoot.myLeft.myRight = new Node("x", t.myRoot.myLeft);
		Tree y = new Tree(new Node("/", null));
		y.myRoot.myLeft = new Node("*", y.myRoot);
		y.myRoot.myRight = new Node("-", y.myRoot);
		y.myRoot.myLeft.myLeft = new Node("x", y.myRoot.myLeft);
		y.myRoot.myLeft.myRight = new Node("x", y.myRoot.myLeft);
		y.myRoot.myRight.myRight = new Node(12, y.myRoot.myRight);
		y.myRoot.myRight.myLeft = new Node(3, y.myRoot.myRight);
		Tree ty = t.copy();
		Tree yt = y.copy();
		crossover(ty, yt);
		ty.printTree();
		
	
		
	}
}
