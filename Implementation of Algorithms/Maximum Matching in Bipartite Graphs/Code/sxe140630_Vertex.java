/*
 * Author : Sasidhar Evuru
 * Net ID : sxe140630
 * 
 * */

import java.util.LinkedList;
/**
	 * Class to represent a vertex of a graph
	 * 
	 *
	 */
	class sxe140630_Vertex {
		public int name; // name of the vertex
		public boolean seen; // flag to check if the vertex has already been
		public sxe140630_Vertex mate; // mate of the node
		public LinkedList<sxe140630_Edge> Adj; // outgoing adjacency list
		public sxe140630_Vertex parent;
		public sxe140630_Vertex root;
		public int inoutstatus;
		/**
		 * Constructor for the vertex
		 * 
		 * @param n
		 *           : int - name of the vertex
		 */
		sxe140630_Vertex(int n) {
			name = n;
			seen = false;
			mate = null;
			parent = null;
			root = null;
			inoutstatus = -1;
			Adj = new LinkedList<sxe140630_Edge>();	
		}

		/**
		 * Method to represent a vertex by its name
		 */
		public String toString() {
			return Integer.toString(name);
		}
	}
