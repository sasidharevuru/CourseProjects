
public class Model {
	
	// All the values which we received from the model file we set here.
	private int numberoftstates; 
	private double[] ISP;
	private double[][] transitionprobability;
	private int no_of_outputsymbols;
	private String[] outputobservations;
	private double[][] outputdistributions;
	public int getNumberoftstates() {
		return numberoftstates;
	}
	public void setNumberoftstates(int numberoftstates) {
		this.numberoftstates = numberoftstates;
	}
	public double[] getISP() {
		return ISP;
	}
	public void setISP(double[] iSP) {
		ISP = iSP;
	}
	public double[][] getTransitionprobability() {
		return transitionprobability;
	}
	public void setTransitionprobability(double[][] transitionprobability) {
		this.transitionprobability = transitionprobability;
	}
	public int getNo_of_outputsymbols() {
		return no_of_outputsymbols;
	}
	public void setNo_of_outputsymbols(int no_of_outputsymbols) {
		this.no_of_outputsymbols = no_of_outputsymbols;
	}
	public String[] getOutputobservations() {
		return outputobservations;
	}
	public void setOutputobservations(String[] outputobservations) {
		this.outputobservations = outputobservations;
	}
	public double[][] getOutputdistributions() {
		return outputdistributions;
	}
	public void setOutputdistributions(double[][] outputdistributions) {
		this.outputdistributions = outputdistributions;
	}
	

}
