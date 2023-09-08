package shop.action;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shop.model.ShopDAO;

public class CookieLogout implements CommandAction{
	public ShopDAO dao = ShopDAO.getInstance();
	public String requestPro(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {

		
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
		
		request.setAttribute("alert", "다른 기기에서 접속하여 로그아웃 처리되었습니다.");
		return "/shop/alert.jsp";
	}

}
