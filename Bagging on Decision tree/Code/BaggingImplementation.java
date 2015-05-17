/**
 * Author : Sasidhar Evuru
 * NetId : sxe140630
 * 
 * 
 * This is the main file and execution starts from here
 * */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class BaggingImplementation {

	public static int noofattributes = 0; // total number of attributes.
	public static ArrayList<ArrayList<Integer>> possibleattributeValues = null; // Store the possible values attributes as an array list.
	public static int no_of_zero_attributes = 0; // no of records that belongs to class 0
	public static int no_of_one_attributes = 0; // no of records that belong to class 1
	public static ArrayList<String> attributeslist = null;
	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Entry format :: Trainfile	Testfile	NoOfBags");
		Scanner objScanner = new Scanner(System.in);
		Scanner in;
		Scanner in1 = null;
		
			File traindatafile = new File(objScanner.next()); // reading the train file from console. 
			File testdatafile = new File(objScanner.next()); // reading the test file from console.
			int k = objScanner.nextInt(); // no of Bags to be formed.
			in = new Scanner(traindatafile);
			in1 = new Scanner(testdatafile);
	
		
		String firstline = in.nextLine();
		String[] splitline = firstline.split(" ");
		
		attributeslist = new ArrayList<String>();
		ArrayList<Integer> noOfValues = new ArrayList<Integer>();
		ArrayList<Integer> unusedattributesnumbers = new ArrayList<Integer>();
		
		for(int i = 0; i< splitline.length; )
		{
			attributeslist.add(splitline[i]); // adding the string to attributes list.
			noOfValues.add(Integer.parseInt(splitline[i+1])); // no of possible values for each attribute.
			unusedattributesnumbers.add(i/2);
			i=i+2;
		}
		
		noofattributes = attributeslist.size();
		// Reading the records in the dat file into an array list.
		
		ArrayList<Record> trainingrecords = new ArrayList<Record>();
		ArrayList<Record> testingrecords = new ArrayList<Record>();
		possibleattributeValues = new ArrayList<ArrayList<Integer>>();
		
		for(int i=0; i< noofattributes;i++)
		{
			ArrayList<Integer> objArrayList = new ArrayList<Integer>(); // creating an array list for each attribute to store the attribute values.
			possibleattributeValues.add(objArrayList);
		}
		
		
		while(in.hasNext())
		{
			Record objRecord = new Record(); // Creating a new record object for every new record line.
			ArrayList<Integer> objArrayList = new ArrayList<Integer>();
			for(int i = 0; i< attributeslist.size() ;i++)
			{
				// this loop reads the attribute values from the records and collects all different values for the attributes.
				int value = in.nextInt();
				if(!possibleattributeValues.get(i).contains(value))
				{
					possibleattributeValues.get(i).add(value);
				}
				objArrayList.add(value);
			}
			int class_value = in.nextInt();// the last attribute of every record contains class variable. 
			objArrayList.add(class_value); 
			if(class_value == 0)
			{
				no_of_zero_attributes++;
			}
			else
			{
				no_of_one_attributes++;
			}
			objRecord.setAttributevalues(objArrayList);
			trainingrecords.add(objRecord); // creating records and saving them in an arraylist
		}
		
		in1.nextLine();
		while(in1.hasNext())
		{
			// the same thing is done for the test data as done with train data.
			Record objRecord = new Record();
			ArrayList<Integer> objArrayList = new ArrayList<Integer>();
			for(int i = 0; i< attributeslist.size() ;i++)
			{
				int value = in1.nextInt();
				objArrayList.add(value);
			}
			int class_value = in1.nextInt();
			objArrayList.add(class_value);
			objRecord.setAttributevalues(objArrayList);
			testingrecords.add(objRecord);
		}
		
		Node root1 = LearnTree(trainingrecords,unusedattributesnumbers);
		double accuracytest = calculateaccuracy(root1,testingrecords);
		System.out.println("Accuracy on testing set without Bagging ("+testingrecords.size()+" instances): "+accuracytest);
		
		// Making K bags for creating the number of bags.
		ArrayList<ArrayList<Record>> inputforkclassifiers = new ArrayList<ArrayList<Record>>();
		int noofrecords = trainingrecords.size();
		for(int i=0;i<k;i++)
		{
			ArrayList<Record> currentrecord = new ArrayList<Record>();
			
			for(int j=0;j<noofrecords;j++)
			{
				int randomnumber = (int)(Math.random()*(noofrecords-1)); // generates a random number between 0 noofrecords-1
				Record objRecord = trainingrecords.get(randomnumber); // getting the random record
				currentrecord.add(objRecord); // adding it to the records.
			}
			
			inputforkclassifiers.add(currentrecord);
		}
	
		Node[] listofroots = new Node[k];
		for(int i=0;i<k;i++)
		{
			Node root = LearnTree(inputforkclassifiers.get(i),unusedattributesnumbers); // creating K different tress for each bag.
			listofroots[i] = root;
		}
		
		double acuuracywithbagging = calculateacuuracywithktrees(listofroots,testingrecords);
		System.out.println("Accuracy on testing set with Bagging ("+testingrecords.size()+" instances): "+acuuracywithbagging);
			
		
	}
	
	/**
	 * This function calculates the accuracy of bagging.
	 * 
	 * @param rootnodes : Array of root nodes of trained tress
	 * @param testingrecords : testing records that are to be tested on the trees in the array.
	 * @return : returns accurayc percentage.
	 */
	private static double calculateacuuracywithktrees(Node[] rootnodes, ArrayList<Record> testingrecords)
	{
		int totalNoOfRecords = testingrecords.size();
		int noOfCorrectRecors = 0;
		for(int i = 0; i< totalNoOfRecords ; i++)
		{
			Record objRecord = testingrecords.get(i);
			ArrayList<Integer> record = objRecord.getAttributevalues();
			
			int[] resultoneverytree = new int[rootnodes.length]; // stores the result of every tree for each train record.
			for(int j=0;j<rootnodes.length;j++)
			{
				int retrievedClassNo = retrieveClass(rootnodes[j],objRecord); // getting the result of each tree.
				resultoneverytree[j] = retrievedClassNo; // storing the result for every tree.
			}
			int noOfZeroes = 0;
			int noOfOnes = 0;
			for(int j=0;j<resultoneverytree.length;j++)
			{
				// computing the number of tress with one input and the no of trees with Zero outputs.
				if(resultoneverytree[j]==0)
				{
					noOfZeroes++;
				}
				else
					noOfOnes++;
			}
			
			int maxcountclass = noOfZeroes>noOfOnes ? 0 : 1; // finding whether ones are maximum or zeroes are maximum.
			int actualClassNo = record.get(record.size()-1); // getting the expected class of the record.
			if(maxcountclass==actualClassNo)
			{
				noOfCorrectRecors++;
			}
		}
		return ((double)(noOfCorrectRecors*100)/totalNoOfRecords); // returning the accuracy percentage.
		
	}
	
	private static double calculateaccuracy(Node root,
			ArrayList<Record> testingrecords) {
		
		int totalNoOfRecords = testingrecords.size();
		int noOfCorrectRecors = 0;
		for(int i = 0; i< totalNoOfRecords ; i++)
		{
			Record objRecord = testingrecords.get(i);
			ArrayList<Integer> record = objRecord.getAttributevalues();
			int retrievedClassNo = retrieveClass(root,objRecord);
			int actualClassNo = record.get(record.size()-1);
			if(retrievedClassNo==actualClassNo)
			{
				noOfCorrectRecors++;
			}
			
			
		}
		return ((double)(noOfCorrectRecors*100)/totalNoOfRecords);
		
	}
	private static int retrieveClass(Node root,Record objRecord) {
		// TODO Auto-generated method stub
		Node objNode = root;
		int classnumber = -1;
		if(root.getChildren().size()!=0)
		{
			int attributeno = root.getClassifyingattrnumber();
			int valueofattInRecord = objRecord.getAttributevalues().get(attributeno);
			int indexofattr = root.getAttributevalue().indexOf(valueofattInRecord);
			if(indexofattr == -1)
			{
				// we encountered unknown data point in the set so returning the maximum encountered class.
				classnumber = no_of_one_attributes > no_of_zero_attributes ? no_of_one_attributes : no_of_zero_attributes;
			}
			else
			{
				classnumber = retrieveClass(root.getChildren().get(indexofattr), objRecord);	
			}
			
		}
		else
		{
			classnumber = root.getClassnumber();
		}
		return classnumber;
	}
	private static void printTree(Node root,int no_of_spaces) {
		
		if(root.getChildren().size()!=0)
		{
			System.out.println();
			int attrno = root.getClassifyingattrnumber();
			String attrname = sxe140630_Assignment5.attributeslist.get(attrno);
			for(int i = 0; i< root.getAttributevalue().size(); i++)
			{
				for(int j = 0; j< no_of_spaces;j++)
				{
					System.out.print("| ");
				}
				System.out.print(attrname+" "+"="+" ");
				System.out.print(root.getAttributevalue().get(i) +" :");
				int temp = no_of_spaces+1;
				printTree(root.getChildren().get(i), temp);
				
				
			}
		}
		else
		{
			System.out.print(" "+root.getClassnumber());
			System.out.println();
			return;
		}
		
		
		
	}
	
	/**
	 * This function creates tree nodes and the sets the node properties accordingly.
	 * @param trainingrecords : the list of records that are passed down. The last element of Record itself will hold the class value
	 * @param unusedattributesnumbers : the list of attributes that are yet to be used
	 * @return node
	 */
	private static Node LearnTree(ArrayList<Record> trainingrecords, ArrayList<Integer> unusedattributesnumbers) {
		
		ArrayList<Record> recordlist = trainingrecords;
		
		Node root = new Node();
		root.setRecordlist(recordlist);
		
		
		if(trainingrecords.isEmpty())
		{
			// We make this node a leaf node.
			root.setClassnumber(no_of_one_attributes>no_of_zero_attributes?1:0); // setting the class value as the most frequent class 
			return root;
			
		}
		
		// Check if any attributes left for division in the unusedattributesnumbers
		if(unusedattributesnumbers.isEmpty())
		{
			// find the most frequent in the local records list and assign the most frequent one. If the local records are equally distributed take the most frequent calss in the entire records.
			int no_of_zero_records = 0; // local zero class records
			int no_of_one_records = 0; // local one class records
			for(int i = 0 ; i< recordlist.size();i++)
			{
				ArrayList<Integer> r = recordlist.get(i).getAttributevalues();
				int size = r.size();
				if(r.get(size-1)==0)
				{
					no_of_zero_records++; 
				}
				else
				{
					no_of_one_records++;
				}
			}
			
			if(no_of_one_records > no_of_zero_records)
			{
				root.setClassnumber(1);
			}
			else if(no_of_zero_records > no_of_one_records)
			{
				root.setClassnumber(0);
			}
			else
			{
				// records equally distributed.finding the most frequent in the whole records.
				
				if(sxe140630_Assignment5.no_of_zero_attributes > sxe140630_Assignment5.no_of_one_attributes)
				{
					root.setClassnumber(0);
				}
				else 
				{
					root.setClassnumber(1);
				}
				
			}
			
			return root;
		
		}
		
		
		// checking if all the records belongs to same class.
		
		boolean allRecordsSameClass = allToSameClass(recordlist);
		if(allRecordsSameClass)
		{
			// if all records belong to same class we make this node as leaf and set the class value and return.
			ArrayList<Integer> firstrecordAttr = recordlist.get(0).getAttributevalues();
			int classnumber = firstrecordAttr.get(firstrecordAttr.size()-1);
			root.setClassnumber(classnumber);
			return root;
		}
		
		if(recordlist.size()>1)
		{
			boolean allAttributeValueSame = allAttributesSame(recordlist);
			if(allAttributeValueSame)
			{
				// if all records have same attribute value sets no need to classify it further, mark it as leaf node and assign the class value.
				int no_of_zero_records = 0;
				int no_of_one_records = 0;
				for(int i = 0 ; i< recordlist.size();i++)
				{
					// finding the most frequent class in the local recordslist. 
					ArrayList<Integer> r = recordlist.get(i).getAttributevalues();
					int size = r.size();
					if(r.get(size-1)==0)
					{
						no_of_zero_records++;
					}
					else
					{
						no_of_one_records++;
					}
				}
				
				if(no_of_one_records > no_of_zero_records)
				{
					root.setClassnumber(1);
				}
				else if(no_of_zero_records > no_of_one_records)
				{
					root.setClassnumber(0);
				}
				else
				{
					// records equally distributed.finding the most frequent in the whole records.
					
					if(sxe140630_Assignment5.no_of_zero_attributes > sxe140630_Assignment5.no_of_one_attributes)
					{
						root.setClassnumber(0);
					}
					else 
					{
						root.setClassnumber(1);
					}
					
				}
				
				return root;
			}
		}
		
	
		
		
		int maxIgindex = Calculations.MaxInformationgain(root, unusedattributesnumbers);
		int maxIgattributenumber = unusedattributesnumbers.get(maxIgindex); // getting the attribute number.
		
		root.setClassifyingattrnumber(maxIgattributenumber);
		
		// classifying the records in the root depending on the value of maxIgattribute.
	
		ArrayList<Integer> possible_values_of_attribute = possibleattributeValues.get(maxIgattributenumber); // possible values of the attribute on which the classification is done.
		ArrayList<ArrayList<Record>> recordswithattribute = new ArrayList<ArrayList<Record>>(); // This list holds the records array list for each possible attribute value.
		HashMap<Integer, Integer> objHashMap = new HashMap<Integer, Integer>();
		for(int p = 0;p<possible_values_of_attribute.size();p++)
		{
			objHashMap.put(possible_values_of_attribute.get(p),p);
			ArrayList<Record> recordwithvalue = new ArrayList<Record>();
			recordswithattribute.add(recordwithvalue);
		}
		
		
		for(int j=0;j<recordlist.size();j++)
		{
			Record objRecord = recordlist.get(j);
			int number = objRecord.getAttributevalues().get(maxIgattributenumber);
			int positiontoadd = objHashMap.get(number);
			recordswithattribute.get(positiontoadd).add(objRecord);
		}
		
		
		
		ArrayList<Integer> passingattributesnumbers = new ArrayList<Integer>();
		for(int i=0; i<unusedattributesnumbers.size();i++)
		{
			passingattributesnumbers.add(unusedattributesnumbers.get(i));
		}
		
		// removing the attribute from the available attributes lists
		passingattributesnumbers.remove(maxIgindex);
		
		for(int a = 0; a < possible_values_of_attribute.size(); a++)
		{
				Node objNode = null;
				objNode = LearnTree(recordswithattribute.get(a), passingattributesnumbers); // recursively calling the node with the remaining records and remaining attributes. 
				objNode.setParent(root);
				root.getChildren().add(objNode);
				root.getAttributevalue().add(possible_values_of_attribute.get(a));
		}
				
		return root;
		
	}
	/**
	 * This function checks if all records has same set of attributes.
	 * @param recordlist : passed recordlist
	 * @return : true if all records have same set of attribute values else return false.
	 */
	private static boolean allAttributesSame(ArrayList<Record> recordlist) {
		
		Record firstrecord = recordlist.get(0);
		ArrayList<Integer> objArrayList = firstrecord.getAttributevalues();
		boolean result = true;
		for(int i = 1;i< recordlist.size();i++)
		{
			Record objRecord =  recordlist.get(i);
			ArrayList<Integer> objArrayList1 = objRecord.getAttributevalues(); // collecting the first record to compare it with others.
			for(int j=0; j< objArrayList1.size();j++)
			{
				// comparing every record with first record.
				if(objArrayList.get(j)!=objArrayList1.get(j))
				{
					result = false;
					break;
				}
			}
			
			if(!result)
			{
				break;
			}	
		}
		
		return result;

	}
	
	/**
	 * Checks if all records belongs to same class
	 * @param recordlist : passed recordlist
	 * @return : true if all records belong to same class else return flase
	 */
	private static boolean allToSameClass(ArrayList<Record> recordlist) {
		// this method checks if all the records belongs to the same class.
		boolean result = true;
		int noOfRecords = recordlist.size();
		int firstrecordclass = 0;
		int recordlength = 0;
		if(noOfRecords > 0)
		{
			recordlength = recordlist.get(0).getAttributevalues().size();
			firstrecordclass = recordlist.get(0).getAttributevalues().get(recordlength-1);
		}
		for(int i =0 ; i < noOfRecords ; i++)
		{
			int ithclass = recordlist.get(i).getAttributevalues().get(recordlength-1);
			if(ithclass != firstrecordclass)
			{
				// found a record that belongs to diff class so breaking the loop.
				result = false;
				break;
			}
		}
		
		return result;
	}
	
}
