
public class Node {
private String myOperator;
private int myConstant;
public Node myLeft;
public Node myRight;
public Node myParent;


	public Node(String operator, Node parent) {
		myOperator = operator;
		myConstant = 0;
		myLeft = null;
		myRight = null;
		myParent = parent;
	}
	
	public Node(int constant, Node parent) {
		myConstant = constant;
		myLeft = null;
		myRight = null;
		myOperator = "c";
		myParent = parent;
	}
	 
	public Node(Node node) {
		myConstant = node.myConstant;
		myLeft = node.myLeft;
		myRight = node.myRight;
		myOperator = node.myOperator;
		myParent = node.myParent;
	}
	
	public int getConstant() {
		return myConstant;
	}
	
	
	
	
	public String getOperator() {
		return myOperator;
		
	}
}
