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



	private static void NodeList(Node currentRoot, ArrayList<Node> nodes) {
		if(currentRoot == null) {
			return;
		}

		NodeList(currentRoot.myLeft, nodes);
		nodes.add(currentRoot);

		NodeList(currentRoot.myRight, nodes);

	}

	public static void crossover(Tree newTree1, Tree newTree2) {
		Random rand = new Random();
		ArrayList<Node> Tree1 = new ArrayList<Node>();
		ArrayList<Node> Tree2 = new ArrayList<Node>();


		NodeList(newTree1.myRoot, Tree1);
		NodeList(newTree2.myRoot, Tree2);
		Node node1 = Tree1.get(rand.nextInt(Tree1.size()-1));
		Node node2 = Tree2.get(rand.nextInt(Tree2.size()-1));
		while(node1.myParent == null) node1 = Tree1.get(rand.nextInt(Tree1.size()-1));

		while(node2.myParent ==null) node2 = Tree2.get(rand.nextInt(Tree2.size()-1));
		Node temp = new Node(node1);


		if(node2.dir == 1) {
			node2.myParent.myLeft = node1;
		}
		else if(node2.dir ==0) {
			node2.myParent.myRight = node1;
		}
		node1.myParent = node2.myParent;
		if(temp.dir == 1) {
			temp.myParent.myLeft = node2;
		}
		else if(temp.dir == 0) {
			temp.myParent.myRight = node2;
		}
		node2.myParent = temp.myParent;



	}

	public static void mutate(Tree tree) {
		Random rand = new Random();
		ArrayList<Node> Tree1 = new ArrayList<Node>();
		NodeList(tree.myRoot, Tree1);

		Node node = Tree1.get(rand.nextInt(Tree1.size()-1));
		if(node.getConstant()!= 0){
			int chance = rand.nextInt(3);
			if (chance == 0){
				node.setOperator("x");
				node.setConstant(0);
			}
			else{
				node.setConstant(rand.nextInt(100)+1);
			}
		}
		else if(node.getOperator() =="x") {
			node.setConstant(rand.nextInt(100)+1);
		}
		else {
			int operand = rand.nextInt(3);
			if(operand == 0) node.setOperator("+");
			else if(operand == 1) node.setOperator("-");
			else if(operand == 2) node.setOperator("*");
			else if(operand == 3) node.setOperator("/");
		}
	}
	public static Node copy(Node node) {
		Node left = null;
		Node right = null;
		Node new1 = null;
		if(node.myLeft != null) {
			left = copy(node.myLeft);

		}
		if(node.myRight != null) {
			right = copy(node.myRight);

		}

		if(node.getConstant() != 0) {
			new1 = new Node(left, right, node.getConstant(), node.dir);
		}
		else {
			new1 = new Node(left,right,node.getOperator(),node.dir);
		}
		if(left != null && right != null) {
			left.myParent = new1;
			right.myParent = new1;
		}
		return new1;

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
		if(currentRoot.getConstant() != 0 ) {
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
		y.myRoot.myLeft.myLeft = new Node(23, y.myRoot.myLeft,1);
		y.myRoot.myLeft.myRight = new Node("x", y.myRoot.myLeft,0);
		y.myRoot.myRight.myRight = new Node("x", y.myRoot.myRight,0);
		y.myRoot.myRight.myLeft = new Node(3, y.myRoot.myRight,1);
		System.out.println("___T___");
		t.printTree();
		System.out.println("______");
		System.out.println("___Y___");
		y.printTree();
		System.out.println("______");
		System.out.println("_______");
		System.out.println();

		Tree f = new Tree(copy(t.myRoot));
		Tree s = new Tree(copy(y.myRoot));
		Tree m = new Tree(copy(s.myRoot));
		crossover(f, s);
		System.out.println("___F = TxY___");
		f.printTree();
		System.out.println("___S = YxT___");
		s.printTree();

		System.out.println("___F mutated___");
		//crossover(f,m);
		mutate(f);
		f.printTree();
		System.out.println(f.evaluate(f.myRoot,1));
		System.out.println("_______");
		//m.printTree();
		//y.printTree();





	}
}
