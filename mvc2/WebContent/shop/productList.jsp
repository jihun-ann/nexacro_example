<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0" />
<div id="searchForm">
	<span style="margin: 0 100px; font-size: 17px;">총 ${prodCount} 개</span>
	<input id="searchVal" type="text" maxlength="10"/>
	<button id="searchBtn"><span class="material-symbols-outlined">search</span></button>
</div>
<div id="prodList-wrap">
	<c:forEach var="prod"  items="${prodLst}">
		<div class="prod-wrap" name="<c:out value="${prod.productseq}"/>">
			<div class="prod-img" style="background-image: url('http://192.168.0.16:8081/resources/image/<c:out value="${prod.filename}"/>');">
			</div>
			<div class="prod-info">
				<p class="prod-name"><c:out value="${prod.name}"/></p>
				<p class="prod-price"><c:out value="${prod.productprice}"/></p>
			</div>
		</div>
	</c:forEach>	
</div>
<div id="pageBtn-wrap">
	<c:if test="${paging.pageNum != 1}">
		<span class="pagingBtn" name="first">처음</span>
	</c:if>
	<c:if test="${paging.pageNum > 10}">
		<span class="pagingBtn" name="prev">이전</span>
	</c:if>
	<c:forEach var="num" begin="${paging.startPage}" end="${paging.endPage}">
		<c:if test="${paging.pageNum eq num}">
			<span style="color:red; font-size:17px; font-weight:bold;" name="<c:out value="${num}"/>">${num}</span>
		</c:if>
		<c:if test="${paging.pageNum != num}">
			<span class="pagingBtn" name="<c:out value="${num}"/>">${num}</span>
		</c:if>
	</c:forEach>
	<c:if test="${paging.startPage+9 < paging.totalPage}">
		<span class="pagingBtn" name="next">다음</span>
	</c:if>
	<c:if test="${paging.pageNum != paging.totalPage}">
		<span class="pagingBtn" name="last">끝</span>
	</c:if>
</div>

<script>
	$(document).ready(function(){
		for (var i=0; i<$('.prod-price').length; i++) {
			let text = $('.prod-price:eq('+i+')').text();
			text = Number(text);
			text = text.toLocaleString('ko-KR');
		$('.prod-price:eq('+i+')').text(text+'원');
		}
		
		for (var i=0; i<$('.prod-img').length; i++) {
			let bg = $('.prod-img:eq('+i+')').css("background-image");
				bg = bg.substr(0,bg.length-2);
			let date = new Date().getTime();
				bg = bg+"?v="+date+'\")';
			$('.prod-img:eq('+i+')').css("background-image",bg);
		}
	})
	
	$('.prod-wrap').click(function(e){
		let newForm = $('<form></form>');
		newForm.attr("method","post");
		newForm.attr("action","${pageContext.request.contextPath}/prodInfo.do");
		
		newForm.append($('<input/>',{type:'hidden',name:'prodSeq',value:$(this).attr('name')}));
		newForm.appendTo('body');
		newForm.submit();
	})
	
	$('.pagingBtn').click(function(){
		let page = '${paging.pageNum}';
		let startPage = '${paging.startPage}';
		let val = '${paging.val}';
		if($(this).attr("name") == 'first'){
			page = 1;
		}else if($(this).attr("name") == 'last'){
			page = '${paging.totalPage}'
		}else if($(this).attr("name") == 'prev'){
			page = Number(startPage) -1;
		}else if($(this).attr("name") == 'next'){
			page = Number(startPage) +10;
		}else{
			page = $(this).attr("name");
		}
		
		
		let newForm = $('<form></form>');
		newForm.attr("method","post");
		newForm.attr("action","${pageContext.request.contextPath}/home.do");
		
		newForm.append($('<input/>',{type:'hidden',name:'page',value: page}));
		newForm.append($('<input/>',{type:'hidden',name:'val',value: val}));
		newForm.appendTo('body');
		newForm.submit();
	})
	
	$('#searchBtn').click(function(){
		let val = $('#searchVal').val();
		let newForm = $('<form></form>');
		newForm.attr("method","post");
		newForm.attr("action","${pageContext.request.contextPath}/home.do");
		
		newForm.append($('<input/>',{type:'hidden',name:'page',value: 1}));
		newForm.append($('<input/>',{type:'hidden',name:'val',value: val}));
		newForm.appendTo('body');
		newForm.submit();
	})
</script>
