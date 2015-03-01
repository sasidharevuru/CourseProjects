/*
 * Author Name : Sasidahr Evuru
 * Net-ID      : sxe140630
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;
import java.util.List;

public class sxe140630_Assignment7 {
	
	static Stack<sxe140630_stackobject> shrinkstack = new Stack<sxe140630_stackobject>();
	static long MSTweight = 0;
	static boolean canprint = false;
	static HashMap<Integer, String> opHashMap = new HashMap<Integer, String>();

	public static void main(String[] args) throws IOException {
		
		long starttime = System.currentTimeMillis();
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		String inputline = null;
		int linenumber = 1;
		int source = 0;
		sxe140630_Graph objGraph = null;
		inputline = input.readLine();
		int no_of_vertices = 0;
		while(inputline != null)
		{
			inputline = inputline.replaceAll("[\t]", " ");
			inputline = inputline.replaceAll("[ ]+", " ");
			if(inputline.charAt(0)==' '){
				inputline = inputline.substring(1);
			}
			String[] linesplit = inputline.split(" ");
			if(linenumber == 1){
				no_of_vertices = Integer.parseInt(linesplit[0]);
				int no_of_edges = Integer.parseInt(linesplit[1]);
				source = Integer.parseInt(linesplit[2]);
				objGraph = new sxe140630_Graph(no_of_vertices, no_of_edges);
			}
			else
			{
				int startvertex = Integer.parseInt(linesplit[0]);
				int endvertex = Integer.parseInt(linesplit[1]);
				int weight_of_edge = Integer.parseInt(linesplit[2]);
				objGraph.addEdge(startvertex, endvertex, weight_of_edge,true);
			}
			inputline = input.readLine();
			linenumber++;
		}
		
		findMST(objGraph, source);
		long endtime = System.currentTimeMillis();
		System.out.println(MSTweight+" "+(endtime-starttime));
		if(no_of_vertices <= 50){
			for (Map.Entry<Integer,String> entry : opHashMap.entrySet())
			{
			  String value = entry.getValue();
			  System.out.println(value);
			}
		}
		
		
}

	private static void findMST(sxe140630_Graph objGraph, int source) {
		// Except for the source fro the remaining nodes subtract the lease incoming edge on every node.
		LinkedList<sxe140630_Edge> edgesincoming = null;
		ArrayList<LinkedList<sxe140630_Edge>> incomingAdjacency = objGraph.getIncomingAdjacency();
		
		for(int i =1 ; i<= objGraph.getNo_of_vertices(); i++)
		{
			if(i!= source)
			{
				edgesincoming = incomingAdjacency.get(i);
				ListIterator<sxe140630_Edge> listIterator = edgesincoming.listIterator();
				// finding the min weighted edge in the graph.
				int min_weight_incoming_edge = Integer.MAX_VALUE;
				while(listIterator.hasNext())
				{
					sxe140630_Edge objEdge = listIterator.next();
					if(objEdge.getEdgeinAdjacencylist()){
						if(min_weight_incoming_edge > objEdge.getWeight_of_edge()){
							min_weight_incoming_edge = objEdge.getWeight_of_edge();
						}
					}	
				}
				
				if(min_weight_incoming_edge != Integer.MAX_VALUE)
				{
					MSTweight = MSTweight+min_weight_incoming_edge;
				}
				listIterator = edgesincoming.listIterator();
				while(listIterator.hasNext())
				{
					sxe140630_Edge objEdge = listIterator.next();
					if(objEdge.getEdgeinAdjacencylist()){
						int previousweight = objEdge.getWeight_of_edge();
						objEdge.setWeight_of_edge(previousweight-min_weight_incoming_edge);
					}
					
				}
				
			}
		}
		
		boolean areallnodesvisited = dfs(objGraph,source);
		
		if(areallnodesvisited)
		{
			// expanding the shrinked nodes.
			canprint = true;
			boolean isstackused = false;
			while(!shrinkstack.isEmpty())
			{
				isstackused = true;
				sxe140630_stackobject objStackobject = shrinkstack.pop();
				int nodevertex = objStackobject.getVertextnumber();
				List<sxe140630_Edge> objsavedEdges = objStackobject.getSavededges();
				List<sxe140630_Edge> objloopEdges = objStackobject.getSavedloopedgeslist();
				LinkedList<sxe140630_Edge> objincomingll = objGraph.getIncomingAdjacency().get(nodevertex);
				LinkedList<sxe140630_Edge> objoutgoingll = objGraph.getOutgoingAdjacency().get(nodevertex);
				ListIterator<sxe140630_Edge> listIterator = objincomingll.listIterator();
				int min_weight_incoming_edge = Integer.MAX_VALUE;
				int startvertexofsmallestedge = 0;
				while(listIterator.hasNext())
				{
					sxe140630_Edge objEdge = listIterator.next();
					if(min_weight_incoming_edge > objEdge.getWeight_of_edge())
					{
						min_weight_incoming_edge = objEdge.getWeight_of_edge();
						startvertexofsmallestedge = objEdge.getStartvertex().getVertexindex();
					}
					objEdge.setEdgeinAdjacencylist(false);	
				}
				listIterator = objsavedEdges.listIterator();
				int savededgevertexinloop = 0;
				while(listIterator.hasNext()){
					sxe140630_Edge objEdge = listIterator.next();
					if(objEdge.getStartvertex().getVertexindex() == startvertexofsmallestedge)
					{
						objEdge.setEdgeinAdjacencylist(true);
						objEdge.setWeight_of_edge(0);
						savededgevertexinloop = objEdge.getEndvertex().getVertexindex();
						break;
					}
				}
				// enabling the loop.
				listIterator = objloopEdges.listIterator();
				while(listIterator.hasNext()){
					sxe140630_Edge objEdge = listIterator.next();
					if(objEdge.getEndvertex().getVertexindex() == savededgevertexinloop)
					{
						//still keeping this edge disabled.
					}
					else{
						objEdge.setEdgeinAdjacencylist(true);
					}
				}
				
				// enabling the outgoing edge.
				
				listIterator = objsavedEdges.listIterator();
				while(listIterator.hasNext()){
					sxe140630_Edge objEdge = listIterator.next();
					objEdge.setEdgeinAdjacencylist(true);
				}
	
			}
			if(isstackused){
				for(int i = 1; i<= objGraph.getNo_of_vertices();i++)
				{
					objGraph.getnodevisitstatus().set(i, false) ;
				}
				dfs(objGraph, source);
			}
		}
		else{
			// trying to find the univisited node by dfs.
			int first_unvisited_vertex_dfs = findunvisitednode(objGraph.getnodevisitstatus(),objGraph.getIsnodeinoop());
			// taking the first_unvisited_vertex_dfs and traveling backward on zero weighted path.
			boolean[] node_detected_on_loop = new boolean[objGraph.getNo_of_vertices()+1];
			Stack<sxe140630_Edge> objStack = new Stack<sxe140630_Edge>();
			ArrayList<sxe140630_Edge> loopEdgeslist = null;
			ArrayList<Integer> loppverticeslist = null;
			
			do
			{
				LinkedList<sxe140630_Edge> linkedListEdges = objGraph.getIncomingAdjacency().get(first_unvisited_vertex_dfs);
				
				ListIterator<sxe140630_Edge> listIterator = linkedListEdges.listIterator();
				while(listIterator.hasNext())
				{
					sxe140630_Edge objEdge = listIterator.next();
					if(objEdge.getEdgeinAdjacencylist()){
						if(objEdge.getWeight_of_edge() == 0)
						{
							node_detected_on_loop[objEdge.getEndvertex().getVertexindex()] = true;
							first_unvisited_vertex_dfs = objEdge.getStartvertex().getVertexindex();
							objStack.push(objEdge);
							break;
						}	
					}
					
				}
			}while((objStack.size() <= objGraph.getNo_of_edges()) && (!node_detected_on_loop[first_unvisited_vertex_dfs]));
			if(objStack.size() < objGraph.getNo_of_edges()){
				loopEdgeslist = new ArrayList<sxe140630_Edge>();
				loppverticeslist = new ArrayList<Integer>();
				
				boolean isloopbackReached = false;
				sxe140630_Edge objEdge = null;
				while(!objStack.isEmpty() && !isloopbackReached)
				{
					objEdge = objStack.pop();
					loppverticeslist.add(objEdge.getStartvertex().getVertexindex());
					loopEdgeslist.add(objEdge);
					if(objEdge.getEndvertex().getVertexindex() == first_unvisited_vertex_dfs)
					{
						isloopbackReached = true;
					}
				}
				objStack.clear();
				
				// creating a new vertex to represnt the loop.
				int indexOfNewNode = objGraph.getNo_of_vertices()+1;
				objGraph.setNo_of_vertices(indexOfNewNode);
				sxe140630_Vertex newvertex = new sxe140630_Vertex(indexOfNewNode);
				objGraph.getOutgoingAdjacency().add(new LinkedList<sxe140630_Edge>());
				objGraph.getIncomingAdjacency().add(new LinkedList<sxe140630_Edge>());
				objGraph.getnodevisitstatus().add(false);
				objGraph.getIsnodeinoop().add(false);
				// creating Hashmap of outgoing and incoming edges out of each node.
				HashMap<Integer, sxe140630_Edge> outgoingHashmap = new  HashMap<Integer,sxe140630_Edge>();
				HashMap<Integer, sxe140630_Edge> incomingHashmap = new  HashMap<Integer,sxe140630_Edge>();
				List<sxe140630_Edge> saveedgeslist = new ArrayList<sxe140630_Edge>();
				// getting the outgoing adjacent list of all nodes in the loop.
				for(int i = 0 ; i< loppverticeslist.size();i++){
					int vertex_in_loop =  loppverticeslist.get(i);
					LinkedList<sxe140630_Edge> lloutgoing_edges_from_vertex = objGraph.getOutgoingAdjacency().get(vertex_in_loop);
					ListIterator<sxe140630_Edge> listIterator = lloutgoing_edges_from_vertex.listIterator();
					while(listIterator.hasNext())
					{
						objEdge = listIterator.next();
						if(objEdge.getEdgeinAdjacencylist()){
							int outgoingveretx = objEdge.getEndvertex().getVertexindex();
							if(loppverticeslist.contains(outgoingveretx)){
								objEdge.setEdgeinAdjacencylist(false);
							}
							else{
								sxe140630_Edge edgeinHashmap = outgoingHashmap.get(outgoingveretx);
								if(edgeinHashmap == null){
									 // there is no node on hashmap with that outgoing.
									outgoingHashmap.put(outgoingveretx, objEdge);
									// putting it in outgoing adjacency list of new node.
									objEdge.setEdgeinAdjacencylist(false);
								}
								else{
									// code to replace the edge.
									if(edgeinHashmap.getWeight_of_edge() > objEdge.getWeight_of_edge())
									{
										outgoingHashmap.put(outgoingveretx, objEdge);
										objEdge.setEdgeinAdjacencylist(false);
										
									}
									else{
										objEdge.setEdgeinAdjacencylist(false);
									}
								}
								
							}
						}
						
					}
					
					// getting the incoming adjacent list of all nodes in the loop.
					
					LinkedList<sxe140630_Edge> llincoming_edges_from_vertex = objGraph.getIncomingAdjacency().get(vertex_in_loop);
					ListIterator<sxe140630_Edge> listIterator1 = llincoming_edges_from_vertex.listIterator();
					while(listIterator1.hasNext())
					{
						objEdge = listIterator1.next();
						if(objEdge.getEdgeinAdjacencylist()){
							int incominggveretx = objEdge.getStartvertex().getVertexindex();
							if(loppverticeslist.contains(incominggveretx)){
								objEdge.setEdgeinAdjacencylist(false);
							}
							else{
								sxe140630_Edge edgeinHashmap = incomingHashmap.get(incominggveretx);
								if(edgeinHashmap == null){
									 // there is no node on hashmap with that outgoing.
									incomingHashmap.put(incominggveretx, objEdge);
									// putting it in outgoing adjacency list of new node.
									objEdge.setEdgeinAdjacencylist(false);
								}
								else{
									// code to replace the edge.
									if(edgeinHashmap.getWeight_of_edge() > objEdge.getWeight_of_edge())
									{
										incomingHashmap.put(incominggveretx, objEdge);
										objEdge.setEdgeinAdjacencylist(false);
										
									}
									else{
										objEdge.setEdgeinAdjacencylist(false);
									}
								}
								
							}
						}
				} 
			}
				
				
				for (Map.Entry<Integer,sxe140630_Edge> entry : outgoingHashmap.entrySet())
				{
				  int key = entry.getKey();
				  sxe140630_Edge value = entry.getValue();
				  
				  sxe140630_Vertex outgoingvertex = value.getEndvertex();
				  saveedgeslist.add(value);
				  sxe140630_Edge newEdge = new sxe140630_Edge(newvertex,outgoingvertex,value.getWeight_of_edge(),true);
				  objGraph.getOutgoingAdjacency().get(indexOfNewNode).add(newEdge);
				  objGraph.getIncomingAdjacency().get(outgoingvertex.getVertexindex()).add(newEdge);
				}
				
				for (Map.Entry<Integer,sxe140630_Edge> entry : incomingHashmap.entrySet())
				{
				  int key = entry.getKey();
				  sxe140630_Edge value = entry.getValue();
				  
				  sxe140630_Vertex outgoingvertex = value.getStartvertex();
				  saveedgeslist.add(value);
				  sxe140630_Edge newEdge = new sxe140630_Edge(outgoingvertex,newvertex,value.getWeight_of_edge(),true);
				  objGraph.getIncomingAdjacency().get(indexOfNewNode).add(newEdge);
				  objGraph.getOutgoingAdjacency().get(outgoingvertex.getVertexindex()).add(newEdge);
				}
				
				
				// adding the loop edges to be saved.
				List<sxe140630_Edge> savedloopedgeslist = new ArrayList<sxe140630_Edge>();
				for(int i = 0; i< loopEdgeslist.size(); i++){
					sxe140630_Edge obEdge2 = loopEdgeslist.get(i);
					savedloopedgeslist.add(obEdge2);
				}
				
				
				// add this to the stack.
				sxe140630_stackobject objStackobject = new sxe140630_stackobject(indexOfNewNode, saveedgeslist,savedloopedgeslist);
				shrinkstack.add(objStackobject);
		}
			// reset all visitstatus variable for each vertex.
			
			// mark the nodes which are in loop.
			
			for(int i = 0 ; i< loppverticeslist.size(); i++)
			{
				int vertex = loppverticeslist.get(i);
				objGraph.getIsnodeinoop().set(vertex, true);
			}
			
			for(int i = 1; i<= objGraph.getNo_of_vertices();i++)
			{
				objGraph.getnodevisitstatus().set(i, false) ;
			}
			
			
			
			objGraph.setNumberOfnodestobevisited(objGraph.getNumberOfnodestobevisited()-loppverticeslist.size()+1);
			findMST(objGraph, source);
	}
		
		
	
	}
	
	private static int findunvisitednode(ArrayList<Boolean> arrayList, ArrayList<Boolean> arrayList2) {
		int unvisited_node_index = 0;
		for(int i = 1;i<=arrayList.size();i++)
		{
			if((arrayList.get(i)==false) && (arrayList2.get(i)==false) )
			{
				unvisited_node_index = i;
				break;
			}
		}
		return unvisited_node_index;
	}

	public static boolean dfs(sxe140630_Graph objGraph,int source)
	   {
	      //DFS uses Stack data structure

	      Stack<Integer> s = new Stack<Integer>();
	      int no_of_nodes_visited = 0;
	      
	      objGraph.getnodevisitstatus().set(source,true);
	      no_of_nodes_visited++;
	      s.push(source);
	      
	      while( !s.isEmpty() )
	      {
	         int n, child;
	        
	         n = (s.peek()).intValue();
	         sxe140630_Edge objEdge = getZeroweightUnvisitedChildNode(objGraph,n);
	         if(objEdge != null){
	        	 child = objEdge.getEndvertex().getVertexindex();
	         }
	         else{
	        	 child = -1; 
	         }
	         if ( child != -1 )
	         {
	        	objGraph.getnodevisitstatus().set(child, true);
	  	      	no_of_nodes_visited++;
	  	      	if(canprint){
	  	      	opHashMap.put(objEdge.getEndvertex().getVertexindex(), String.valueOf(objEdge.getStartvertex().getVertexindex()+" "+objEdge.getEndvertex().getVertexindex()+" "+objEdge.getOriginalweight()));	
	  	      	}
	  	    	 
	            s.push(child);
	         }
	         else
	         {
	            s.pop();
	         }
	      }

	      if(no_of_nodes_visited == (objGraph.getNumberOfnodestobevisited())){
	    	  return true;
	      }
	      else{
	    	  return false;
	      }
	   }

	private static sxe140630_Edge getZeroweightUnvisitedChildNode(sxe140630_Graph objGraph, int n) {
		
		LinkedList<sxe140630_Edge> outgoingedEdges = objGraph.getOutgoingAdjacency().get(n);
		ListIterator<sxe140630_Edge> listIterator = outgoingedEdges.listIterator();
		sxe140630_Edge objEdge = null;
		while(listIterator.hasNext()){
			objEdge = listIterator.next();
			if(objEdge.getEdgeinAdjacencylist()){
				if((objEdge.getWeight_of_edge() == 0) && (objGraph.getnodevisitstatus().get(objEdge.getEndvertex().getVertexindex()) == false) ){
					objGraph.getnodevisitstatus().set(objEdge.getEndvertex().getVertexindex(), true);
					break;
				}
				else{
					objEdge = null;
				}
			}
			else{
				objEdge = null;
			}
			
		}
		
		return objEdge;
		
	}
	
	
	
}
