import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.*;

public class sxe140630_Assignment6 {

	static HashMap<Long,Product> objHashMapwithid = new HashMap<Long,Product>(); // Hash map that stores product with id as key 
	static HashMap<Long,RedBlackBST<Product, Product>> obHashMapwithname = new HashMap<Long, RedBlackBST<Product, Product>>(); // Hash map with key name(each part of the name) 
	static RedBlackBST<Long,Long> objredbalckbstwithid = new RedBlackBST<Long,Long>(); // RB tree to store product references.
	
	public static void main(String[] args) throws IOException {	
		
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		double outputnumber = 0;
		String inputline = null;
		while((inputline = input.readLine())!= null)
		{
			long returnvalue = 0;
			inputline = inputline.replaceAll("\\s+", " "); // removes any extra spaces in between.
			String stroperation = inputline.substring(0,inputline.indexOf(' '));
			String strvalues = inputline.substring(inputline.indexOf(' ')+1);

			switch(stroperation){

			case "Insert":
				strvalues = strvalues.trim();
				returnvalue = parsecallInsert(strvalues);
				outputnumber = outputnumber+returnvalue;
				break;
			case "Find":
				strvalues = strvalues.trim();
				returnvalue = Find(Long.parseLong(strvalues));
				outputnumber = outputnumber+returnvalue/100d;
				break;
			case "Delete":
				strvalues = strvalues.trim();
				returnvalue = Delete(Long.parseLong(strvalues));
				outputnumber = outputnumber+returnvalue;
				break;
			case "FindMinPrice":
				strvalues = strvalues.trim();
				returnvalue = FindMinPrice(Long.parseLong(strvalues));
				outputnumber = outputnumber+returnvalue/100d;
				break;
			case "FindMaxPrice":
				strvalues = strvalues.trim();
				returnvalue = FindMaxPrice(Long.parseLong(strvalues));
				outputnumber = outputnumber+returnvalue/100d;
				break;
			case "FindPriceRange":
				strvalues = strvalues.trim();
				returnvalue = parsecallFindPriceRange(strvalues);
				outputnumber = outputnumber+returnvalue;
				break;
			case "PriceHike":
				strvalues = strvalues.trim();
				returnvalue = parsecallPriceHike(strvalues);
				outputnumber = outputnumber+returnvalue/100d;
				break;

			}
		}
		System.out.printf("%.2f\n", outputnumber);
	}

	private static long parsecallPriceHike(String strvalues) {
		// parsing the string to get the values of long ids.
		String[] splitarray = strvalues.split(" ");
		long id_lower_bound = Long.parseLong(splitarray[0]);
		long id_upper_bound = Long.parseLong(splitarray[1]);
		double percentage_increase_price = Double.parseDouble(splitarray[2]);

		return PriceHike(id_lower_bound,id_upper_bound,percentage_increase_price);
	}

	private static long PriceHike(long id_lower_bound, long id_upper_bound,
			double percentage_increase_price) {
		 Iterable<Long> queue = objredbalckbstwithid.keys(id_lower_bound, id_upper_bound); // qeue has list of ids in the id range.
		 Iterator<Long> iterator = queue.iterator();
		 long returnvalue = 0;
		 while(iterator.hasNext()){
			 // using iterator to iterate through items.
			 long id = iterator.next();
			 Product objProduct = objHashMapwithid.get(id);
			 String name = objProduct.getName();
			 long oldprice = objProduct.getPrice();
			 long newprice = (long)(oldprice+oldprice*(percentage_increase_price/100d));
			 Insert(id, newprice, name);
			 returnvalue = returnvalue+(newprice-oldprice);
		 }
		 return returnvalue;
	}

	private static long parsecallFindPriceRange(String strvalues) {
		// parsing the string to call FindPriceRange
		String[] splitarray = strvalues.split(" ");
		long id = Long.parseLong(splitarray[0]);
		double price_lower_bound = Double.parseDouble(splitarray[1]);
		double price_upper_bound = Double.parseDouble(splitarray[2]);
		long lprice_lower_bound = (long)(price_lower_bound*100); // converting every value in cents and storing it in the form of cents.
		long uprice_lower_bound = (long)(price_upper_bound*100);
		return FindPriceRange(id,lprice_lower_bound,uprice_lower_bound);
	}

	private static long FindPriceRange(long name, long price_lower_bound,
			long price_upper_bound) {
		long numberofitems = 0;
		RedBlackBST<Product, Product> objredblackbst = obHashMapwithname.get(name);
		if(objredblackbst != null){
			Product objProduct_lowerbound = new Product(0,null,price_lower_bound); // creating the new product object with id 0 and lowerpricebound so that comparison functions can recognise the small one
			Product objProduct_upperbound = new Product(Long.MAX_VALUE,null,price_upper_bound); // creating new product with max id and priceupperbound.
			Queue<Product> queue = (Queue<Product>) objredblackbst.keys(objProduct_lowerbound, objProduct_upperbound);
			numberofitems = queue.size();
		}
		return numberofitems;
	}

