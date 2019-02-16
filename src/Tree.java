import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Tree {
	public Node myRoot;
	public Double fitness;

	//makes a tree given a root
	public Tree(Node root) {
		myRoot = root;
	}

	//creates new random tree of specified depth
	public Tree(int goalDepth, HashMap<Double, Double> data, ArrayList<Double> testData){
		Node root = new Node(null, -1);
		randomTreeHelper(root, 0, goalDepth);
		myRoot = root;
		fitness = meanSquaredError(data, testData);
	}

	private static void randomTreeHelper(Node current, int depth, int goalDepth){
		Random rand = new Random();
		String operator = "";
		int constant;
		int chance;

		//if leaf
		if (depth == goalDepth){
			current.myLeft = null;
			current.myRight = null;
			chance = rand.nextInt(3);
			if (chance == 0) {
				current.setOperator("x");
				current.setConstant(0);
			}
			else{
				current.setOperator("c");
				current.setConstant(rand.nextInt(50) + 1);
			}
			return;
		}

		//internal
		chance = rand.nextInt(4);
		if(chance == 0) operator = "+";
		else if(chance == 1) operator = "-";
		else if(chance == 2) operator = "*";
		else if(chance == 3) operator = "/";

		current.setOperator(operator);
		current.setConstant(0);
		current.myLeft = new Node(current, 1);
		current.myRight = new Node(current, 0);
		randomTreeHelper(current.myLeft, depth + 1, goalDepth);
		randomTreeHelper(current.myRight, depth + 1, goalDepth);

		return;
	}

	//evaluates a tree given a value for x
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
		double left = 0;
		double right = 0;
		if(current.myLeft != null) {
			left = evaluate(current.myLeft, x);
		}
		if(current.myRight!=null) {
			right = evaluate(current.myRight, x);
		}

		if(current.getOperator() == "+") {
			value = left + right;
		}
		else if(current.getOperator() == "-") {
			value = left - right;
		}
		else if(current.getOperator() == "/") {
			if(right == 0.0) {
				value = left;
			}
			else {
				value = left / right;
			}
		}
		else {
			value = left * right;
		}

		return value;


	}
	public String evaluateEqn(Node current) {
		if(current.myLeft == null && current.myRight == null) {
			if(current.getConstant() != 0) {
				return  Integer.toString(current.getConstant());
			}
			else {
				return "x";
			}
		}
		String eqn;
		String left = "";
		String right = "";
		if(current.myLeft != null) {
			left = evaluateEqn(current.myLeft);
		}
		if(current.myRight!=null) {
			right = evaluateEqn(current.myRight);
		}

		eqn = "("+left + current.getOperator()+right+")";

		return eqn;



	}
	//returns number of nodes in tree
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

	//turns tree into list, good for random selection
	private static void NodeList(Node currentRoot, ArrayList<Node> nodes) {
		if(currentRoot == null) {
			return;
		}

		NodeList(currentRoot.myLeft, nodes);
		nodes.add(currentRoot);

		NodeList(currentRoot.myRight, nodes);
	}

	//crosses over two trees
	public static void crossover(Tree newTree1, Tree newTree2, HashMap<Double, Double> data, ArrayList<Double> testData) {
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

		newTree1.fitness = newTree1.meanSquaredError(data, testData);
		newTree2.fitness = newTree2.meanSquaredError(data, testData);
	}

	//mutates a random node within given tree
	public static void mutate(Tree tree, HashMap<Double, Double> data, ArrayList<Double> testData) {
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
				node.setConstant(rand.nextInt(50)+1);
			}
		}
		else if(node.getOperator() =="x") {
			node.setConstant(rand.nextInt(50)+1);
		}
		else {
			int operand = rand.nextInt(4);
			if(operand == 0) node.setOperator("+");
			else if(operand == 1) node.setOperator("-");
			else if(operand == 2) node.setOperator("*");
			else if(operand == 3) node.setOperator("/");
		}
		tree.fitness = tree.meanSquaredError(data, testData);
	}

	//copys a tree, returns root node
	public static Tree copy(Tree old){
		Tree t = new Tree(copyHelper(old.myRoot));
		t.fitness = old.fitness;
		return t;
	}
	private static Node copyHelper(Node node) {
		Node left = null;
		Node right = null;
		Node new1 = null;
		if(node.myLeft != null) {
			left = copyHelper(node.myLeft);

		}
		if(node.myRight != null) {
			right = copyHelper(node.myRight);

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

	//Caps the depth limit of a tree and replaces all nodes at that depth leaf nodes
	public void depthLimit(int limit) {
		Random rand = new Random();
		int depth = 0;
		depthHelper(depth, limit, this.myRoot, rand);

	}

	private void depthHelper(int depth, int limit, Node currentRoot, Random rand) {
		if(currentRoot.myLeft ==null && currentRoot.myRight ==null) {
			return;
		}
		if(depth == limit) {
			currentRoot.myRight = null;
			currentRoot.myLeft = null;
			int chance = rand.nextInt(3);
			if (chance == 0) {
				currentRoot.setOperator("x");
				currentRoot.setConstant(0);
			}
			else{
				currentRoot.setOperator("c");
				currentRoot.setConstant(rand.nextInt(50) + 1);
			}
			return;
		}
		depth++;
		if(currentRoot.myLeft != null) {
			depthHelper(depth, limit, currentRoot.myLeft, rand);
		}
		if(currentRoot.myRight != null) {
			depthHelper(depth, limit, currentRoot.myRight, rand);
		}
	}

	public int maxDepth() {
		int depth = 0;
		depth = maxDepthHelper(this.myRoot, depth);
		return depth;
	}
	private int maxDepthHelper(Node current, int depth) {
		if(current.myLeft == null && current.myRight == null) {
			return depth;
		}
		depth++;
		int left = maxDepthHelper(current.myLeft, depth);
		int right = maxDepthHelper(current.myRight, depth);
		return Math.max(left, right);
	}
	//print the tree
	public void printTree() {
		if(myRoot != null) {
			printInOrder(myRoot, 0);
		}
	}

	//prints tree in order
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

	public double meanSquaredError(HashMap<Double, Double> data, ArrayList<Double> testData) {
		double sum = 0;
		for(double x : testData) {
			sum+= Math.pow(((this.evaluate(this.myRoot, x) - data.get(x))), 2);
		}
		return Math.floor(Math.sqrt(sum/testData.size())*1000)/1000;
	}
	public double meanError(HashMap<Double, Double> data, ArrayList<Double> testData) {
		double sum = 0;
		for(double x : testData) {
			sum+= Math.abs(((this.evaluate(this.myRoot, x) - data.get(x))));
		}
		return Math.floor((sum/testData.size())*1000)/1000;
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
		//System.out.println("___T___");
		//t.printTree();
		//System.out.println("______");
		//System.out.println("___Y___");
		//y.printTree();
		//System.out.println("______");
		//System.out.println("_______");
		//System.out.println();


		//Tree random = new Tree(3);
		//random.printTree();

		//random2.printTree();

		//System.out.println(random.evaluate(random.myRoot,1));

	}
}
