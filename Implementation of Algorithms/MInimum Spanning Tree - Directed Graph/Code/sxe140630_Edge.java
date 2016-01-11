/*
 * Author Name : Sasidahr Evuru
 * Net-ID      : sxe140630
 */

public class sxe140630_Edge {

	private sxe140630_Vertex objstartvertex;
	private sxe140630_Vertex objendvertex;
	private int weight_of_edge;
	private int originalweight;
	private boolean isEdgeinAdjacencylist;
	public sxe140630_Edge(sxe140630_Vertex objstartvertex,sxe140630_Vertex objendvertex,int weight_of_edge,boolean isEdgeinAdjacencylist)
	{
		this.objstartvertex = objstartvertex;
		this.objendvertex = objendvertex;
		this.weight_of_edge = weight_of_edge;
		this.originalweight = weight_of_edge;
		this.isEdgeinAdjacencylist = isEdgeinAdjacencylist;
	}
	public sxe140630_Vertex getStartvertex() {
		return objstartvertex;
	}
	public void setStartvertex(sxe140630_Vertex objstartvertex) {
		this.objstartvertex = objstartvertex;
	}
	public sxe140630_Vertex getEndvertex() {
		return objendvertex;
	}
	public void setEndvertex(sxe140630_Vertex objendvertex) {
		this.objendvertex = objendvertex;
	}
	public int getWeight_of_edge() {
		return weight_of_edge;
	}
	public void setWeight_of_edge(int weight_of_edge) {
		this.weight_of_edge = weight_of_edge;
	}
	public boolean getEdgeinAdjacencylist() {
		return isEdgeinAdjacencylist;
	}
	public void setEdgeinAdjacencylist(boolean isEdgeinAdjacencylist) {
		this.isEdgeinAdjacencylist = isEdgeinAdjacencylist;
	}
	public int getOriginalweight() {
		return originalweight;
	}
	public void setOriginalweight(int originalweight) {
		this.originalweight = originalweight;
	}
	
	
	
	
}
