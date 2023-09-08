<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="orderBuy-wrap">
	<div><h1>상품 정보</h1></div>
	<div id="prod-wrap">
		<c:forEach var="prod" items="${prodLst}">
			<div class="prodInfo-wrap" name="<c:out value="${prod.prodSeq}"/>">
				<img class="prodImg" src="http://192.168.0.16:8081/resources/image/${prod.prodImg}">
				<div class="prodInfo">
					<p class="prodName"><c:out value="${prod.prodName}" /></p>
					<c:forEach var="bas" items="${basketLst}">
						<c:if test="${bas.productseq == prod.prodSeq}">
							<p class="prodOption" value="${bas.productprice}" name="${bas.optionseq}">ㄴ옵션 : <span class="optionname"><c:out value="${bas.optionname}" /></span> / 갯수 : <span class="optioncount"><c:out value="${bas.ordercount}"/></span></p>
						</c:if>
					</c:forEach>
					<p class="prodPrice">가격 : </p>
				</div>
			</div>
		</c:forEach>
	</div>
	<div id="prodCount-wrap">
		<span id="prodCount">총</span>
	</div>
	<div><h1>배송 정보</h1></div>
	<div id="memberInfo-wrap">
		<p id="name"><c:out value="${member.name}"/></p>
		<p id="phone"><c:out value="${member.phone}"/></p>
		<p id="address"><c:out value="${member.address}"/></p>
		<br/>
		<hr/>
		<span style="font-size:15px;margin: 10px;line-height:30px;">배송 요청사항</span>
		<select id="deliveryOrder">
			<option value="0" selected>배송시 요청 사항(선택사항)</option>
			<option value="1" >배송 전, 연락바랍니다.</option>
			<option value="2" >부재시, 전화 또는 문자 주세요.</option>
			<option value="3" >부재시, 경비실에 맡겨주세요.</option>
			<option value="4" >직접 입력</option>
		</select>
		<input id="deliveryOrderCus" type="text" maxlength="20" placeholder="직접입력"/>
	</div>
	<div><h1>결제 정보</h1></div>
	<div id="price-wrap">
		<p id="allPrice"> 총 결제 금액 :</p>
	</div>
	<div id="paySelect-wrap">
		<div id="menuBtn-wrap">
			<button id="menuBtn-C" class="menuBtn">체크 / 신용카드</button>
			<button id="menuBtn-B" class="menuBtn">은행 계좌이체</button>
		</div>
		<div id="menu-C" class="menu-wrap">
			<c:forEach var="bank" begin="1" end="4">
				<div class="bankBtn" value=${bank}>
					<img class="bankImg" src="http://192.168.0.16:8081/resources/bank/${bank}.png"/>
					<span class="bankName">은행명</span>
				</div>
			</c:forEach>
		</div>
		<div id="menu-B" class="menu-wrap">
			<div style="width:100%; text-align: center; padding: 20px; box-sizing: border-box;">
				<span>계좌이체시 환불에 필요한 계좌번호가 필요합니다.</span><br/>
				<input id="accountBank" type="text" maxlength="12" placeholder="계좌번호"/>
			</div>
		</div>
	</div>
	<div style="text-align: center;">
		<button id="payBtn">결제 하기</button>
	</div>
