import java.util.List;

/*
 * Author Name : Sasidahr Evuru
 * Net-ID      : sxe140630
 */
public class sxe140630_stackobject {

	// used fro bundling the savings while shrinking to expand back.
	private int vertextnumber;
	private List<sxe140630_Edge> savededges;
	private List<sxe140630_Edge> savedloopedgeslist;
	
	public sxe140630_stackobject(int vertextnumber, List<sxe140630_Edge> savededges, List<sxe140630_Edge> savedloopedgeslist)
	{
		this.vertextnumber = vertextnumber;
		this.savededges = savededges;
		this.setSavedloopedgeslist(savedloopedgeslist);
	}

	public int getVertextnumber() {
		return vertextnumber;
	}

	public void setVertextnumber(int vertextnumber) {
		this.vertextnumber = vertextnumber;
	}

	public List<sxe140630_Edge> getSavededges() {
		return savededges;
	}

	public void setSavededges(List<sxe140630_Edge> savededges) {
		this.savededges = savededges;
	}

	public List<sxe140630_Edge> getSavedloopedgeslist() {
		return savedloopedgeslist;
	}

	public void setSavedloopedgeslist(List<sxe140630_Edge> savedloopedgeslist) {
		this.savedloopedgeslist = savedloopedgeslist;
	}
	
}
