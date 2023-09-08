package shop.action;

import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shop.model.ShopDAO;

public class Signin implements CommandAction{

	public ShopDAO dao = ShopDAO.getInstance();
	
	public String requestPro(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {

		for(Cookie c : request.getCookies()){
			if(c.getName().contains("username")){
				request.setAttribute("redirect", "/home.do");
				return "/shop/redirect.jsp";
			}
		}
		
		String id = String.valueOf(request.getParameter("username"));
		String pass = String.valueOf(request.getParameter("password"));
		HashMap<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("pass", pass);
		
		String check = dao.duplicateIdPw(map);
		if(check == null || check.equals("")){
			request.setAttribute("page", "login");
			request.setAttribute("alert", "아이디와 비밀번호를 확인해주세요.");
			return "/shop/layout.jsp";
		}
		
		String idAddr = request.getRemoteAddr();
		String browser = null; 

		String agent = request.getHeader("User-Agent");
		if(agent.indexOf("Trident") > -1)        browser = "MSIE"; 
		else if(agent.indexOf("Edg") > -1)   browser = "Edg"; 
		else if(agent.indexOf("Chrome")> -1)  browser = "Chrome"; 
		else if(agent.indexOf("Opera") > -1)   browser = "Opera"; 
		else if(agent.indexOf("iPhone")> -1 && agent.indexOf("Mobile") > -1) browser = "iPhone"; 
		else if(agent.indexOf("Android")> -1 && agent.indexOf("Mobile")> -1) browser = "Android"; 
		
		map.put("userip", idAddr);
		map.put("browser", browser);
		
		String logseq = dao.checkMemberLog(map);
		if(logseq == null || logseq.equals("")){
			map.put("type", "insert");
		}else{
			map.put("type", "update");
		}
		dao.insertMemberLog(map);
		
		Cookie cookie = new Cookie("username", id);
		cookie.setMaxAge(60*60*24*7);
		response.addCookie(cookie);
		
		request.setAttribute("redirect", "/home.do");
		return "/shop/redirect.jsp";
	}

}
