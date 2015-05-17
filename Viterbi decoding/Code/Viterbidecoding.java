import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;


public class Viterbidecoding {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("enter file names");
		Scanner objScanner = new Scanner(System.in);
		Scanner in;
		Scanner in1 = null;
		//if (args.length > 0) {
			File modelfile = new File(objScanner.next()); // reading the model file from console. 
			File testdatafile = new File(objScanner.next()); // reading the test file from console.
			in = new Scanner(modelfile);
			in1 = new Scanner(testdatafile);
			
			ArrayList<Output> outputsequenceslist = new ArrayList<Output>();
			while(in1.hasNextLine())
			{
				String line = in1.nextLine();
				Output objOutput = new Output(line); // passing the line to create objects for each line separately
				outputsequenceslist.add(objOutput);// storing the created objects in arraylist.
			}
			
			Model objModel = new Model(); // creating a model object
			
			String firstline = in.nextLine();
			int no_of_states = Integer.parseInt(firstline); // reading no_of_states from first line.
			objModel.setNumberoftstates(no_of_states);
			
			// reading the second line ffrom the file and forming an Intial state probabilities.
			String secondline = in.nextLine();
			double [] ISP = new double[no_of_states];
			String[] secondlinevalues = secondline.split(" ");
			for(int i =0;i<ISP.length;i++)
			{
				ISP[i]= Double.parseDouble(secondlinevalues[i]);
			}
			objModel.setISP(ISP);
			
			// reading the third line from the file and forming N*N transition matrix.
			String thirdline = in.nextLine();
			String[] transitionarray = thirdline.split(" ");
			double[][] transitionprobability = new double[no_of_states][no_of_states];
			for(int i=0;i<(no_of_states);i++)
			{
				for(int j=0;j<(no_of_states);j++)
				{
					transitionprobability[i][j]=Double.parseDouble(transitionarray[i*no_of_states+j]);
				}	
			}
			objModel.setTransitionprobability(transitionprobability);
			
			//  reading fourth line from the file and setting number of output symbols.
			String fourthddline = in.nextLine();
			int no_of_outputsymbols = Integer.parseInt(fourthddline);
			objModel.setNo_of_outputsymbols(no_of_outputsymbols);
			
			// reading fifth line from the file and setting the observation sequence.
			String fifthdline = in.nextLine();
			String[] outputobservations = fifthdline.split(" ");
			objModel.setOutputobservations(outputobservations);
			
			// reading the sixth line from the file and setting the outputdistributions.
			String sixthline = in.nextLine();
			String[] temp = sixthline.split(" ");
			double[][] outputdistributions = new double[no_of_states][no_of_outputsymbols];
			for(int i=0;i<no_of_states;i++)
			{
				for(int j=0;j<no_of_outputsymbols;j++)
				{
					outputdistributions[i][j]= Double.parseDouble(temp[i*no_of_outputsymbols+j]);
				}
			}
			objModel.setOutputdistributions(outputdistributions);
			
			// for every sequence we are calculating the viterbiti decoding separately.
			for(int i=0;i<outputsequenceslist.size();i++)
			{
				Output objOutput = outputsequenceslist.get(i);
				viterbitiDecoding(objOutput,objModel);
			}
			
			
			
	}
/**
 * 
 * @param objOutput = The output sequence.
 * @param objModel = The given model read from the input.
 */
	private static void viterbitiDecoding(Output objOutput, Model objModel) 
	{
		int outputsequencelength = objOutput.getObjservationsequence().length; // output sequence length
		int no_of_states = objModel.getNumberoftstates(); // reading no of states.
		double[][] finalprobability = new double[outputsequencelength][objModel.getNumberoftstates()]; // storing the final probability in a 2 dimanesional matrix 
		int[][] Maxtransitionstates = new int[outputsequencelength][objModel.getNumberoftstates()]; // This stores the states from which we came to a particular state.
		
		double[] recentprobability = new double[objModel.getNumberoftstates()];
		
		// caclulating for the first output.
		String currentop = objOutput.getObjservationsequence()[0]; // getting the current state.
		int indexofcurrentop = indexofop(currentop,objModel.getOutputobservations()); // finding the index of the current output.
		
		// since first output states are caluclated a bit differently these first lines are calculated separately
		for(int i=0;i<objModel.getNumberoftstates();i++)
		{
			finalprobability[0][i] = objModel.getISP()[i]*objModel.getOutputdistributions()[i][indexofcurrentop]; // intial state prob * prob of each state generating that particular output
			recentprobability[i] = finalprobability[0][i]; // strong the latest probability states so that we can use them form subsequent calculations in coming for loops.
		}
		
		for(int i=1;i<outputsequencelength;i++)
		{
			currentop = objOutput.getObjservationsequence()[i]; // getting the current output.
			indexofcurrentop = indexofop(currentop,objModel.getOutputobservations()); // getting the index of current output.
			for(int j=0;j<objModel.getNumberoftstates();j++)
			{
				double[] values = new double[no_of_states] ; // probability of coming to this state from diff states.
				for(int k=0;k<no_of_states;k++)
				{
					values[k] = recentprobability[k]*objModel.getTransitionprobability()[k][j]*objModel.getOutputdistributions()[j][indexofcurrentop]; //prev probability * transition prob * prob of generating that prob 
				}
				int index = Maxindex(values); // finding the index where the max value resides
				finalprobability[i][j] = values[index]; // strong the maxvalue in the final probability array.
				Maxtransitionstates[i][j] = index; // storing the state from which we came to this max state.
			}
			for(int j=0;j<no_of_states;j++)
			{
				recentprobability[j] = finalprobability[i][j];
			}
		}
		
		int index = Maxindex(recentprobability);//finding the index where the max value resides
		
		// stack is used to output the states in the reverse order. we can push the states from end to first and output the state transitions from first.
		Stack<Integer> objStack = new Stack<Integer>();
		objStack.push(index);
		
		for(int i=outputsequencelength-1;i>0;i--)
		{
			int valueatindex = Maxtransitionstates[i][index]; // geeting the value at the index so that can be used to get the next state.
			objStack.push(valueatindex);
			index = valueatindex;
		}
		
		// popping the ements firn the stack.
		while(!objStack.isEmpty())
		{
			System.out.print("S"+objStack.pop()+" ");
		}
		System.out.println();
	}

	/**
	 * finds the index of the max double array.
	 *   
	 * @param values : receives a double array
	 * @return : returns the index of the max double element
	 */
	private static int Maxindex(double[] values) {
		double max= Double.MIN_VALUE;
		int index = -1;
		for(int i=0;i<values.length;i++)
		{
			if(values[i]>max)
			{
				max = values[i];
				index = i;
			}
		}
		return index;
	}
/**
 * This function finds the index of the given string in currenttop in the outputobservations array
 * @param currentop : the current top elemnt(string)
 * @param outputobservations : The array from whihc we need to find the index of the currenttop.
 * @return : index of the top element
 */
	private static int indexofop(String currentop, String[] outputobservations) {
		// TODO Auto-generated method stub
		for(int i=0;i<outputobservations.length;i++)
		{
			if(currentop.equals(outputobservations[i]))
			{
				return i;
			}
		}
		return -1;
	}
	
	

}
