<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="orderList-wrap">
	<div id="form-wrap">
		<span>
			총 ${paging.count}건
		</span>
		<select id="statusSelctor">
			<option value="" selected>전체</option>
			<option value="0" >배송준비중</option>
			<option value="1" >배송중</option>
			<option value="2" >배송완료</option>
			<option value="3" >환불요청중</option>
			<option value="4" >반품중</option>
			<option value="5" >환불완료</option>
			<option value="6" >구매확정</option>
		</select>
	</div>
	<c:if test="${empty orderList}">
		주문 내역이 없습니다.
	</c:if>
	<c:if test="${!empty orderList}">
		<c:forEach var="order" items="${orderList}">
			<div class="order-wrap" name="${order.orderseq}">
				<img src="http://192.168.0.16:8081/resources/order/${order.filename}"/>
				<div class="orderInfo" name="<c:out value="${order.orderseq}"/>">
					<p class="date"><c:out value="${order.date}"/> 결제</p>
					<p class="prodname">
						<c:out value="${order.prodname}"/>
						<c:if test="${order.prodcount > 1}">
							외<c:out value="${order.prodcount-1}"/>건	
						</c:if>
					</p>
					<p class="prodprice"><c:out value="${order.prodprice}"/></p>
					<button class="orderInfoBtn">자세히 보기</button>
				</div>
				<div class="delStatus-wrap">
					<span class="delStatus" value="<c:out value="${order.deliverystatus}"/>"></span>
					<c:if test="${order.deliverystatus == 1}">
						<span class="refundBtn statusBtn">
							환불요청
						</span>
						<span class="receptionBtn statusBtn">
							수취확인
						</span>
					</c:if>
					<c:if test="${order.deliverystatus == 2}">
						<span class="refundBtn statusBtn">
							환불요청
						</span>
						<span class="dealBtn statusBtn">
							구매확정
						</span>
					</c:if>
				</div>
			</div>
		</c:forEach>
	</c:if>
	<div id="paging-wrap">
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
		<c:if test="${paging.pageNum +10 < paging.totalPage}">
			<span class="pagingBtn" name="next">다음</span>
		</c:if>
		<c:if test="${paging.pageNum != paging.totalPage && paging.totalPage > 0}">
			<span class="pagingBtn" name="last">끝</span>
		</c:if>
	</div>
	<div id="searchForm-wrap">
		<input id="search" type="text" maxlength="15" placeholder="상품명" />
		<input id="searchBtn" type="button" value="검색" />
	</div>
</div>
<script>
let val = '${paging.val}';
let status = '${status}'
$(document).ready(function(){
	
	for (var i=0; i<$('.prodprice').length; i++) {
		let text = $('.prodprice:eq('+i+')').text();
			text = Number(text);
			text = text.toLocaleString('ko-KR');
		$('.prodprice:eq('+i+')').text(text+'원');
	}	
	
	for (var i=0; i<$('.delStatus-wrap').length; i++) {
		let statusNum = $('.delStatus-wrap:eq('+i+')').children('.delStatus').attr('value');
		let status = '';
		switch (statusNum) {
			case '0' : status = '배송준비중'; break;
			case '1' : status = '배송중'; break;
			case '2' : status = '배송완료'; break;
			case '3' : status = '환불요청중'; break;
			case '4' : status = '반품중'; break;
			case '5' : status = '환불완료'; break;
			case '6' : status = '구매확정'; break;
		}
		$('.delStatus-wrap:eq('+i+')').children('.delStatus').text(status);
	}
	

	if(val != ''){
		$('#statusSelctor').val(status);
	}
})
	
$('.pagingBtn').click(function(){
	let page = '${paging.pageNum}';
	if($(this).attr("name") == 'first'){
		page = 1;
	}else if($(this).attr("name") == 'last'){
		page = '${paging.totalPage}'
	}else if($(this).attr("name") == 'prev'){
		page = '${pagin.startPage}'-1;
	}else if($(this).attr("name") == 'next'){
		page ='${pagin.startPage}' +10;
	}else{
		page = $(this).attr("name");
	}
	let newForm = $('<form></form>');
	newForm.attr("method","post");
	newForm.attr("action","${pageContext.request.contextPath}/member/orderList.do");
	newForm.append($('<input/>',{type:'hidden',name:'status',value: status}));
	newForm.append($('<input/>',{type:'hidden',name:'page',value: page}));
	newForm.append($('<input/>',{type:'hidden',name:'val',value: val}));
	newForm.appendTo('body');
	newForm.submit();
})

$('.orderInfoBtn').click(function(){
	let orderseq = $(this).parent().attr("name");
	let newForm = $('<form></form>');
	newForm.attr("method","post");
	newForm.attr("action","${pageContext.request.contextPath}/member/orderInfo.do");
	
	newForm.append($('<input/>',{type:'hidden',name:'orderSeq',value: orderseq}));
	newForm.appendTo('body');
	newForm.submit();
})

$('.statusBtn').click(function(){
	let orderseq = $(this).parent().parent().attr('name');
	let newForm = $('<form></form>');
	newForm.attr("method","post");
	newForm.attr("action","${pageContext.request.contextPath}/member/refundReq.do");
	
	let status = '';
	if($(this).hasClass('refundBtn')){
		status = 3;
	}else if($(this).hasClass('receptionBtn')){
		status = 2;
	}else if($(this).hasClass('dealBtn')){
		status = 6;
	}
	
	newForm.append($('<input/>',{type:'hidden',name:'orderSeq',value: orderseq}));
	newForm.append($('<input/>',{type:'hidden',name:'status',value: status}));
	newForm.appendTo('body');
	newForm.submit();
})

$('#statusSelctor').change(function(){
	let newForm = $('<form></form>');
	newForm.attr("method","post");
	newForm.attr("action","${pageContext.request.contextPath}/member/orderList.do");
	
	newForm.append($('<input/>',{type:'hidden',name:'status',value: $(this).val()}));
	newForm.append($('<input/>',{type:'hidden',name:'page',value: 1}));
	newForm.append($('<input/>',{type:'hidden',name:'val',value: val}));
	newForm.appendTo('body');
	newForm.submit();
})

$('#searchBtn').click(function(){
	let val = $('#search').val();
	
	let newForm = $('<form></form>');
	newForm.attr("method","post");
	newForm.attr("action","${pageContext.request.contextPath}/member/orderList.do");
	
	newForm.append($('<input/>',{type:'hidden',name:'page',value: 1}));
	newForm.append($('<input/>',{type:'hidden',name:'val',value: val}));
	newForm.appendTo('body');
	newForm.submit();
})
</script>