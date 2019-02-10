import java.util.Objects;

public class Node {
	private String myOperator;
	private int myConstant;
	public Node myLeft;
	public Node myRight;
	public Node myParent;
	public int dir;


	public Node(String operator, Node parent, int d) {
		myOperator = operator;
		myConstant = 0;
		myLeft = null;
		myRight = null;
		myParent = parent;
		dir = d;
	}

	public Node(int constant, Node parent, int d) {
		myConstant = constant;
		myLeft = null;
		myRight = null;
		myOperator = "c";
		myParent = parent;
		dir = d;
	}

	public Node(Node node) {
		myConstant = node.myConstant;
		myLeft = node.myLeft;
		myRight = node.myRight;
		myOperator = node.myOperator;
		myParent = node.myParent;
		dir = node.dir;
	}

	public int getConstant() {
		return myConstant;
	}

	public String getOperator() {
		return myOperator;

	}
/**
	public int hashCode() {
		int h = 31 * Objects.hashCode(myOperator);
		h += 31 * myConstant;
		if (myLeft != null) h = h* 31 + myLeft.hashCode();
		if (myRight != null) h = h * 31 + myRight.hashCode();
		if (myParent != null) h = h * 31 + myParent.hashCode();
		return h;
	}

	public boolean equals(Node other) {
		if(!(this.myConstant == other.myConstant)) return false;
		else if(!(this.myOperator == other.myOperator)) return false;
		else if((this.myLeft==null && other.myLeft==null) && (this.myRight==null && other.myRight==null)) {
			if((this.myParent.equals(other.myParent))) return true;
		}

		else if(!( (this.myLeft==null && other.myLeft==null) || (this.myLeft!=null && other.myLeft!=null) )) return false;
		else if(!( (this.myRight==null && other.myRight==null) || (this.myRight!=null && other.myRight!=null) )) return false;
		else if(!( (this.myParent==null && other.myParent==null) || (this.myParent!=null && other.myParent!=null) )) return false;
		return (this.myLeft.equals(other.myLeft) && this.myRight.equals(other.myRight));



	}
	**/
}
