
public class Product implements Comparable<Product>{

	private long id ;
	private String name;
	private long price; // storing the price in cents.
	
	public Product(long id,String name,long price) {
		this.id = id;
		this.name = name;
		this.price = price;
			}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	
	public int compareTo(Product p)
	{
		// Overriding the compareto method of comparable.
		// This function first compares compares price and if price is same then comparison is done for id. If id is also is same then comparison is being done for same product object
		// the rb tree is populated on taking price as a comparison factor and if price is same then id is used for the comparison factor.
		long diffinprice = this.price-p.getPrice();
		if(diffinprice > 0){
			return 1;
		}
		else if (diffinprice < 0){
			return -1;
		}
		else{
			// price diff is same so arrange on id.
			long diffinid = this.id-p.getId();
			if(diffinid > 0){
				return 1;
			}
			else if (diffinid < 0){
				return -1;
			}
			else{
				return 0;
			}
			
		}		
	}
	
}