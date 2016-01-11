/*
 * Author : Sasidhar Evuru
 * Net ID : sxe140630
 * 
 * */
	class sxe140630_Edge{
		public sxe140630_Vertex From; // head VXA_Vertex
		public sxe140630_Vertex To; // tail VXA_Vertex
		public int Weight;// weight of the arc
		

		
		public sxe140630_Edge(sxe140630_Vertex u, sxe140630_Vertex v, int w) {
			From = u;
			To = v;
			Weight = w;
			
		}

		/**
		 * Method to find the other end end of the arc given a VXA_Vertex reference
		 * 
		 * @param u
		 *            : VXA_Vertex
		 * @return
		 */
		public sxe140630_Vertex otherEnd(sxe140630_Vertex u) {
			// if the VXA_Vertex u is the head of the arc, then return the tail
			// else return the tail
			if (From == u)
				return To;
			else
				return From;
		}

		/**
		 * Method to represent the edge in the form (x,y) where x is the head of
		 * the arc and y is the tail of the arc
		 */
		public String toString() {
			return "(" + From + "," + To + ")";
		}
	}
