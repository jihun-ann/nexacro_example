package shop.action;

import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shop.model.ShopDAO;

public class SignUp implements CommandAction{

	public ShopDAO dao = ShopDAO.getInstance();
	
	public String requestPro(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {

		for(Cookie c : request.getCookies()){
			if(c.getName().contains("username")){
				request.setAttribute("redirect", "/home.do");
				return "/shop/redirect.jsp";
			}
		}
		
		
		request.setAttribute("page", "signup");
		return "/shop/layout.jsp";
	}

}
