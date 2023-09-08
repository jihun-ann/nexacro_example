package shop.json;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import shop.model.ShopDAO;

public class OptionCountCheck implements CommandJson {
	public ShopDAO dao = ShopDAO.getInstance();
		
	public String requestPro(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		
		request.setCharacterEncoding("UTF-8");
		String data = String.valueOf(request.getParameter("data"));
		
		JSONParser jsonParser = new JSONParser();
		JSONArray jarray = null;
		try {
			jarray = (JSONArray) jsonParser.parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONArray result = new JSONArray();
		JSONObject resultob = new JSONObject();
		List<HashMap<String, Object>> oplst = new ArrayList<>();
		
		for (int i=0; i<jarray.size(); i++) {
			JSONObject job = (JSONObject)jarray.get(i);
			String opseq = String.valueOf(job.get("opseq"));
			String ordercount = String.valueOf(job.get("opcount"));
			HashMap<String, Object> map = new HashMap<>();
			map.put("opseq", opseq);
			map.put("ordercount", ordercount);
			
			int opcount = dao.selectProductHasCount(opseq);
			map.put("opcount", opcount);
			oplst.add(map);
			
			if(opcount <= 0){
				resultob.put("soldOpseq", opseq);
				result.add(resultob);
			}
			
			
		}
		
		if(result.size() == 0){
			for (int i=0; i<oplst.size(); i++) {
				String order = String.valueOf(oplst.get(i).get("ordercount"));
				String opcount = String.valueOf(oplst.get(i).get("opcount"));
				
				int orderint = Integer.parseInt(order);
				int opcountint = Integer.parseInt(opcount);
				int subtract = opcountint - orderint;
				
				if(subtract < 0){
					resultob.put("subOpseq", oplst.get(i).get("opseq"));
					resultob.put("leftcount", opcount);
					resultob.put("overcount", subtract*-1);
					result.add(resultob);
				}
			}
		}
		
		if(result.size() == 0){
			for (int i=0; i<oplst.size(); i++) {
				String order = String.valueOf(oplst.get(i).get("ordercount"));
				String opseq = String.valueOf(oplst.get(i).get("opseq"));
				
				HashMap<String, Object> map = new HashMap<>();
				map.put("order", order);
				map.put("opseq", opseq);
				
				dao.updateOptionCount(map);
			}
		}
		
		return result.toJSONString();
	}
}
