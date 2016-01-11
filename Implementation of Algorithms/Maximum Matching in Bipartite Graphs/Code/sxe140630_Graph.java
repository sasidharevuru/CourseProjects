/*
 * Author : Sasidhar Evuru
 * Net ID : sxe140630
 * */
import java.util.ArrayList;
import java.util.Iterator;


/**
	 * Class to represent a graph
	 * 
	 *
	 */
	public class sxe140630_Graph implements Iterable<sxe140630_Vertex> {
		public ArrayList<sxe140630_Vertex> V; // array of vertices
		public int N; // number of verices in the graph
		public int actualNoOfNodes;

		/**
		 * Constructor for Graph
		 * 
		 * @param size
		 *            : int - number of vertices
		 */
		sxe140630_Graph(int size) {
			N = size;
			actualNoOfNodes = size;
			V = new ArrayList<sxe140630_Vertex>();
			// create an array of VXA_Vertex objects
			V.add(null);
			for (int i = 1; i <= size; i++)
				V.add(new sxe140630_Vertex(i));
		}

		/**
		 * Method to add an arc to the graph
		 * 
		 * @param a
		 *            : int - the head of the arc
		 * @param b
		 *            : int - the tail of the arc
		 * @param weight
		 *            : int - the weight of the arc
		 */
		void addVXA_Edge(int a, int b, int weight) {
			sxe140630_Edge e = new sxe140630_Edge(V.get(a), V.get(b), weight);
			V.get(a).Adj.add(e);
			V.get(b).Adj.add(e);
		}

		/**
		 * Method to create an instance of VXA_VertexIterator
		 */
		public Iterator<sxe140630_Vertex> iterator() {
			return new VXA_VertexIterator<sxe140630_Vertex>(V, N);
		}

		/**
		 * A Custom Iterator Class for iterating through the vertices in a graph
		 * 
		 *
		 * @param <VXA_Vertex>
		 */
		private class VXA_VertexIterator<VXA_Vertex> implements Iterator<VXA_Vertex> {
			private int nodeIndex = 0;
			private ArrayList<sxe140630_Vertex> iterV;// array of vertices to iterate through
			private int iterN; // size of the array

			/**
			 * Constructor for VXA_VertexIterator
			 * 
			 * @param v
			 *            : Array of vertices
			 * @param n
			 *            : int - Size of the graph
			 */
			private VXA_VertexIterator(ArrayList<sxe140630_Vertex> v, int n) {
				nodeIndex = 0;
				iterV = v;
				iterN = n;
			}

			/**
			 * Method to check if there is any VXA_Vertex left in the iteration
			 * Overrides the default hasNext() method of Iterator Class
			 */
			public boolean hasNext() {
				return nodeIndex != iterN;
			}

			/**
			 * Method to return the next VXA_Vertex object in the iteration
			 * Overrides the default next() method of Iterator Class
			 */
			@SuppressWarnings("unchecked")
			public VXA_Vertex next() {
				nodeIndex++;
				return (VXA_Vertex) iterV.get(nodeIndex);
			}

			/**
			 * Throws an error if a VXA_Vertex is attempted to be removed
			 */
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}

	}

