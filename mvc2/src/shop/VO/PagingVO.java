package shop.VO;

public class PagingVO {
	private String val;
	private int pageNum;
	private int startPage;
	private int endPage;
	private int totalPage;
	private int count;
	
	
	
	@Override
	public String toString() {
		return "PagingVO [val=" + val + ", pageNum=" + pageNum + ", startPage="
				+ startPage + ", endPage=" + endPage + ", totalPage="
				+ totalPage + ", count=" + count + "]";
	}
	
	public String getVal() {return val;}
	public int getPageNum() {return pageNum;}
	public int getStartPage() {return startPage;}	
	public int getEndPage() {return endPage;}
	public int getTotalPage() {return totalPage;}
	public int getCount() {return count;}
	
	public void setVal(String val) {this.val = val;}
	public void setPageNum(int pageNum) {this.pageNum = pageNum;}
	public void setStartPage(int startPage) {this.startPage = startPage;}
	public void setEndPage(int endPage) {this.endPage = endPage;}
	public void setTotalPage(int totalPage) {this.totalPage = totalPage;}
	public void setCount(int count) {this.count = count;}
}
