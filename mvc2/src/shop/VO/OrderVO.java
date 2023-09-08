package shop.VO;

public class OrderVO {
	private int num; 
	private int orderseq; 
	private String date; 
	private String prodname; 
	private int prodcount; 
	private int prodprice; 
	private String ordermessage; 
	private String payment; 
	private String filename;
	private String address;
	private String memberid;
	private String membername;
	private String deliverystatus;
	
	

	@Override
	public String toString() {
		return "OrderVO [num=" + num + ", orderseq=" + orderseq + ", date="
				+ date + ", prodname=" + prodname + ", prodcount=" + prodcount
				+ ", prodprice=" + prodprice + ", ordermessage=" + ordermessage
				+ ", payment=" + payment + ", filename=" + filename
				+ ", address=" + address + ", memberid=" + memberid
				+ ", membername=" + membername + ", deliverystatus="
				+ deliverystatus + "]";
	}


	


	public int getOrderseq() {return orderseq;}
	public String getDate() {return date;}
	public String getProdname() {return prodname;}
	public int getProdcount() {return prodcount;}
	public int getProdprice() {return prodprice;}
	public String getFilename() {return filename;}
	public String getDeliverystatus() {return deliverystatus;}
	public String getPayment() {return payment;}
	public String getOrdermessage() {return ordermessage;}
	public String getAddress() {return address;}
	public int getNum() {return num;}
	public String getMemberid() {return memberid;}
	public String getMembername() {return membername;}
	
	
	public void setMemberid(String memberid) {this.memberid = memberid;}
	public void setMembername(String membername) {this.membername = membername;}
	public void setNum(int num) {this.num = num;}
	public void setAddress(String address) {this.address = address;}
	public void setOrdermessage(String ordermessage) {this.ordermessage = ordermessage;}
	public void setPayment(String payment) {this.payment = payment;}
	public void setDeliverystatus(String deliverystatus) {this.deliverystatus = deliverystatus;}
	public void setOrderseq(int orderseq) {this.orderseq = orderseq;}
	public void setDate(String date) {this.date = date;}
	public void setProdname(String prodname) {this.prodname = prodname;}
	public void setProdcount(int prodcount) {this.prodcount = prodcount;}
	public void setProdprice(int prodprice) {this.prodprice = prodprice;}
	public void setFilename(String filename) {this.filename = filename;} 
	
}
