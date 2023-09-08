package shop.VO;

public class ProductVO {
	private int num;
	private int productseq;
	private String name;
	private int opcount;
	private int ordercount;
	private String date;
	private String filename;
	private int productprice;
	


	


	@Override
	public String toString() {
		return "ProductVO [num=" + num + ", productseq=" + productseq
				+ ", name=" + name + ", opcount=" + opcount + ", ordercount="
				+ ordercount + ", date=" + date + ", filename=" + filename
				+ ", productprice=" + productprice + "]";
	}



	public int getNum() {return num;}
	public int getProductseq() {return productseq;}
	public String getName() {return name;}
	public int getOpcount() {return opcount;}
	public int getOrdercount() {return ordercount;}
	public String getDate() {return date;}
	public String getFilename() {return filename;}
	public int getProductprice() {return productprice;}


	public void setNum(int num) {this.num = num;}
	public void setProductseq(int productseq) {this.productseq = productseq;}
	public void setName(String name) {this.name = name;}
	public void setOpcount(int opcount) {this.opcount = opcount;}
	public void setOrdercount(int ordercount) {this.ordercount = ordercount;}
	public void setDate(String date) {this.date = date;}
	public void setFilename(String filename) {this.filename = filename;}
	public void setProductprice(int productprice) {this.productprice = productprice;}
	
}