	private static long FindMaxPrice(long name) {
		long returnvalue = 0;
		if(obHashMapwithname.containsKey(name)){
			// searches for the maximum price in the tree with that name.
			RedBlackBST<Product, Product> objredbalckbst = obHashMapwithname.get(name);
			if(objredbalckbst != null)
			{
				Product objProduct = objredbalckbst.max();
				if(objProduct!=null){
					returnvalue = objProduct.getPrice();
				}
			}	
		}
		return returnvalue;
	}

	private static long FindMinPrice(long name) {
		long returnvalue = 0;
		if(obHashMapwithname.containsKey(name)){
			// searches the RB tree with that name  for the minprice.
			RedBlackBST<Product, Product> objredbalckbst = obHashMapwithname.get(name);
			if(objredbalckbst != null)
			{
				Product objProduct = objredbalckbst.min();
				if(objProduct!=null){
					returnvalue = objProduct.getPrice();
				}
			}	
		}
		return returnvalue;
	}

	private static long Delete(long id) {
		long returnvalue = 0;
		Product objProduct = objHashMapwithid.get(id); // getting the object with the corresponding id.
		if(objProduct != null){
			String[] namearray = objProduct.getName().split(" "); // getting the individual components of name.
			int arrayiterator = 0;
			while(!namearray[arrayiterator].equals("0")) // since name ends in zero
			{
				long key = Long.parseLong(namearray[arrayiterator]);
				RedBlackBST<Product, Product> objredbalckbst = obHashMapwithname.get(key);
				if(objredbalckbst == null){
					System.out.println("Trying to delete in no existent name");
				}
				else{
					objredbalckbst.delete(objProduct); // deleting that corresponding object refrence node from objredbalckbst
				}
				arrayiterator++;
				returnvalue = returnvalue+key;
			}
			objredbalckbstwithid.delete(id); // deleting the same id node from the ids rb tree.
			objHashMapwithid.remove(id); // finally removing the id and product object from hashmap with id.
			
		}
		return returnvalue;
	}

	private static long Find(long id) {
		// find the price of the given id and returns it.
		Product objProduct = objHashMapwithid.get(id);
		long returnvalue = 0;
		if(objProduct != null){
			returnvalue = objProduct.getPrice();
		}
		return returnvalue;
	}

	private static long parsecallInsert(String strvalues) {
		long id = Long.parseLong(strvalues.substring(0,strvalues.indexOf(' ')));
		String remaining_string = strvalues.substring(strvalues.indexOf(' ')+1);
		String price = remaining_string.substring(0,remaining_string.indexOf(' '));
		remaining_string = remaining_string.substring(remaining_string.indexOf(' ')+1);
		long lprice = 0;
		if(price.contains(".")){
			// instead of reading the price directly as double (as giving some minor changes in precession ) parsing it and reading in cents directly
			String[] priceComponent = price.split("\\.");
			lprice = (Long.parseLong(priceComponent[0]))*100; // converting dollars into cents.
			if(priceComponent[1].length()==2){
				lprice = lprice+Long.parseLong(priceComponent[1]); // if length after dot is 2 add directly as it is cents already
			}
			else{
				lprice = lprice+Long.parseLong(priceComponent[1])*10; // if length is 1 we have to multiply the digit with 10 and add it.
			}
			
		}
		else{
			lprice = Long.parseLong(price)*100;
		}
		return Insert(id,lprice,remaining_string);
	}

	private static long Insert(long id, long price, String name) {
		Product objProduct = objHashMapwithid.get(id);
		long isnewinsertion = 1;
		String productname = null;
		if(objProduct != null){
			// there is a product object with the same id so we delete the product with this id and insert a new one.
			isnewinsertion = 0; // since this is already present.
			productname = objProduct.getName();
			String[] namearray = productname.split(" ");
			int arrayiterator = 0;
			while(!namearray[arrayiterator].equals("0"))
			{
				// deleting the product refrence from all the components of name.
				long key = Long.parseLong(namearray[arrayiterator]);
				RedBlackBST<Product, Product> objredbalckbst = obHashMapwithname.get(key);
				if(objredbalckbst == null){
					System.out.println("Trying to delete in no existent name");
				}
				else{
					objredbalckbst.delete(objProduct);
				}
				
				arrayiterator++;
			}
			// deleteing from hashmapwithid.
			objHashMapwithid.remove(id);
			
		}
		
			if(isnewinsertion == 1){
				// new id .. so insert it in reblack tree with id. Incase of already existing node this operation need not be perfromed as we are not deleting the node from idrbtree.
				objredbalckbstwithid.put(id, id);
			}
			if(name.equals("0")){
				name = productname;
			}
			objProduct = new Product(id, name, price);
			objHashMapwithid.put(id, objProduct);
			// inset into hash map with every longint of name.
			String[] namearray = name.split(" ");
			int arrayiterator = 0;
			while(!namearray[arrayiterator].equals("0"))
			{
				long key = Long.parseLong(namearray[arrayiterator]);
				RedBlackBST<Product, Product> objredbalckbst = obHashMapwithname.get(key);
				if(objredbalckbst == null){
					// no tree in the hash map with this key yet. create a new tree and insert the node.
					objredbalckbst = new RedBlackBST<Product, Product>();
					obHashMapwithname.put(key, objredbalckbst);
				}
				objredbalckbst.put(objProduct, objProduct);
				arrayiterator++;
			}

		return isnewinsertion;
	}
	
}
