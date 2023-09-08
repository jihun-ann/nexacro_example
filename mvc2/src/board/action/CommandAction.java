package board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CommandAction {
//요청 파라미터로 명령어를 전달하는 방식의 슈퍼 인터페이스
	
	public String requestPro(HttpServletRequest request, HttpServletResponse response)throws Throwable;
}
