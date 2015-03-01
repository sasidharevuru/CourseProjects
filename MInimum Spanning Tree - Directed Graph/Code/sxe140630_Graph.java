/*
 * Author Name : Sasidahr Evuru
 * Net-ID      : sxe140630
 */


import java.util.ArrayList;
import java.util.LinkedList;

public class sxe140630_Graph {
	private int no_of_edges;
	private int no_of_vertices;
	private ArrayList<LinkedList<sxe140630_Edge>> outgoingAdjacency;
	private ArrayList<LinkedList<sxe140630_Edge>> incomingAdjacency;
	private ArrayList<Boolean> nodevisitstatus;
	private ArrayList<Boolean> isnodeinoop;
	private int numberOfnodestobevisited;
	
	public ArrayList<Boolean> getnodevisitstatus() {
		return nodevisitstatus;
	}

	@SuppressWarnings("unchecked")
	public sxe140630_Graph(int no_of_vertices,int no_of_edges){
		this.no_of_edges = no_of_edges;
		this.no_of_vertices = no_of_vertices;
		outgoingAdjacency = new ArrayList<LinkedList<sxe140630_Edge>>();
		outgoingAdjacency.add(null);
		for(int i = 1;i<=no_of_vertices;i++)
		{
			outgoingAdjacency.add(new LinkedList<sxe140630_Edge>());
		}
		incomingAdjacency = new ArrayList<LinkedList<sxe140630_Edge>>();
		incomingAdjacency.add(null);
		for(int i = 1;i<=no_of_vertices;i++)
		{
			incomingAdjacency.add(new LinkedList<sxe140630_Edge>());
		}
		nodevisitstatus = new ArrayList<Boolean>();
		nodevisitstatus.add(null);
		for(int i = 1;i<=no_of_vertices;i++)
		{
			nodevisitstatus.add(false);
		}
		isnodeinoop = new ArrayList<Boolean>();
		isnodeinoop.add(null);
		for(int i = 1;i<=no_of_vertices;i++)
		{
			isnodeinoop.add(false);
		}
		numberOfnodestobevisited = no_of_vertices;
	}
	
	public void addEdge(int startvertex,int endvertex,int weight_of_edge,boolean isEdgeinAdjacencylist)
	{
		sxe140630_Vertex objstartvertex = new sxe140630_Vertex(startvertex);
		sxe140630_Vertex objendvertex = new sxe140630_Vertex(endvertex);
		sxe140630_Edge objedge = new sxe140630_Edge(objstartvertex, objendvertex, weight_of_edge,isEdgeinAdjacencylist);
		outgoingAdjacency.get(startvertex).add(objedge);
		incomingAdjacency.get(endvertex).add(objedge);
	}

	public ArrayList<LinkedList<sxe140630_Edge>> getIncomingAdjacency() {
		return incomingAdjacency;
	}


	public int getNo_of_edges() {
		return no_of_edges;
	}

	public void setNo_of_edges(int no_of_edges) {
		this.no_of_edges = no_of_edges;
	}

	public int getNo_of_vertices() {
		return no_of_vertices;
	}

	public void setNo_of_vertices(int no_of_vertices) {
		this.no_of_vertices = no_of_vertices;
	}

	public ArrayList<LinkedList<sxe140630_Edge>> getOutgoingAdjacency() {
		return outgoingAdjacency;
	}

	public int getNumberOfnodestobevisited() {
		return numberOfnodestobevisited;
	}

	public void setNumberOfnodestobevisited(int numberOfnodestobevisited) {
		this.numberOfnodestobevisited = numberOfnodestobevisited;
	}

	public ArrayList<Boolean> getIsnodeinoop() {
		return isnodeinoop;
	}

	public void setIsnodeinoop(ArrayList<Boolean> isnodeinoop) {
		this.isnodeinoop = isnodeinoop;
	}

	
	
}
