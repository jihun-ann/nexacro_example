package shop.VO;

public class BasketVO {
	private int basketseq;
	private int productseq;
	private String productname;
	private int optionseq;
	private String optionname;
	private int productprice;
	private int ordercount;
	private String filename;
	
	
	
	@Override
	public String toString() {
		return "BasketVO [basketseq=" + basketseq + ", productseq="
				+ productseq + ", productname=" + productname + ", optionseq="
				+ optionseq + ", optionname=" + optionname + ", productprice="
				+ productprice + ", ordercount=" + ordercount + ", filename="
				+ filename + "]";
	}



	public int getBasketseq() {return basketseq;}
	public int getProductseq() {return productseq;}
	public String getProductname() {return productname;}
	public int getOptionseq() {return optionseq;}
	public String getOptionname() {return optionname;}
	public int getProductprice() {return productprice;}
	public int getOrdercount() {return ordercount;}
	public String getFilename() {return filename;}
	
	
	public void setBasketseq(int basketseq) {this.basketseq = basketseq;}
	public void setProductseq(int productseq) {this.productseq = productseq;}
	public void setProductname(String productname) {this.productname = productname;}
	public void setOptionseq(int optionseq) {this.optionseq = optionseq;}
	public void setOptionname(String optionname) {this.optionname = optionname;}
	public void setProductprice(int productprice) {this.productprice = productprice;}
	public void setOrdercount(int ordercount) {this.ordercount = ordercount;}
	public void setFilename(String filename) {this.filename = filename;}
	
	
}