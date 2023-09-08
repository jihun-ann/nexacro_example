package shop.json;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import shop.VO.ProdOptionVO;
import shop.VO.ProductVO;

import shop.model.ShopDAO;

public class ModalProdInfo implements CommandJson {
	public ShopDAO dao = ShopDAO.getInstance();
		
	public String requestPro(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		
		request.setCharacterEncoding("UTF-8");
		String prodseq = request.getParameter("prodSeq");
		
		ProductVO vo = dao.selectOneProd(prodseq);
		List<ProdOptionVO> oplst = dao.selectProdOptionAll(prodseq);
		
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("prodname", vo.getName());

		
		List<JSONObject> jlst = new ArrayList<>();
		for (int i=0; i<oplst.size(); i++) {
			HashMap<String, Object> opmap= new HashMap<>();
			opmap.put("optionseq", oplst.get(i).getOptionseq());
			opmap.put("productcount", oplst.get(i).getProductcount());
			opmap.put("optionname", oplst.get(i).getName());
			opmap.put("optionprice", oplst.get(i).getProductprice());
			JSONObject jobj2 = new JSONObject(opmap);
			jlst.add(jobj2);
		}
		
		map.put("option", jlst);
		
		JSONObject jobj = new JSONObject(map);
		
		return jobj.toJSONString();
	}

}
