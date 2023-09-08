package shop.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shop.VO.OrderProductVO;
import shop.VO.OrderVO;
import shop.VO.PagingVO;
import shop.VO.MemberVO;
import shop.VO.BasketVO;
import shop.model.ShopDAO;

public class OrderInfo implements CommandAction{

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
		
		String orderSeq = request.getParameter("orderSeq");
		int memseq = dao.selectMemberSeq(id);
		List<OrderProductVO> orderLst = dao.selectOrderInfoOne(orderSeq);
		List<HashMap<String, Object>> prodLst = new ArrayList<>();
		List<Integer> checkLst = new ArrayList<>();
		
		for(int i=0; i<orderLst.size(); i++){
			if(checkLst.contains(orderLst.get(i).getProductseq())){
				
			}else{
				checkLst.add(orderLst.get(i).getProductseq());
				HashMap<String, Object> map= new HashMap<>();
				map.put("prodSeq", orderLst.get(i).getProductseq());
				map.put("prodName", orderLst.get(i).getProductname());
				map.put("prodImg", orderLst.get(i).getFilename());
				map.put("prodPrice", orderLst.get(i).getPrice());
				prodLst.add(map);
			}
		}
		
		OrderVO order = dao.selectOrderOneOrderseq(orderSeq);
		System.out.println(order);
		
		request.setAttribute("orderLst",orderLst);
		request.setAttribute("orgOrder",order);
		request.setAttribute("prodLst",prodLst);
		request.setAttribute("page","orderInfo");
		request.setAttribute("username",id);
		return "/shop/layout.jsp";
	}

}
