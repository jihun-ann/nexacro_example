package shop.action;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;

import shop.VO.OrderProductVO;
import shop.VO.PagingVO;
import shop.VO.ProductVO;
import shop.model.ShopDAO;

public class OrderPayment implements CommandAction{
	public ShopDAO dao = ShopDAO.getInstance();
	
	private String path = "C:/RootSystem/mvc2/WebContent/resources/image";
	private String orpath = "C:/RootSystem/mvc2/WebContent/resources/order";

	public String requestPro(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {

		request.setCharacterEncoding("utf-8");
		
		String id="";
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
							request.setAttribute("redirect", "/cookieLogout.do");
							return "/shop/redirect.jsp";
						}else{
							id = c.getValue();
						}
					}else{
						int memseq = dao.selectMemberSeq(c.getValue());
						if(memseq == 0){
							request.setAttribute("redirect", "/cookiedelete.do");
							return "/shop/redirect.jsp";
						}else{
							id = c.getValue();
						}
					}
				}
			}
		}
		
		if(id.equals("")){
			request.setAttribute("redirect", "/login.do");
			return "/shop/redirect.jsp";
		}
		int memseq = dao.selectMemberSeq(id);
		String bankStatus = request.getParameter("bankStatus");
		String account = request.getParameter("account");
		
		
		if(request.getParameter("bankStatus") == null || request.getParameter("bankStatus").equals("")){
			bankStatus = "-1";
		}
		if(request.getParameter("account") == null || request.getParameter("account").equals("")){
			account = "-1";
		}
		
		
		String[] prodseq = request.getParameterValues("prodSeq[]");
		String message = request.getParameter("message");
		String address = request.getParameter("address");
		String allPrice = request.getParameter("allPrice");
		
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("member", memseq);
		map.put("prodCount", prodseq.length);
		map.put("message", message);
		map.put("price", allPrice);
		map.put("address", address);
		
		if(bankStatus.equals("-1")){
			map.put("payment", "이체"+account);
			map.put("kind", "account");
		}else if(account.equals("-1")){
			map.put("payment", "카드"+bankStatus);
			map.put("kind", "paycard");
		}
		
		dao.updatePayment(map);
		
		List<OrderProductVO> oplst = new ArrayList<>();
		for (int i=0; i<prodseq.length; i++) {
			map.put("prod", prodseq[i]);
			List<OrderProductVO> op = dao.selectBasketPayment(map);
			oplst.addAll(op);
			if(op.isEmpty()){
				request.setAttribute("alert","비정상적인 접근입니다.");
				return "/shop/alert.jsp";
			}
		}
		dao.insertOrder(map);
		
		HashMap<String, String> filelst = new HashMap<>();
		List<String> seqLs = new ArrayList<>();
		for (int i=0; i<oplst.size(); i++) {
			map.put("prod", oplst.get(i).getProductseq());
			map.put("prodname", oplst.get(i).getProductname());
			map.put("option", oplst.get(i).getOptionseq());
			map.put("opname", oplst.get(i).getOptionname());
			map.put("count", oplst.get(i).getOrdercount());
			map.put("price", oplst.get(i).getPrice());
			String ext = FilenameUtils.getExtension(oplst.get(i).getFilename());
			map.put("filename", oplst.get(i).getProductseq()+"_0."+ext);
			
			if(filelst.containsKey(String.valueOf(oplst.get(i).getProductseq()))){
				System.out.println("y");
			}else{
				System.out.println("n");
				filelst.put(String.valueOf(oplst.get(i).getProductseq()), oplst.get(i).getFilename());
			}
			dao.insertOrderProduct(map);
		}
		
		for (int i=0; i<prodseq.length; i++) {
			map.put("prod", prodseq[i]);
			dao.basketDeleteProd(map);
			dao.updateordercountProd(map);
			
			String filename = filelst.get(String.valueOf(prodseq[i]));
			File newFile = new File(orpath+File.separator+filename);
			
			if(!newFile.exists()){
				File orFile = new File(path+File.separator+filename);
				Files.copy(orFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			
		}
		
		request.setAttribute("redirect", "/member/orderList.do");
		request.setAttribute("alert", "정상적으로 결제 되었습니다.");
		
		return "/shop/redirect.jsp";
	}

}
