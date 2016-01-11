/**
 * Author : Sasidhar Evuru
 * NetId : sxe140630
 * 
 * 
 * This is the node class, all the nodes have these properties listed in the node class.
 * */
import java.util.ArrayList;


public class Node {

	private Node parent; // parent of this node
	private ArrayList<Node> children; // list of children to this node.
	private ArrayList<Integer> attributevalue;  
	private double entropy;  // entropy of the node
	private ArrayList<Record> recordlist; // records that the node holds.
	private int classifyingattrnumber; // depending in what attribute value we classify further.
	private int classnumber; // will be -1 if non leaf node, else will 0 or 1.
	
	public Node()
	{
		setParent(null);
		setChildren(new ArrayList<Node>());
		setEntropy(0.0);
		setRecordlist(new ArrayList<Record>());
		setClassnumber(-1);
		setClassifyingattrnumber(-1);
		setAttributevalue(new ArrayList<Integer>());
	}
	
	
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}

	public double getEntropy() {
		return entropy;
	}
	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}
	public ArrayList<Record> getRecordlist() {
		return recordlist;
	}
	public void setRecordlist(ArrayList<Record> recordlist) {
		this.recordlist = recordlist;
	}


	public int getClassifyingattrnumber() {
		return classifyingattrnumber;
	}


	public void setClassifyingattrnumber(int classifyingattrnumber) {
		this.classifyingattrnumber = classifyingattrnumber;
	}


	public ArrayList<Node> getChildren() {
		return children;
	}


	public void setChildren(ArrayList<Node> children) {
		this.children = children;
	}


	public int getClassnumber() {
		return classnumber;
	}


	public void setClassnumber(int classnumber) {
		this.classnumber = classnumber;
	}


	public ArrayList<Integer> getAttributevalue() {
		return attributevalue;
	}


	public void setAttributevalue(ArrayList<Integer> attributevalue) {
		this.attributevalue = attributevalue;
	}
	
	
	
	
}
