package shop.json;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shop.model.ShopDAO;

public class PwFind implements CommandJson {
	public ShopDAO dao = ShopDAO.getInstance();
		
	public String requestPro(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		
		request.setCharacterEncoding("UTF-8");
		String id = "";
		String phone = "";
		
		if(request.getParameter("id") != null){
			id = String.valueOf(request.getParameter("id"));
		}
		if(request.getParameter("phone") != null){
			phone = String.valueOf(request.getParameter("phone"));
		}
		HashMap<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("phone", phone);
		
		String result = dao.selectFindPw(map);
		if(result == null || result == ""){
			result = "-1";
		}
		
		return result;
	}

}
