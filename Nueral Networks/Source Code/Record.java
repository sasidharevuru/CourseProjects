
public class Record {
	
	private int[] attributevalue;
	public Record(int numberofattributes) {
		attributevalue = new int[numberofattributes+1];
	}
	public int[] getAttributevalue() {
		return attributevalue;
	}
	public void setAttributevalue(int[] attributevalue) {
		this.attributevalue = attributevalue;
	}

}
