/*
 * Author : Sasidhar Evuru
 * Net ID : sxe140630
 * 
 * */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


public class sxe140630_Assignment9_basic {

	static long starttime ;
	static int msize = 0;
	public static final int inner = 0;
	public static final int outer = 1;
	public static void main(String[] args) throws FileNotFoundException {
		
		Scanner in;
		if (args.length > 0) {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} else {
			in = new Scanner(System.in);
		}

		
		// read the VXA_Graph related parameters
		int n = in.nextInt(); // number of vertices in the VXA_Graph
		int m = in.nextInt(); // number of VXA_Edges in the VXA_Graph

		// create a VXA_Graph instance
		sxe140630_Graph g = new sxe140630_Graph(n);
		for (int i = 0; i < m; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			int w = in.nextInt();
			g.addVXA_Edge(u, v, w);
		}
		in.close();
		starttime = System.currentTimeMillis();
		boolean isbipartite = isbipartite(g,g.V.get(1));
		if(!isbipartite)
		{
			System.out.println("G is not bipartite");
		}
		else
		{
			findmaximalMatching(g); // finding maximal amrching in a graph.
			findmaxMatching(g);
			long endtime = System.currentTimeMillis();
			System.out.println(msize+" "+(endtime-starttime));
			
			if(n<=100)
			{
				for(int i = 1; i<=n;i++)
				{
					sxe140630_Vertex matchedvertexed = g.V.get(i).mate;
					if(matchedvertexed != null)
					{
						System.out.println(i+" "+matchedvertexed.name);
					}
					else
					{
						System.out.println(i+" "+"-");
					}
				}
			}
		}
		
		
	}
	/**
	 * 
	 * @param g = the graph which we are checking
	 * @param src = passing the source intially to do BFS
	 * @return retruns whether the gicen grapg is bipartite or not.
	 */
	private static boolean isbipartite(sxe140630_Graph g, sxe140630_Vertex src) 
	{
		Queue<sxe140630_Vertex> vertextqueue = new LinkedList<sxe140630_Vertex>();
		
		vertextqueue.add(src); // intially adding src to queue.
		src.seen = true;
		src.inoutstatus = 0; // intially setting src as inner node.
		boolean isBipartite = true;
		while(!vertextqueue.isEmpty())
		{
			// remove a vertex from the head of the queue
			sxe140630_Vertex u = vertextqueue.remove();
			boolean isbreakset = false;
			// iterate through the u's adjacency list
			for (sxe140630_Edge e : u.Adj) {
				sxe140630_Vertex v = e.otherEnd(u);
				/*
				 * if the vertex v is not visited then mark v as visited and
				 * update v's distance and parent and then add v to the queue
				 */
				if (!v.seen) 
				{
					v.seen = true;
					v.parent = u;
					v.inoutstatus = (u.inoutstatus == 1)?0:1; // if u is inner setting v as outer and viceversa.
					vertextqueue.add(v);
				} 
				else {
					
					/*
					 * if the ends of edge (u,v), vertices u and v, are at the 
					 * same distance from the source, the graph is not bipartite
					 */
					if (u.inoutstatus == v.inoutstatus)
					{
						// the nodes adjacent to each other have same inout status.
						isBipartite = false;
						isbreakset = true;
						break;
					}
				}
			}
			if(isbreakset)
			{
				break;
			}
		}
		
		return isBipartite;
		
	}
	private static void findmaxMatching(sxe140630_Graph g) 
	{
	
		
		while(true)
		{
			Queue<sxe140630_Vertex> verticesQueue = new LinkedList<sxe140630_Vertex>();
			boolean runagain = false;
			
			for(int i =1; i<= g.N; i++)
			{
				sxe140630_Vertex u = g.V.get(i);
				u.seen = false;
				u.parent = null;
				u.root = null;
				if(u.mate==null && u.inoutstatus == 1)
				{
					// encountered a free outer node.
					u.seen = true;
					verticesQueue.add(u);
				}
			}
			
			while(!verticesQueue.isEmpty())
			{
				sxe140630_Vertex u = verticesQueue.remove();
				sxe140630_Vertex root;
				if(u.root == null)
				{
					// it doesnt have a root already so new tree.
					u.root = u;
					root = u;
				}
				else
				{
					root = u.root;
				}
				for(sxe140630_Edge e : u.Adj)
				{
					sxe140630_Vertex v = e.otherEnd(u);
					if(!v.seen)
					{
						v.parent = u;
						v.seen = true;
						v.root = root;
						if(v.mate == null && v.root.mate == null)
						{
							// here we are checking if root.mate == null because the graph wont have augmenting path is the augmenting path in a tree is already detected and root will always be present in the augmenting path
							processAugPath(g,v); 
							runagain = true; // if augmenting path is found we have to run the while(true)loop again
 							break;
						}
						else if(root.mate == null)
						{
							sxe140630_Vertex x= v.mate;
							x.seen = true;
							x.parent = v;
							x.root = root;
							verticesQueue.add(x); // adding the vertex to the queue. 
						}
					}
				}
			}
			if(verticesQueue.isEmpty()&&!runagain)
			{
				break; // we don not have augmenting path and the queue is empty so we break the while loop and there are no more augmenting paths.
			}
		}
	}
	
	
	//Helper function to increase size of matching by 1, using an augmenting path
	private static void processAugPath(sxe140630_Graph g, sxe140630_Vertex u) 
	{	
		// u is a free inner node with an augmenting path to the root of the tree
		sxe140630_Vertex p = u.parent;
		sxe140630_Vertex x = p.parent;
		u.mate = p;
		p.mate = u;
		while(x != null) // go up two steps from outer node x
		{
			sxe140630_Vertex nmx = x.parent;
			sxe140630_Vertex y = nmx.parent;
			x.mate = nmx;
			nmx.mate = x;
			x = y;
		}
		msize++;
	}
	
	/**
	 * 
	 * @param g = Graph g.
	 */
	private static void findmaximalMatching(sxe140630_Graph g)
	{
		for(int i =1; i<= g.N; i++)
		{
			sxe140630_Vertex u = g.V.get(i);
			if(u.mate == null)
			{
				for (sxe140630_Edge e : u.Adj) 
				{
						sxe140630_Vertex v = e.otherEnd(u);
						if((u.mate == null)&&(v.mate == null))
						{
							u.mate = v;
							v.mate = u;
							msize++;
						}
					}
			}			
		}
	}
	
	
	
	}
