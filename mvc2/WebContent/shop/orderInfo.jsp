<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<div id="order-wrap">
	<div style="text-align: right;">
		<span>결제일 : <c:out value="${orgOrder.date}"/></span>
	</div>
	<div>
		<h1>주문 상품</h1>
	</div>
	<c:forEach var="prod" items="${prodLst}">
		<div class="prod-wrap" name="${prod.prodSeq}">
			<img src="http://192.168.0.16:8081/resources/order/${prod.prodImg}">
			<div class="prodInfo">
				<p class="prodName"><c:out value="${prod.prodName}"/></p>
				<c:forEach var="order" items="${orderLst}">
					<c:if test="${order.productseq == prod.prodSeq}">
						<p class="prodOption" value="${order.price}">ㄴ옵션 : <span class="optionName"><c:out value="${order.optionname}"/></span> / 갯수 : <span class="optionCount"><c:out value="${order.ordercount}"/></span></p>
					</c:if>
				</c:forEach>
				<p class="prodPrice"><c:out value="${prod.prodPrice}"/></p>
			</div>
		</div>
	</c:forEach>
	<div>
		<h1>결제 내역</h1>
	</div>
	<div id="detailOrder">
		<table id="orderTable">
			<tr>
				<td>
					주소
				</td>
				<td>
					<span id="address"><c:out value="${orgOrder.address}"/></span>
				</td>
			</tr>
			<tr>
				<td>
					주문 요청 사항
				</td>
				<td>
					<span id="message"><c:out value="${orgOrder.ordermessage}"/></span>
				</td>
			</tr>
			<tr>
				<td>
					결제일
				</td>
				<td>
					<span id="date" ><c:out value="${orgOrder.date}"/></span>
				</td>
			</tr>
			<tr>
				<td>
					결제 금액
				</td>
				<td>
					<span id="price"><c:out value="${orgOrder.prodprice}"/></span>
				</td>
			</tr>
			<tr>
				<td>
					결제 방법
				</td>
				<td>
					<span id="payment"><c:out value="${orgOrder.payment}"/></span>
				</td>
			</tr>
		</table>
	</div>
</div>

<script>
	$(document).ready(function(){
		let prodCount = $('.prodInfo').length;
		for (var i=0; i<prodCount; i++) {
			let optionCount =  $('.prodInfo:eq('+i+')').children('.prodOption').length;
			let result = 0;
			for(var j=0; j<optionCount; j++){
				let price = Number($('.prodInfo:eq('+i+')').children('.prodOption:eq('+j+')').attr('value'));
				console.log(price);
				result += price;
			}
			result = result.toLocaleString('ko-KR');
			$('.prodPrice:eq('+i+')').text(result+"원");
		}
		
		let price = Number($('#price').text());
			price = price.toLocaleString('ko-KR');
		$('#price').text(price+"원");
		
		
		let pay = $('#payment').text();
		let paykind = pay.substr(0,2);
		
		if(paykind == '카드'){
			let bank = pay.substr(2);
			let text = '';
			switch (bank) {
			case '0': text = '카카오뱅크'; break;
			case '1': text = '신한은행'; break;
			case '2': text = '농협은행'; break;
			case '3': text = '국민은행'; break;
			}
			$('#payment').text(text+' 카드결제');
		}else if(paykind == '이체'){
			let text = pay.substr(2);
			$('#payment').text('계좌이체 / 환불계좌 : '+text);
		}
	})
	
</script>