package shop.action;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shop.VO.PagingVO;
import shop.VO.ProductVO;
import shop.model.ShopDAO;

public class Home implements CommandAction{
	public ShopDAO dao = ShopDAO.getInstance();

	public String requestPro(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {

		request.setCharacterEncoding("utf-8");
		String page = String.valueOf(request.getParameter("page"));
		if(page.equals("null") || page == null){
			page = "1";
		}
		String val = String.valueOf(request.getParameter("val"));
		if(val.equals("null") || val == null){
			val = "";
		}
		String regExp = "^[0-9]+$";
		if(!page.matches(regExp)){
			request.setAttribute("alert","비정상적인 접근입니다.");
			return "/shop/alert.jsp";
		}
		
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
							request.setAttribute("username", c.getValue());
						}
					}else{
						int memseq = dao.selectMemberSeq(c.getValue());
						if(memseq == 0){
							request.setAttribute("redirect", "/cookiedelete.do");
							return "/shop/redirect.jsp";
						}else{
							request.setAttribute("username", c.getValue());
						}
					}
				}
			}
		}
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("page", page);
		map.put("val", val);
		
		
		int count = dao.selectProdListCount(map);
		int endpd = count-((Integer.parseInt(page)-1)*6);
		int startpd = endpd - 5;
			startpd = startpd <=0? 1:startpd;
			
		map.put("startnum",startpd);
		map.put("endnum",endpd);
		
		List<ProductVO> lst = dao.selectProdListMember(map);
		
		
		int pageNum = Integer.parseInt(page);
		int totalPage = count / 6;
			totalPage += count%6 == 0? 0 : 1; 
		int startPage = (pageNum/10);
			if(pageNum%10 != 0){
				startPage = (startPage*10)+1;
			}
			
		int endPage = startPage + 9;
			endPage = endPage <= totalPage? endPage : totalPage;

		PagingVO paging = new PagingVO();
		paging.setVal(val);
		paging.setPageNum(pageNum);
		paging.setStartPage(startPage);
		paging.setEndPage(endPage);
		paging.setTotalPage(totalPage);
		paging.setCount(count);
		
		
		request.setAttribute("page","productList");
		request.setAttribute("prodCount",count);
		request.setAttribute("prodLst",lst);
		request.setAttribute("paging", paging);
		
		return "/shop/layout.jsp";
		
	}

}
