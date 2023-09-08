package shop.VO;

public class OrderProductVO {
	private int orderseq;
	private int productseq;
	private String productname;
	private int optionseq;
	private String optionname;
	private int ordercount;
	private int price;
	private String filename;
	
	
	


	@Override
	public String toString() {
		return "OrderProductVO [orderseq=" + orderseq + ", productseq="
				+ productseq + ", productname=" + productname + ", optionseq="
				+ optionseq + ", optionname=" + optionname + ", ordercount="
				+ ordercount + ", price=" + price + ", filename=" + filename + "]";
	}


	public int getOrderseq() {return orderseq;}
	public int getProductseq() {return productseq;}
	public String getProductname() {return productname;}
	public int getOptionseq() {return optionseq;}
	public String getOptionname() {return optionname;}
	public int getOrdercount() {return ordercount;}
	public int getPrice() {return price;}
	public String getFilename() {return filename;}


	public void setFilename(String filename) {this.filename = filename;}
	public void setOrderseq(int orderseq) {this.orderseq = orderseq;}
	public void setProductseq(int productseq) {this.productseq = productseq;}
	public void setProductname(String productname) {this.productname = productname;}
	public void setOptionseq(int optionseq) {this.optionseq = optionseq;}
	public void setOptionname(String optionname) {this.optionname = optionname;}
	public void setOrdercount(int ordercount) {this.ordercount = ordercount;}
	public void setPrice(int price) {this.price = price;}
	
	
}
