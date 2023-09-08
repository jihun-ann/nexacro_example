package shop.json;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shop.model.ShopDAO;

public class MemberDuplicate implements CommandJson {
	public ShopDAO dao = ShopDAO.getInstance();
		
	public String requestPro(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		
		request.setCharacterEncoding("UTF-8");
		String phone = "";
		
		if(request.getParameter("phone") != null){
			phone = String.valueOf(request.getParameter("phone"));
		}
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("key", "phone");
		map.put("val", phone);
		String result = dao.duplicateCheck(map);
		
		if(result.equals("0")){
			result = "0";
		}else{
			result = "1";
		}
		return result;
	}

}
