import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class sxe140630Assignment3 {

	
	public static void main(String[] args) {
		
		System.out.println("enter fields in the order each separated by a space 1.training file 2.test file 3.learning rate 4.the number of iterations");
		
		Scanner objScanner = new Scanner(System.in);
		
		File traindatafile = new File(objScanner.next()); // reading the train file from console. 
		File testdatafile = new File(objScanner.next()); // reading the test file from console.
		
		double learnigrate = Double.parseDouble(objScanner.next()); //reading the learningrate file from console
		int noOfInterations = Integer.parseInt(objScanner.next()); //reading the noOfInterations file from console
		
		
		Scanner intrain = null;
		Scanner intest = null;
		try {
			intrain = new Scanner(traindatafile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			intest = new Scanner(testdatafile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String trainfile_firstline = intrain.nextLine();  // reading the first line of train file
		String[] attributeslist = trainfile_firstline.split("\t"); // making an array of attributeslist
		
		
		ArrayList<Record> trainrecordlist = new ArrayList<Record>(); // this holds the list of train records.
		
		while(intrain.hasNext())
		{
			Record objRecord = new Record(attributeslist.length); // Creating a new record object for every new record line.
			for(int i=0;i<=attributeslist.length;i++)
			{
				objRecord.getAttributevalue()[i] = intrain.nextInt();
			}
			trainrecordlist.add(objRecord);
		}
		
		String testfile_firstline = intest.nextLine();
		
		ArrayList<Record> testrecordlist = new ArrayList<Record>(); // saving the test records in the arraylist.
		while(intest.hasNext())
		{
			Record objRecord = new Record(attributeslist.length); // Creating a new record object for every new record line.
			for(int i=0;i<=attributeslist.length;i++)
			{
				objRecord.getAttributevalue()[i] = intest.nextInt();
			}
			testrecordlist.add(objRecord);
		}
		
		double[] returnedweigths = Calculateweigths(trainrecordlist,learnigrate,noOfInterations);
		
		double trainefficieny = CalculateAccuracy(returnedweigths,trainrecordlist);
		double testefficieny = CalculateAccuracy(returnedweigths,testrecordlist);
		
		
		System.out.println("Accuracy on training set ("+noOfInterations+" instances): "+trainefficieny);
		System.out.println("Accuracy on training set ("+noOfInterations+" instances): "+testefficieny);
		
		
}
	
/**
 * 	
 * @param returnedweigths = passing the returned weights array after all the updations
 * @param recordlist = passing the records list to which we are calculating the accuracy.
 * @return accuracy percentage.
 */
private static double CalculateAccuracy(double[] returnedweigths,ArrayList<Record> recordlist) 
{
	int noOfcorrectrecords = 0;
	int noofWrongRecords = 0;
	double w0= returnedweigths[0];
	double[] weights = new double[returnedweigths.length-1];
	for(int i=0;i<returnedweigths.length-1;i++)
	{
		weights[i] = returnedweigths[i+1];
	}
	for(int i=0;i<recordlist.size();i++)
	{
		Record currentrecord = recordlist.get(i);
		double invalue = 0; 
		for(int j=0;j<currentrecord.getAttributevalue().length-1;j++)
		{
			invalue = invalue+(weights[j]*currentrecord.getAttributevalue()[j]);
		}
		invalue = invalue-w0;
		double calculatedop = 1/(1+Math.exp(-invalue));
		
		if(calculatedop>=0.5) // comparing against threshold.
		{
			if(currentrecord.getAttributevalue()[currentrecord.getAttributevalue().length-1]==1)
			{
				noOfcorrectrecords++;
			}
			else
			{
				noofWrongRecords++;
			}
		}
		else
		{
			if(currentrecord.getAttributevalue()[currentrecord.getAttributevalue().length-1]==0)
			{
				noOfcorrectrecords++;
			}
			else
			{
				noofWrongRecords++;
			}
			
		}	
	}
	
	return ((double)(noOfcorrectrecords*100)/(recordlist.size()));
	
}



/**
 * 
 * @param recordlist - list of training records.
 * @param learnigrate - learning rate got form the input
 * @param noOfIterations - no of iterations
 * @return returns the calculated weights array
 */
	
	private static double[] Calculateweigths(ArrayList<Record> recordlist,
			double learnigrate, int noOfIterations) {
		
		double w0 = 0;
		int noOfattributes = recordlist.get(0).getAttributevalue().length-1;
		double[] weights = new double[noOfattributes];
		double[] returningweights = new double[noOfattributes+1];
		int noOfexecutedrecords = 0;
		for(int countofiterations = 0; countofiterations< noOfIterations;countofiterations++)
		{
			if(noOfexecutedrecords<noOfIterations)
			{
				
				for(int iteratedrecords = 0;iteratedrecords < recordlist.size();iteratedrecords++)
				{
					if(noOfexecutedrecords>=noOfIterations)
					{
						break;
					}
					Record currentrecord = recordlist.get(iteratedrecords);
					int expectedop = currentrecord.getAttributevalue()[noOfattributes];
					
					// calculating the output 
					
					double invalue = 0;
					for(int i = 0; i<noOfattributes;i++)
					{
						invalue = invalue+(weights[i]*currentrecord.getAttributevalue()[i]);
					}
					invalue = invalue-w0;
					
					double calculatedop = 1/(1+Math.exp(-invalue));
					
					double error = expectedop-calculatedop;
					
					w0 = w0+(learnigrate*error*calculatedop*(1-calculatedop)*(-1)); // updating the weight
					for(int i=0;i<noOfattributes;i++)
					{
						int attributevalue = currentrecord.getAttributevalue()[i];
						weights[i] = weights[i]+(learnigrate*error*calculatedop*(1-calculatedop)*attributevalue); // updating the weight
					}
					noOfexecutedrecords++;
				}	
			}
			else
			{
				break;
			}
			
			

		}
		
		
		// forming the weights array to return.
		returningweights[0] = w0;
		for(int i=0;i<noOfattributes;i++)
		{
			returningweights[i+1] = weights[i];
		}
		return returningweights;
	}
}
