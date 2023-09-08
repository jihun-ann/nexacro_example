<%@page import="java.lang.Object"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script  src="https://code.jquery.com/jquery-3.7.0.js"  integrity="sha256-JlqSTELeR4TLqP0OG9dxM7yDPqX1ox/HfgiSLBj8+kM="  crossorigin="anonymous"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/shop/css/layout.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/shop/css/<c:out value="${page}"/>.css">
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>   
</head>
<body>
	<div id="container">
		<div id="header">
			<div id="header-logo" class="headerBtn">
				<div>로고</div>
			</div>
			<div id="header-btn-wrap">
				<c:if test="${username == null}">
					<span id="loginBtn" class="headerBtn">로그인</span>
					<span id="signupBtn" class="headerBtn">회원가입</span>
				</c:if>
				<c:if test="${username != null }">
					<p style="font-weight: bold;"><c:out value="${username}"/>님</p>
					<form id="logout-form" action="${pageContext.request.contextPath}/logout.do" method="post">
						<span id="logoutBtn" class="headerBtn" >로그아웃</span>
					</form>
				</c:if>
				<span id="basketBtn" class="headerBtn">장바구니</span>
				<span id="mypageBtn" class="headerBtn">마이페이지</span>
				<span id="orderListBtn" class="headerBtn">구매내역</span>
			</div>
		</div>
		<div id="body">
			<c:if test="${page == null}">
				<c:import url="./test.jsp"></c:import>
			</c:if>
			<c:if test="${page != null}">
				<c:import url="./${page}.jsp"></c:import>
			</c:if>
		</div>
	</div>
<script>
$(document).ready(function(){
	let alertmessage = '${alert}';
	if(alertmessage != ''){
		alert(alertmessage);
	}
	
	let test = '${page}';
	if(test==null || test==''){
		window.location.href = "${pageContext.request.contextPath}/home.do";
	}
})

	$(".headerBtn").click(function(){
		if($(this).attr('id') == 'loginBtn'){
			window.location.href = "${pageContext.request.contextPath}/login.do";
			
		}else if($(this).attr('id') == 'signupBtn'){
			window.location.href = "${pageContext.request.contextPath}/signup.do";
			
		}else if($(this).attr('id') == 'basketBtn'){
			window.location.href = "${pageContext.request.contextPath}/member/basket.do";
			
		}else if($(this).attr('id') == 'mypageBtn'){
			window.location.href = "${pageContext.request.contextPath}/member/mypage.do";
			
		}else if($(this).attr('id') == 'orderListBtn'){
			window.location.href = "${pageContext.request.contextPath}/member/orderList.do";
			
		}else if($(this).attr('id') == 'logoutBtn'){
			console.log('로그아웃');
			$('#logout-form').submit();
		}else{
			window.location.href = "${pageContext.request.contextPath}/home.do";
		}
	})
</script>
</body>
</html>