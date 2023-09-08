<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="view/color.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>게시판</title>
<link href="style.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="${bodyback_c }">
	<center>
		<b>글목록(전체 글:${count})</b>
		<table width="700">
			<tr>
				<td align="right"><a
					href="/mvc2/board/writeForm.do">글쓰기</a></td>
			</tr>
		</table>
		<c:if test="${count == 0}">
			<table width="700" border="1" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center">게시판에 저장된 글이 없습니다.</td>
				</tr>
			</table>
		</c:if>
		<c:if test="${count >0 }">
			<table border="1" width="700" cellspacing="0" cellpadding="0"
				align="center">
				<tr height="30" bgcolor="${value_c }">
					<td align="center" width="40">번호</td>
					<td align="center" width="250">제목</td>
					<td align="center" width="100">작성자</td>
					<td align="center" width="150">작성일</td>
					<td align="center" width="50">조회</td>
					<td align="center" width="100">IP</td>
				</tr>
				<c:forEach var="list" items="${boardList}">
					<tr height="30">
						<td align="center" width="50"><c:out value="${number}" /> <c:set
								var="number" value="${number-1 }" /></td>
						<td width="250"><c:if test="${list.depth > 0 }">
             &nbsp;&nbsp;&nbsp;L
            </c:if> <a href="/mvc2/board/content.do?num=${list.num}&pageNum=${currentPage}">${list.subject}</a>
							<c:if test="${list.readcount >= 20}">
								<img src="hot.gif" border="0" height="16" />
							</c:if></td>
						<td align="center" width="100">${list.writer}</td>
						<td align="center" width="150"><fmt:formatDate value="${list.regdate}" pattern="yyyy-MM-dd HH:mm"/></td>
						<td align="center" width="50">${list.readcount}</td>
						<td align="center" width="100">${list.ip}</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
	<c:if test="${count > 0 }">
			<c:set var="imsi" value="${count % pageSize == 0 ? 0 : 1 }" />	
			<fmt:parseNumber var="pageCount" value="${count / pageSize + imsi }" integerOnly="true" />			
			<c:set var="pageBlock" value="${5}" />			
			<fmt:parseNumber var="result" value="${currentPage / pageBlock}" integerOnly="true" />				
			<c:if test="${currentPage%5  ==0 }">
			<c:set var="result" value="${result-1}" />
			</c:if>			
			<c:set var="startPage" value="${result * pageBlock+1}" />
			<c:set var="endPage" value="${startPage + pageBlock-1 }" />			
			<c:if test="${endPage > pageCount }">
				<c:set var="endPage" value="${pageCount }" />
			</c:if>
		
			<c:forEach var="i" begin="${startPage}" end="${endPage}">
			<a href="/mvc2/board/list.do?pageNum=${i}">
			<c:if test="${i==currentPage}"><b><font color="red"> [${i}]</font></b></c:if><c:if test="${i!=currentPage}">[${i}]</c:if>
			</a>
			
			</c:forEach>

			</c:if>
	</center>
</body>
</html>