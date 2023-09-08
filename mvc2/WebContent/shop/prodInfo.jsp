<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0" />
<div id="prodInfo-wrap" >
	<div id="simpleProd">
		<img id="prodImg"/>
		<div id="prodOption">
			<h1><c:out value="${prod.name}"/></h1>
			<c:if test="${prod.opcount <= 1}">
				<c:if test="${optionList[0].productcount > 0}">
					<h4 id="singleOp" value="${optionList[0].optionseq}" name="${optionList[0].productcount}">
						<c:out value="${optionList[0].name}"/>
					</h4>
				</c:if>
				<c:if test="${optionList[0].productcount <= 0}">
					<h4 id="singleOp" style="text-decoration: line-through;" value="-99">
						<c:out value="${optionList[0].name}"/>(품절)
					</h4>
				</c:if>
			</c:if>
			<div id="prodPrice">
				<span id="changePrice">
					<c:if test="${prod.opcount<=1 }">
						<c:out value="${optionList[0].productprice}"/>
					</c:if>
				</span><span>원</span>
			</div>
			<c:if test="${prod.opcount > 1}">
				<select id="prodSelect">
					<option selected value="0" name="0">옵션리스트</option>
					<c:forEach var="op" items="${optionList}">
						<c:if test="${op.productcount == 0}">
							<option value="-99" name="-99" disabled><c:out value="${op.name}(품절)"/></option>
						</c:if>
						<c:if test="${op.productcount != 0 }">
							<option value="<c:out value="${op.productprice}"/>" name="<c:out value="${op.optionseq}"/>"><c:out value="${op.name}"/></option>
						</c:if>
					</c:forEach>
				</select>
			</c:if>
			<div id="prodCount">
				<span class="material-symbols-outlined countBtn">add</span>
				<input id="opCount" type="number" value="1" readonly/>
				<span class="material-symbols-outlined countBtn">remove</span>
			</div>
			<div id="orderBtn-wrap">
				<button id="orderBasket">장바구니</button>
				<button id="orderBuy">구매하기</button>
			</div>
		</div>
	</div>
	<div id="detailProd">
		<img id="prodDetailImg"/>
	</div>
</div>

<script>
	let opcount = 0;
	let orgprice = 0;
	$(document).ready(function(){
		let tmpDate = new Date().getTime();
		opcount = '${prod.opcount}';

		$('#prodImg').attr('src', 'http://192.168.0.16:8081/resources/image/'+'${prodimg.SIMPLE}'+'?v='+tmpDate);
		$('#prodDetailImg').attr('src', 'http://192.168.0.16:8081/resources/image/'+'${prodimg.DETAIL}'+'?v='+tmpDate);
				
		
		let opseq = 0;
		if(opcount<=1){
			orgprice = '${optionList[0].productprice}';
		}
		
		
		
		
		if(opcount >1){
			$('#prodSelect').change(function(e){
				if($('#prodSelect').val() == null || $('#prodSelect').val() == ''){
					$('#changePrice').text(0);
				}else{
					$('#changePrice').text($('#prodSelect').val());
					orgprice = $('#prodSelect').val();
					$('#opCount').val(1);
				}
			})
		}
	})
	
	$('#orderBtn-wrap button').click(function(){
		let prodseq = '${prod.productseq}';
		let opseq = 0;
		let user = '${username}';
		
		if(opcount<=1){
			opseq = $('#singleOp').attr('value');
		}else if(opcount>1){
			opseq = $('#prodSelect option:selected').attr('name');
		}
		
		if(opseq == 0 || opseq==null){
			alert('옵션을 선택해주세요.');			
		}else if(opseq == -99){
			alert('품절 상품은 선택 불가합니다.');
		}else{
			if($(this).attr('id') == 'orderBasket'){
				if(user == null || user == ''){
					alert('장바구니는 로그인 후 이용이 가능합니다.');
					window.location.href="${pageContext.request.contextPath}/login.do";
				}else{
					let prodCount = $('#opCount').val();
					$.ajax({
						url:"${pageContext.request.contextPath}/member/insertBasket.js",
						type:"post",
						dataType:"json",
						data:{"prodSeq":prodseq,"opSeq":opseq,"count":prodCount},
						success:function(e){
							if(e == '1'){
								if(confirm('장바구니의 저장하였습니다. \n 장바구니로 이동하시겠습니까?')){
									window.location.href="${pageContext.request.contextPath}/member/basket.do";
								}
							}else if(e == '98'){
								window.location.href = "${pageContext.request.contextPath}/cookieLogout.do";
							}else if(e == '99'){
								window.location.href = "${pageContext.request.contextPath}/cookiedelete.do";
							}
						},
						error:function(e){
							console.log(e);
						}
					})	
				}
			}else if($(this).attr('id') == 'orderBuy'){
				if(user == null || user == ''){
					alert('구매는 로그인 후 이용이 가능합니다.');
					window.location.href="${pageContext.request.contextPath}/login.do";
				}else{
					let prodCount = $('#opCount').val();
					$.ajax({
						url:"${pageContext.request.contextPath}/member/insertBasket.js",
						type:"post",
						dataType:"json",
						data:{"prodSeq":prodseq,"opSeq":opseq,"count":prodCount},
						success:function(e){
							if(e == 1){
								let newForm = $('<form></form>');
								newForm.attr("method","post");
								newForm.attr("action","${pageContext.request.contextPath}/member/orderBuy.do");
								newForm.append($('<input/>',{type:'hidden',name:'prodLst[]',value: prodseq}));
								newForm.appendTo('body');
								newForm.submit();
							}else if(e == '98'){
								window.location.href = "${pageContext.request.contextPath}/cookieLogout.do";
							}else if(e == '99'){
								window.location.href = "${pageContext.request.contextPath}/cookiedelete.do";
							}
						},
						error:function(e){
							console.log(e);
						}
					})	
				}
			}
		}
		
	})
	
	$('.countBtn').click(function(){
	let opseq = 0;
		
		if(opcount<=1){
			opseq = $('#singleOp').attr('value');
		}else if(opcount>1){
			opseq = $('#prodSelect option:selected').attr('name');
		}
		if(opseq == 0 || opseq==null){
			alert('옵션을 선택해주세요.');			
		}else{
			if($(this).text() == 'add'){
				var c = $('#opCount').val();
				c++;
				if(c >= 20){
					alert('수량은 20 이상으로 올라갈 수 없습니다.');
				}else{
					$('#opCount').val(c);
					let newprice = orgprice*c;
					newprice = newprice.toLocaleString('ko-KR');
					$('#changePrice').text(newprice);
				}
			}else if($(this).text() == 'remove'){
				var c = $('#opCount').val();
				c--;
				if(c <= 0){
					alert('수량은 0 이하로 내려갈 수 없습니다.');
				}else{
					$('#opCount').val(c);
					let newprice = orgprice*c;
					newprice = newprice.toLocaleString('ko-KR');
					$('#changePrice').text(newprice);
				}
			}
		}
	})
</script>