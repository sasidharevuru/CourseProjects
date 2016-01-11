/**
 * Author : Sasidhar Evuru
 * NetId : sxe140630
 * 
 * 
 * This class is only for evaluating the entropy and maximum info gain
 * */
import java.util.ArrayList;
import java.util.HashMap;


public class Calculations {

	/**
	 * 
	 * @param recordslist : the recordslist whose entropy has to be calculated
	 * @return : etropy of the records.
	 * 
	 * This functions cacluates the entropy of the given records
	 */
	static double calculateentropy(ArrayList<Record> recordslist)
	{
		int noOfElementsClass0 = 0; // no of calss0 records
		int noOfElementsCalss1 = 0; // no of class1 records
		
		double entropy = 0;
		
		int noOfAtrributes = sxe140630_Assignment5.noofattributes;
		for(int i=0; i<recordslist.size() ;i++)
		{
			if(recordslist.get(i).getAttributevalues().get(noOfAtrributes)==0)
			{
				noOfElementsClass0++;
			}
			else
			{
				noOfElementsCalss1++;
			}
		}
		
		double probability0 = noOfElementsClass0 / (double)recordslist.size();
		double probability1 = noOfElementsCalss1 /(double)recordslist.size();
		
		
		
		if(probability0 > 0) {
			entropy += -probability0 * (Math.log(probability0) / Math.log(2));
		}
		if(probability1 > 0) {
			entropy += -probability1 * (Math.log(probability1) / Math.log(2));
		}
		
		// here we are not cosidering cases where probablity of one class is Zero, thus preveting it to add to entropy.
		return entropy;	
		
	}
	/**
	 * 
	 * @param parent = the parent node from which the classification is taking place
	 * @param remainingattributes = list of remaining attributes along that line.
	 * @return attribute number which gives maximum entropy.
	 * 
	 * This function checks which attribute gives the maximum info gain
	 */
	static int MaxInformationgain(Node parent,ArrayList<Integer> remainingattributes)
	{
		ArrayList<Record> recordsinparent = parent.getRecordlist();
		double parentEntropy = Calculations.calculateentropy(recordsinparent);
		double maxIg = 0;
		int maxIgattributeindex = 0;
		
		for(int i=0;i<remainingattributes.size();i++)
		{
			int attributenumber = remainingattributes.get(i);
			ArrayList<Integer> possible_values_of_attribute = sxe140630_Assignment5.possibleattributeValues.get(attributenumber);
			ArrayList<ArrayList<Record>> recordswithattribute = new ArrayList<ArrayList<Record>>();
			HashMap<Integer, Integer> objHashMap = new HashMap<Integer, Integer>();
			for(int p = 0;p<possible_values_of_attribute.size();p++)
			{
				objHashMap.put(possible_values_of_attribute.get(p),p);
				ArrayList<Record> recordwithvalue = new ArrayList<Record>();
				recordswithattribute.add(recordwithvalue);
			}
			
			
			for(int j=0;j<recordsinparent.size();j++)
			{
				Record objRecord = recordsinparent.get(j);
				int number = objRecord.getAttributevalues().get(attributenumber);
				int positiontoadd = objHashMap.get(number);
				recordswithattribute.get(positiontoadd).add(objRecord);
			}
			
			double childrenentropy = 0;
			
			for(int k = 0; k<possible_values_of_attribute.size() ; k++)
			{
				// loop to calcuate children entropy
				childrenentropy = childrenentropy+((recordswithattribute.get(k).size()/(double)recordsinparent.size()))*calculateentropy(recordswithattribute.get(k));
			}
			
			
			double Ig = parentEntropy-childrenentropy;
			if(Ig>maxIg)
			{
				// replacing the IG attribute.
				maxIg = Ig;
				maxIgattributeindex = i;
			}
		}
		return maxIgattributeindex;
		
	}
	
}
