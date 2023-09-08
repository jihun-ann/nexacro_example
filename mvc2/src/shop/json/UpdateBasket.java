package shop.json;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.model.ShopDAO;

public class UpdateBasket implements CommandJson {
	public ShopDAO dao = ShopDAO.getInstance();
		
	public String requestPro(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		
		request.setCharacterEncoding("UTF-8");
		String data = request.getParameter("data");
		
		
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
		
		
		JSONParser jsonParser = new JSONParser();
		JSONArray jarray = null;
		
		try {
			jarray = (JSONArray) jsonParser.parse(data);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0";
		}
		
		List<Map<String, Object>> update = new ArrayList<>();
		List<Map<String, Object>> delete = new ArrayList<>();
        List<Map<String, Object>> insert = new ArrayList<>();
		for (int i=0; i<jarray.size(); i++) {
			JSONObject job = (JSONObject)jarray.get(i);
			Map<String, List<Map<String, Object>>> jsonObject = new ObjectMapper().readValue(job.toJSONString(), Map.class);
			update = jsonObject.get("hasLst");
			delete= jsonObject.get("bseqLst");
			insert = jsonObject.get("addLst");
		}

		for (int i=0; i<update.size(); i++) {
			if(update.get(i).get("hasSeq").equals(-1) || update.get(i).get("hasSeq") == null){
				
			}else{
				String seq = String.valueOf(update.get(i).get("hasSeq"));
				String count = String.valueOf(update.get(i).get("hasCount"));
				
				HashMap<String, Object> map = new HashMap<>();
				map.put("seq",seq);
				map.put("count",count);
				dao.updateBasketOne(map);
			}
		}
		for (int i=0; i<delete.size(); i++) {
			if(delete.get(i).get("deleteSeq").equals(-1) || delete.get(i).get("deleteSeq") == null){
				
			}else{
				String seq = String.valueOf(delete.get(i).get("deleteSeq"));
				dao.basketDeleteOne(seq);
			}
		}
		for (int i=0; i<insert.size(); i++) {
			if(insert.get(i).get("addSeq").equals(-1) || insert.get(i).get("addSeq") == null){
				
			}else{
				String seq = String.valueOf(insert.get(i).get("addSeq"));
				String count = String.valueOf(insert.get(i).get("addCount"));
				String prod = String.valueOf(insert.get(i).get("prodSeq"));
				int memseq = dao.selectMemberSeq(id);
				
				HashMap<String, Object> map = new HashMap<>();
				map.put("opSeq",seq);
				map.put("count",count);
				map.put("prodSeq",prod);
				map.put("memseq",memseq);
				dao.insertBasket(map);
			}
		}
		
		return "1";
	}

}
