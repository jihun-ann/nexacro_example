package shop.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shop.json.CommandJson;

public class ControllerAjax extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// 명령어와 명령어 처리 클래스를 쌍으로 저장
		private Map<String, Object> commandMap = new HashMap<String, Object>();

		// 명령어와 처리클래스가 매핑되어 있는 properties 파일을
		// 읽어서 Map객체인 commandMap에 저장
		// 명령어와 처리클래스가 매핑되어 있는 properties 파일은 Command.properties파일
		public void init(ServletConfig config) throws ServletException {
			// web.xml 에서 PropertyConfig에 해당하는 init-param의 값을 읽어옴
			String props = config.getInitParameter("propertyConfig");
			// 명령어와 처리클래스의 매핑정보를 저장할 Properties객체 생성
			Properties pr = new Properties();
			String path = config.getServletContext().getRealPath("/WEB-INF");
			FileInputStream f = null;
			try {
				// Command.properties파일의 내요을 읽어옴
				f = new FileInputStream(new File(path, props));
				// Command.properties파일의 정보를 Properties객체에 저장
				pr.load(f);
			} catch (IOException e) {
				throw new ServletException(e);
			} finally {
				if (f != null)
					try {
						f.close();
					} catch (IOException ex) {
					}
			}
			// Iterator객체는 Enumeration 객체를 확장시킨 개념의 갹체
			Iterator<Object> keylter = pr.keySet().iterator();
			// 객체를 하나씩 꺼내서 그 객체명으로 Properties객체에 저장된 객체에 접근
			while (keylter.hasNext()) {
				String command = (String) keylter.next();
				String className = pr.getProperty(command);
				try {// 해당 문자열을 클래스로 만든다
					@SuppressWarnings("rawtypes")
					Class commandClass = Class.forName(className);
					Object commandInstance = commandClass.newInstance();// 해당클래스의
																		// 객체를 생성
					// Map객체인 commandMap에 객체 저장
					commandMap.put(command, commandInstance);
				} catch (ClassNotFoundException e) {
					throw new ServletException(e);
				} catch (InstantiationException e) {
					throw new ServletException(e);
				} catch (IllegalAccessException e) {
					throw new ServletException(e);
				}

			}
		}

		public void doGet(// get방식의 서비스 메소드
				HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			requestPro(request, response);
		}

		protected void doPost(// post 방식의 서비스 메소드) {
				HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			requestPro(request, response);
		}

		// 사용자의 요청을 분석해서 새당 작업을 처리
		private void requestPro(HttpServletRequest request,
				HttpServletResponse response) throws ServletException, IOException {
			String result = null;
			CommandJson com = null;

			try {
				String command = request.getRequestURI();
				if (command.indexOf(request.getContextPath()) == 0) {
					command = command.substring(request.getContextPath().length());
				}
				com = (CommandJson) commandMap.get(command);
				result = com.requestPro(request, response);
			} catch (Throwable e) {
				throw new ServletException(e);
			}
			PrintWriter pw = response.getWriter();
			pw.print(result);
			pw.flush();
		}

}
