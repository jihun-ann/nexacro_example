package shop.json;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shop.model.ShopDAO;

public class Duplicate implements CommandJson {
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
		map.put("key", "id");
		map.put("val", id);
		
		String result = dao.duplicateCheck(map);
		System.out.println(result);
		if(result.equals("0")){
			map.put("key", "phone");
			map.put("val", phone);
			result = dao.duplicateCheck(map);
			if(result.equals("0")){
				result = "0";
			}else{
				result = "2";
			}
		}else{
			result = "1";
		}
		
		return result;
	}

}
