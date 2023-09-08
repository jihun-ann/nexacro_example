<%
	String redirect = String.valueOf(request.getAttribute("redirect"));
	if(redirect == null || redirect == ""){
		redirect = "/home.do";
	}
	response.sendRedirect(request.getContextPath()+redirect);
%>               