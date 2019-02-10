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

	public void crossover(Tree newTree2) {
		this.myRoot = new Node("Y", null, -1);
		/**
		System.out.println(node1.getOperator());


		Node node2 = newTree2.randomNode();
		System.out.println(node2.getOperator());
		Node temp = node2;

		if(node1.dir == 0) {
			if(node1.myParent != null) {
				node1.myParent.myLeft = new Node("word", node1.myParent, 0);
			}
		}
		else {
			if(node1.myParent!=null) {
				node1.myParent.myRight = new Node("value", node1.myParent, 1);
			}
		}

		node2.myParent = node1.myParent;

		if(node2.dir == 0) {
			System.out.println("here2");
			node2.myParent.myRight = node1;
		}
		else {
			node2.myParent.myLeft = node1;
		}

		node1.myParent = temp.myParent;

		**/
	}


	public Tree copy() {
		Node newRoot = new Node(this.myRoot.getOperator(), new Node(this.myRoot),-1);
		copyHelper(newRoot, this.myRoot);
		Tree t = new Tree(newRoot);
		return t;

	}


	private void copyHelper(Node newRoot, Node oldRoot) {
		if(oldRoot.myLeft == null && oldRoot.myRight == null) {
			if(oldRoot.getConstant() != 0) {
				newRoot = new Node(oldRoot.getConstant(), new Node(oldRoot.myParent), oldRoot.dir);
			}
			else {
				newRoot = new Node("x", new Node(oldRoot.myParent), oldRoot.dir);
			}
			return;
		}
		if(oldRoot.myLeft.getConstant() != 0) {
			newRoot.myLeft = new Node(oldRoot.myLeft.getConstant(), new Node(oldRoot), oldRoot.myLeft.dir);
		}
		else {
			newRoot.myLeft = new Node(oldRoot.myLeft.getOperator(), new Node(oldRoot), oldRoot.myLeft.dir);
		}
		copyHelper(newRoot.myLeft, oldRoot.myLeft);


		if(oldRoot.myRight.getConstant() != 0) {
			newRoot.myRight = new Node(oldRoot.myRight.getConstant(), new Node(oldRoot), oldRoot.myRight.dir);
		}
		else {
			newRoot.myRight = new Node(oldRoot.myRight.getOperator(), new Node(oldRoot), oldRoot.myRight.dir);
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
		Tree t = new Tree(new Node("*", null,-1));

		t.myRoot.myLeft = new Node("+", t.myRoot,1);
		t.myRoot.myRight = new Node("x", t.myRoot,0);
		t.myRoot.myLeft.myLeft = new Node(10, t.myRoot.myLeft,1);
		t.myRoot.myLeft.myRight = new Node("x", t.myRoot.myLeft,0);
		Tree y = new Tree(new Node("/", null,-1));
		y.myRoot.myLeft = new Node("*", y.myRoot,1);
		y.myRoot.myRight = new Node("-", y.myRoot,0);
		y.myRoot.myLeft.myLeft = new Node("x", y.myRoot.myLeft,1);
		y.myRoot.myLeft.myRight = new Node("x", y.myRoot.myLeft,0);
		y.myRoot.myRight.myRight = new Node(12, y.myRoot.myRight,0);
		y.myRoot.myRight.myLeft = new Node(3, y.myRoot.myRight,1);
		t.myRoot.myLeft.myRight = y.myRoot.myLeft.myRight.myParent.myRight;
		Tree ty = t.copy();
		Tree yt = y.copy();
		ty.crossover(yt);
		ty.printTree();



	}
}
