package shop.VO;

public class ProdOptionVO {
	private int optionseq;
	private int productseq;
	private String name;
	private int productcount;
	private int productprice;
	
	
	@Override
	public String toString() {
		return "ProdOptionVO [optionseq=" + optionseq + ", productseq="
				+ productseq + ", name=" + name + ", productcount="
				+ productcount + ", productprice=" + productprice + "]";
	}


	public int getOptionseq() {return optionseq;}
	public int getProductseq() {return productseq;}
	public String getName() {return name;}
	public int getProductcount() {return productcount;}
	public int getProductprice() {return productprice;}


	public void setOptionseq(int optionseq) {this.optionseq = optionseq;}
	public void setProductseq(int productseq) {this.productseq = productseq;}
	public void setName(String name) {this.name = name;}
	public void setProductcount(int productcount) {this.productcount = productcount;}
	public void setProductprice(int productprice) {this.productprice = productprice;}
	
	
}
