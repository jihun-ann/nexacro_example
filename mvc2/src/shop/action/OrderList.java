package shop.action;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shop.VO.OrderVO;
import shop.VO.PagingVO;
import shop.VO.MemberVO;
import shop.VO.BasketVO;
import shop.model.ShopDAO;

public class OrderList implements CommandAction{

	public ShopDAO dao = ShopDAO.getInstance();
	
	public String requestPro(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {

		String id = "";
		request.setCharacterEncoding("utf-8");
		if(request.getCookies() != null){
			for(Cookie c : request.getCookies()){
				if(c.getName().contains("username")){
					HashMap<String, Object> map = dao.loginLogCheck(c.getValue());
					
					String idAddr = request.getRemoteAddr();
					String browser = null; 
					String agent = request.getHeader("User-Agent");
					if(agent.indexOf("Trident") > -1)        browser = "MSIE"; 
					else if(agent.indexOf("Edg") > -1)   browser = "Edg"; 
					else if(agent.indexOf("Chrome")> -1)  browser = "Chrome"; 
					else if(agent.indexOf("Opera") > -1)   browser = "Opera"; 
					else if(agent.indexOf("iPhone")> -1 && agent.indexOf("Mobile") > -1) browser = "iPhone"; 
					else if(agent.indexOf("Android")> -1 && agent.indexOf("Mobile")> -1) browser = "Android"; 
					
					if(map.get("USERIP") != null){
						if(!map.get("USERIP").equals(idAddr) || !map.get("BROWSER").equals(browser)){
							request.setAttribute("redirect", "/cookieLogout.do");
							return "/shop/redirect.jsp";
						}else{
							id = c.getValue();
						}
					}else{
						int memseq = dao.selectMemberSeq(c.getValue());
						if(memseq == 0){
							request.setAttribute("redirect", "/cookiedelete.do");
							return "/shop/redirect.jsp";
						}else{
							id = c.getValue();
						}
					}
				}
			}
		}
		
		if(id.equals("")){
			request.setAttribute("redirect", "/login.do");
			return "/shop/redirect.jsp";
		}
		
		String page = "1";
		if(request.getParameter("page") != null){
			page = request.getParameter("page");
		}
		String status = "";
		if(request.getParameter("status") != null){
			status = request.getParameter("status");
		}
		String val = "";
		if(request.getParameter("val") != null){
			val = request.getParameter("val");
		}
		
		System.out.println(val);
		
		int memseq = dao.selectMemberSeq(id);
		
		int pageNum = Integer.parseInt(page);
		
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("member", memseq);
		map.put("status", status);
		map.put("val", val);
		
		int count = dao.selectOrderAllCount(map);
		int endNum = count - ((pageNum-1)*5);
		int startNum = endNum-4;
			startNum = startNum <=0 ? 1 : startNum;
			
		map.put("startNum", startNum);
		map.put("endNum", endNum);
			
		List<OrderVO> orderlst = dao.selectOrderListMember(map);
		
		int totalPage = count / 5;
			totalPage += count%5 == 0? 0 : 1; 
		int startPage = ((pageNum/10)*10)+1;
		int endPage = startPage + 9;
			endPage = endPage <= totalPage? endPage : totalPage;
		
			
		PagingVO paging = new PagingVO();
		paging.setPageNum(pageNum);
		paging.setStartPage(startPage);
		paging.setEndPage(endPage);
		paging.setTotalPage(totalPage);
		paging.setCount(count);
		paging.setVal(val);
		
		request.setAttribute("page","orderList");
		request.setAttribute("username",id);
		request.setAttribute("orderList",orderlst);
		request.setAttribute("status",status);
		request.setAttribute("paging",paging);
		return "/shop/layout.jsp";
	}

}
