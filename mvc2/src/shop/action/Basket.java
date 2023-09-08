package shop.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shop.VO.BasketVO;
import shop.model.ShopDAO;

public class Basket implements CommandAction{

	public ShopDAO dao = ShopDAO.getInstance();
	
	public String requestPro(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		request.setCharacterEncoding("utf-8");
		String id = "";
		
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
		
		
		int memseq = dao.selectMemberSeq(id);
		
		List<BasketVO>blst = dao.selectBasketMember(memseq);
		List<HashMap<String, Object>> prodSeqlst = new ArrayList<>();
		List<Integer> checkL = new ArrayList<>();
		for(int i=0; i<blst.size(); i++){
			if(checkL.contains(blst.get(i).getProductseq())){
				
			}else{
				checkL.add(blst.get(i).getProductseq());
				HashMap<String, Object> map= new HashMap<>();
				map.put("prodSeq", blst.get(i).getProductseq());
				map.put("prodName", blst.get(i).getProductname());
				map.put("prodImg", blst.get(i).getFilename());
				prodSeqlst.add(map);
			}
		}
		
		request.setAttribute("page","basket");
		request.setAttribute("basketLst",blst);
		request.setAttribute("prodLst",prodSeqlst);
		request.setAttribute("username",id);
		return "/shop/layout.jsp";
	}

}
