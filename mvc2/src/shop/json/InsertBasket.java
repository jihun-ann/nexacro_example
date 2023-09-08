package shop.json;


import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shop.model.ShopDAO;

public class InsertBasket implements CommandJson {
	public ShopDAO dao = ShopDAO.getInstance();
		
	public String requestPro(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		
		request.setCharacterEncoding("UTF-8");
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
							return "98";
						}else{
							id = c.getValue();
						}
					}else{
						int memseq = dao.selectMemberSeq(c.getValue());
						if(memseq == 0){
							return "99";
						}else{
							id = c.getValue();
						}
					}
				}
			}
		}
		
		int memberseq = dao.selectMemberSeq(id);
		
		String prodSeq = String.valueOf(request.getParameter("prodSeq"));
		String opSeq = String.valueOf(request.getParameter("opSeq"));
		String count = String.valueOf(request.getParameter("count"));
		
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("prodSeq", prodSeq);
		map.put("opSeq", opSeq);
		map.put("memseq", memberseq);
		map.put("count", count);
		
		String result = "";
		HashMap<String, Object> check = dao.basketCheck(map);
		
		int res = 0;
		if(check.get("ORDERCOUNT") == null){
			res = dao.insertBasket(map);
		}else{
			int rescount = Integer.parseInt(count) + Integer.parseInt(String.valueOf(check.get("ORDERCOUNT")));
			rescount = rescount >= 20 ? 19 : rescount;
			map.put("check", check.get("BASKETSEQ"));
			map.put("count", rescount);
			res = dao.updateBasketCount(map);
		}
		
		result = String.valueOf(res);
		return result;
	}

}
