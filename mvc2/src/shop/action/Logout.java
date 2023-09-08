package shop.action;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shop.model.ShopDAO;

public class Logout implements CommandAction{
	public ShopDAO dao = ShopDAO.getInstance();
	public String requestPro(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {

		if(request.getCookies()== null){
			request.setAttribute("page", "login");
			return "/shop/layout.jsp";
		}
		
		
		String id = "";
		for(Cookie c : request.getCookies()){
			if(c.getName().contains("username")){
				id = c.getValue();
				Cookie cookie = new Cookie("username", "");
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
		}
		dao.deleteMemberLog(id);
		
		request.setAttribute("redirect", "/home.do");
			
		return "/shop/redirect.jsp";
	}

}
