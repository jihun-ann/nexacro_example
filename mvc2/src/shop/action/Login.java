package shop.action;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Login implements CommandAction{

	public String requestPro(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {

		for(Cookie c : request.getCookies()){
			if(c.getName().contains("username")){
				request.setAttribute("redirect", "/home.do");
				return "/shop/redirect.jsp";
			}
		}
		
		request.setAttribute("page", "login");
			
		return "/shop/layout.jsp";
	}

}
