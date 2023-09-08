package shop.action;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.tracing.dtrace.ModuleAttributes;

import shop.VO.MemberVO;
import shop.VO.PagingVO;
import shop.VO.ProductVO;
import shop.model.ShopDAO;

public class SaveMember implements CommandAction{
	public ShopDAO dao = ShopDAO.getInstance();

	public String requestPro(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {

		request.setCharacterEncoding("utf-8");
		
		for(Cookie c : request.getCookies()){
			if(c.getName().contains("username")){
				request.setAttribute("redirect", "/home.do");
				return "/shop/redirect.jsp";
			}
		}
		
		MemberVO vo = new MemberVO();
		vo.setId(String.valueOf(request.getParameter("id")));
		vo.setPass(String.valueOf(request.getParameter("pass")));
		vo.setName(String.valueOf(request.getParameter("name")));
		vo.setPhone(String.valueOf(request.getParameter("phone")));
		
		String addressCode = String.valueOf(request.getParameter("addressCode"));
		String simpleAddress = String.valueOf(request.getParameter("simpleAddress"));
		String detailAddress = String.valueOf(request.getParameter("detailAddress"));
		String extraAddress = String.valueOf(request.getParameter("extraAddress"));
		
		vo.setAddress(addressCode+"/"+simpleAddress+","+detailAddress+extraAddress);
		vo.setRole("ROLE_MEMBER");
		
		dao.signUpMember(vo);

		request.setAttribute("alert","회원가입이 완료되었습니다.");
		return "/shop/alert.jsp";
	}

}
