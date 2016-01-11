
public class Output {
	
	Output(String observation)
	{
		setObjservationsequence(observation.split(" ")); // this sets the observation sequence.
	}
	private String[] objservationsequence = null;
	public String[] getObjservationsequence() {
		return objservationsequence;
	}
	public void setObjservationsequence(String[] objservationsequence) {
		this.objservationsequence = objservationsequence;
	}
	
}