</div>
<script>

	let allPrice = 0;
	$(document).ready(function(){
		$('#menuBtn-C').css({"background":"cadetblue", "color":"white"});
		$('#menu-C').css({'display':'flex'});
		
		let hascard= '${member.paycard}';
		if(hascard != ''){
			hascard = Number(hascard);
			$('.bankBtn:eq('+hascard+')').click();
		}
		
		let hasaccount= '${member.account}';
		if(hasaccount != ''){
			
		}
		$('#accountBank').val(hasaccount);
		
		
		
		let prodCount = $('.prodInfo-wrap').length;
		for (var i=0; i<prodCount; i++) {
			let reprice = 0;
			let optionCount = $('.prodInfo-wrap:eq('+i+')').children('.prodInfo').children('.prodOption').length;
			
			for(var j=0; j<optionCount; j++){
				let price = $('.prodInfo-wrap:eq('+i+')').children('.prodInfo').children('.prodOption:eq('+j+')').attr('value');
				let count = $('.prodInfo-wrap:eq('+i+')').children('.prodInfo').children('.prodOption:eq('+j+')').children('.optioncount').text();
					count = Number(count);
				
				let result = 0;
				result = Number(price) * Number(count);
				allPrice += result;
				reprice += result;
			}
			$('.prodInfo-wrap:eq('+i+')').children('.prodInfo').children('.prodPrice').text(reprice.toLocaleString('ko-KR') + '원');				
		}
		$('#allPrice').text('총 결제 금액 : '+allPrice.toLocaleString('ko-KR')+'원');	
		$('#prodCount').text('총 : '+prodCount +"건");
		
		for(var i=0; i<$('.bankBtn').length; i++){
			
			let text = '';
			switch (i) {
			case 0: text = '카카오뱅크'; break;
			case 1: text = '신한은행'; break;
			case 2: text = '농협은행'; break;
			case 3: text = '국민은행'; break;
			}
			
			$('.bankBtn:eq('+i+')').children('.bankName').text(text);
		}
		
		let pdlst = $('.prodInfo-wrap').length;
		if(pdlst == null || pdlst == 0){
			window.location.href = "${pageContext.request.contextPath}/home.do";
		}
	});
	
	let menustatus = 0;
	let bankstatus = -1;
	let resmessage = '';
	
	$('.menuBtn').click(function(){
		$('.menuBtn').css({"background":"none", "color":"black"});
		$('.menu-wrap').css({'display':'none'});
		$(this).css({"background":"cadetblue", "color":"white"});
		
		if($(this).attr('id')=='menuBtn-C'){
			$('#menu-C').css({'display':'flex'});
			menustatus = 0;
		}else if($(this).attr('id')=='menuBtn-B'){
			$('#menu-B').css({'display':'flex'});
			menustatus = 1;
		}
	})
	
	$('#deliveryOrder').change(function(){
		if($(this).val() == 4){
			$('#deliveryOrderCus').css({'display':'block'})
		}
	})
	
	$('.bankBtn').click(function(){
		$('.bankBtn').css({"border": "none"});
		$(this).css({"border": "1px solid #cdcdcd"});
		bankstatus = $(this).index();
	})
	
	
	$('#payBtn').click(function(){

		if($('#deliveryOrder option:selected').val() != 0){
			resmessage = $('#deliveryOrder option:selected').text();
		}
		
		if($('#deliveryOrder option:selected').val() == 4){
			let message = $('#deliveryOrderCus').val();
			message = message.trim();
			$('#deliveryOrderCus').val(message);
			resmessage = message;
		}
		
		if($('#deliveryOrder option:selected').val() == 4 && $('#deliveryOrderCus').val() ==''){
			alert('배송 요청 사항을 입력 해주세요.');
		}else{
			if(menustatus == 0){
				if(bankstatus == -1){
					alert('결제 하실 은행을 선택하여 주세요.')
				}else{
					prodHasCount(0);
				}
			}else if(menustatus == 1){
				let accountB = $('#accountBank').val();
				
				if(accountB != null || accountB != ''){
					accountB = accountB.replaceAll(" ","");
					$('#accountBank').val(accountB);
				}
				
				if(accountB == '' || accountB == null || accountB.length < 10){
					alert('계좌번호 10~12자리를 입력해주세요.');
				}else{
					prodHasCount(1);
				}
			}
		}
	})

	function prodHasCount(t){
		let opseqLst = new Array();
		
		for (var i=0; i<$('.prodOption').length; i++) {
			opseqLst.push({"opseq":$('.prodOption:eq('+i+')').attr('name'),"opcount":$('.prodOption:eq('+i+')').children('.optioncount').text()});
		}
		let test = JSON.stringify(opseqLst);
		console.log(test);
		$.ajax({
			url:"${pageContext.request.contextPath}/member/optionCountCheck.js",
			type:"post",
			data:{"data":test},
			success:function(e){
				let result = 'n';
				let obj = JSON.parse(e);
				if(obj.length > 0){
					for (var i=0; i<$('.prodOption').length; i++) {
						for(var j=0; j<obj.length; j++){
							let optionseq = $('.prodOption:eq('+i+')').attr('name');
							if(optionseq == obj[j].soldOpseq){
								let prodname = $('.prodOption:eq('+i+')').parent().children('.prodName').text();
								let optionname = $('.prodOption:eq('+i+')').children('.optionname').text();
								alert('['+prodname+'] 상품의 ['+optionname+']옵션이 품절입니다.');
								result = 'n';
							}else if(optionseq == obj[j].subOpseq){
								let prodname = $('.prodOption:eq('+i+')').parent().children('.prodName').text();
								let optionname = $('.prodOption:eq('+i+')').children('.optionname:eq('+j+')').text();
								alert('['+prodname+'] 상품의 ['+optionname+']옵션 잔여 수량 '+obj[j].overcount+'개를 초과하였습니다. \n 해당 상품의 잔여 수량은 '+obj[j].leftcount+'개 입니다.');
								result = 'n';
							}
						}
					}
				}else{
					result = 'y';
				}
			newformsubmit(t,result);
			}
		})
	}
	
	function newformsubmit(t,c){
		
		if(c == 'n'){
			
		}else if(c == 'y'){
			let newForm = $('<form></form>');
			newForm.attr("method","post");
			newForm.attr("action","${pageContext.request.contextPath}/member/orderPayment.do");
			newForm.append($('<input/>',{type:'hidden',name:'allPrice',value: allPrice}));
			
			if(t == 0){
				newForm.append($('<input/>',{type:'hidden',name:'bankStatus',value: bankstatus}));
			}
			if(t == 1){
				let accountB = $('#accountBank').val();
				newForm.append($('<input/>',{type:'hidden',name:'account',value: accountB}));
			}
			
			for(var i=0; i<$('.prodInfo-wrap').length; i++){
				let prodSeq = $('.prodInfo-wrap:eq('+i+')').attr('name');
				newForm.append($('<input/>',{type:'hidden',name:'prodSeq[]',value: prodSeq}));
			}
			newForm.append($('<input/>',{type:'hidden',name:'message',value: resmessage}));
			newForm.append($('<input/>',{type:'hidden',name:'address',value: $('#address').text()}));
			newForm.appendTo('body');
			newForm.submit();
		}
	}
</script>