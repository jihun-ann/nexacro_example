<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${empty prodLst}">
	<p style="font-size: 25px; text-align: center; line-height: 100px;">장바구니에 담겨진 상품이 없습니다.</p>
</c:if>
<c:if test="${!empty prodLst}">
<div id="basket-wrap">
		<c:forEach var="prod" items="${prodLst}">
			<div class="prod-wrap" name="${prod.prodSeq}">
				<input class="prodCheckbox" type="checkbox" value="${prod.prodSeq}"/>
				<img class="prodImg" src="http://192.168.0.16:8081/resources/image/${prod.prodImg}">
				<div class="prodInfo" name="<c:out value="${prod.prodSeq}"/>">
					<p class="prodName"><c:out value="${prod.prodName}"/></p>
					<c:forEach var="basket" items="${basketLst}">
						<c:if test="${prod.prodSeq == basket.productseq}">
							<p class="prodOption" name="<c:out value="${basket.optionseq}"/>" value="<c:out value="${basket.basketseq}"/>">ㄴ옵션 : <c:out value="${basket.optionname}"/><span class="hasC" value="<c:out value="${basket.ordercount}"/>"> /주문수량 : <c:out value="${basket.ordercount}"/></span></p>
						</c:if>
					</c:forEach>
					<span class="optionRe" value="<c:out value="${basket.basketseq}"/>">수정</span>
					<c:forEach var="basket" items="${basketLst}">
						<c:if test="${prod.prodSeq == basket.productseq}">
							<input type="hidden" class="eachPrice" name="<c:out value="${prod.prodSeq}"/>" value="<c:out value="${basket.productprice * basket.ordercount}"/>"/>
						</c:if>
					</c:forEach>
					<p class="prodPrice">가격: </p>
				</div>
			</div>
		</c:forEach>
</div>
<div id="basketBtn-wrap">
	<button class="basketBtn selDel">상품 삭제</button>
	<button class="basketBtn selBuy">선택 구매</button>
	<button class="basketBtn allBuy">전체 구매</button>
</div>
</c:if>
<div id="modal-wrap">
	<div id="modal">
		<span id="modal-prodName">상품명</span>
		<div id="select-wrap"></div>
		<div id="hasOption-wrap"></div>
		<div id="addOption-wrap"></div>
		<div id="modalBtn-wrap">
			<button id="modal-saveBtn">저장</button>
			<button id="modal-falseBtn">취소</button>
		</div>
	</div>
</div>
<script>
	let prodLst = $('.prod-wrap').length;
	$(document).ready(function(){
		
		for(var i=0; i<prodLst; i++){
			var result = 0;
			for(var j=0; j<$('.eachPrice').length; j++){
				if($('.eachPrice:eq('+j+')').attr('name') == $('.prod-wrap:eq('+i+')').attr('name')){
					result += Number($('.eachPrice:eq('+j+')').val());
				}
			}
			result = result.toLocaleString('ko-KR');
			$('.prodPrice:eq('+i+')').text('가격 : '+result);
		}
		
		$(document).change(function(e){
			if($(e.target).attr('id') == 'modal-select'){
				let seletval = $('#modal-select :selected').attr('name');
				let seletpric = $('#modal-select :selected').attr('value');
				let selettext = $('#modal-select :selected').text();
				let res = 0;
				for(var i=0; i<$('.hasOption').length; i++){
					if($('.hasOption:eq('+i+')').attr('name') == seletval){
						$('#modal-select').val(0).prop('selected',true);
						alert('이미 선택된 옵션입니다.');
						res++;
					}else if(seletpric == 0){
						res++;
					}
				}
				for(var j=0; j<$('.addOption').length; j++){
					if($('.addOption:eq('+j+')').attr('value') == seletval){
						$('#modal-select').val(0).prop('selected',true);
						alert('이미 선택된 옵션입니다.');
						res++;
					}
				}
				
				if(res == 0){
					let newDiv = $('<div>',{class:'addOption',value : seletval, name:seletpric},'</div>');
					newDiv.append($('<span>',{text:'옵션 : '+selettext},'</span>'));
					newDiv.append($('<input/>',{class: 'addOpcount', type: 'number', value: 1}));
					newDiv.append($('<span>',{class: 'addOpPrice', value: seletpric,text: seletpric+'원'},'</span>'));
					newDiv.append($('<button>',{class:'addOpXBtn',text: 'X'},'</button>'));
					$('#addOption-wrap').append(newDiv);
					}
				$('#modal-select').val(0).prop('selected',true);
				}else if($(e.target).hasClass('hasOpcount')){
					if($(e.target).val() >=20){
						alert('상품의 갯수는 20 이상으로 올라 갈 수 없습니다.');
						$(e.target).val(19);
					}else if($(e.target).val() <= 0){
						alert('상품의 갯수는 0이하로 내려갈 수 없습니다.');
						$(e.target).val(1);
					}else{
					}
						let rePrice = $(e.target).parent().children('.hasOpPrice').attr('value')*$(e.target).val();
						rePrice = rePrice.toLocaleString('ko-KR');
						$(e.target).parent().children('.hasOpPrice').text(rePrice +'원');
				}else if($(e.target).hasClass('addOpcount')){
					if($(e.target).val() >=20){
						alert('상품의 갯수는 20 이상으로 올라 갈 수 없습니다.');
						$(e.target).val(19);
					}else if($(e.target).val() <= 0){
						alert('상품의 갯수는 0이하로 내려갈 수 없습니다.');
						$(e.target).val(1);
					}else{
					}
						let rePrice = $(e.target).parent().children('.addOpPrice').attr('value')*$(e.target).val();
						rePrice = rePrice.toLocaleString('ko-KR');
						$(e.target).parent().children('.addOpPrice').text(rePrice +'원');
				}
		})
	})
	
	$('.basketBtn').click(function(){
		let result = new Array;
		let svcURL = "${pageContext.request.contextPath}/member/orderBuy.do";
		
		if($(this).hasClass("allBuy")){
			for(var i=0; i<prodLst; i++){
				result.push(Number($('.prod-wrap:eq('+i+')').attr('name')));
			}
		}else{
			for(var i=0; i<prodLst; i++){
				if($('.prodCheckbox:eq('+i+')').is(":checked")){
				result.push(Number($('.prodCheckbox:eq('+i+')').val()));
				}
			}
		}
		if(result.length == 0){
			alert('상품을 선택해주세요.');
		}else{
			if($(this).hasClass("selDel")){
				svcURL = "${pageContext.request.contextPath}/member/orderDelete.do";
			}
			
			let newForm = $('<form></form>');
			newForm.attr("method","post");
			newForm.attr("action",svcURL);
			
			for (var i=0; i<result.length; i++) {
				newForm.append($('<input/>',{type:'hidden',name:'prodLst[]',value: result[i]}));
				console.log(result[i]);
			}
			newForm.appendTo('body');
			
			newForm.submit();
		}
	})
	
	var bseqLst = new Array();
	$(document).click(function(e){
		if($(e.target).attr('id') == 'modal-wrap' || $(e.target).attr('id') == 'modal-falseBtn'){
			$('#modal-wrap').css({'display':'none'});
			
			$('#hasOption-wrap').empty();
			$('#addOption-wrap').empty();
			bseqLst = new Array();
			
		}else if($(e.target).hasClass('hasOpXBtn')){
			let basketseq = $(e.target).parent().attr('value');
			$(e.target).parent().remove();
			bseqLst.push({'deleteSeq':basketseq});
			
		}else if($(e.target).hasClass('addOpXBtn')){
			$(e.target).parent().remove();
		}else if($(e.target).attr('id')=='modal-saveBtn'){
			let prodSeq = $(e.target).parent().parent().attr('name');
			console.log(prodSeq);
			let addLst = new Array();
			for(var i=0; i<$('.addOption').length; i++){
				let addOptionSeq = $('.addOption:eq('+i+')').attr('value');
				let addOptionCount =$('.addOption:eq('+i+')').children('.addOpcount').val();
				addLst.push({'addSeq':addOptionSeq,'addCount':addOptionCount,'prodSeq':prodSeq});
			}
			
			let hasLst = new Array();
			for(var i=0; i<$('.hasOption').length; i++){
				let hasOptionSeq = $('.hasOption:eq('+i+')').attr('value');
				let hasOptionCount = $('.hasOption:eq('+i+')').children('.hasOpcount').val();
				let hasOption = new Map();
				hasOption.set('hasSeq',hasOptionSeq);
				hasOption.set('hasCount',hasOptionCount);
				hasLst.push({'hasSeq':hasOptionSeq,'hasCount':hasOptionCount});
			}	
			
			if(addLst.length == 0 && hasLst.length == 0){
				alert('옵션을 선택 후 저장하십시오.');
			}else{
				if(bseqLst.length == 0){
					bseqLst.push({'deleteSeq':-1});
				}
				if(addLst.length == 0){
					addLst.push({'addSeq' : -1});
				}
				if(hasLst.length == 0){
					hasLst.push({'hasSeq':-1});
				}
			} 
			
			
			let reqLst = [];
			reqLst.push({"bseqLst":bseqLst,"hasLst":hasLst,"addLst":addLst});
			
			$.ajax({
				url:"${pageContext.request.contextPath}/member/updateBasket.js",
				type:"post",
				data: {"data" : JSON.stringify(reqLst)},
				success:function(e){
					if(e == '1'){
						alert('옵션이 정상적으로 수정되었습니다.');
						window.location.href = "${pageContext.request.contextPath}/member/basket.do";
					}else if(e == '98'){
						window.location.href = "${pageContext.request.contextPath}/cookieLogout.do";
					}else if(e == '99'){
						window.location.href = "${pageContext.request.contextPath}/cookiedelete.do";
					}else{
						alert('수정의 실패하였습니다.')
					}
				},error:function(e){
					console.log(e);
				}
			})
		}
	})
		
		
	$('.optionRe').click(function(e){
		let prodname = $(this).parent().children('.prodName').text();
		let prodSeq = $(this).parent().attr('name');
		let hasOptionCount = $(e.target).parent().children('.prodOption');
		$('#modal').attr('name',prodSeq);
		
		$.ajax({
			url:"${pageContext.request.contextPath}/member/modalProdInfo.js",
			type:"post",
			data:{"prodSeq":prodSeq},
			success:function(e){
				
				let map = e
					map = JSON.parse(map);
				
				$('#modal-wrap').css({'display':'block'});
				$('#modal-prodName').text(map.prodname);
				
				let newSelect = $('<select>',{id:'modal-select'},'</select>');
					newSelect.append($('<option>',{name:0, value:0, text:'옵션 리스트', selected:true},'</option>'));
				for(var i=0; i<map.option.length; i++){
					if(map.option[i].productcount <=0 ){
						newSelect.append($('<option>',{name:-99, value:-99, text:map.option[i].optionname, disabled:true},'</option>'));
					}else{
						newSelect.append($('<option>',{name:map.option[i].optionseq, value:map.option[i].optionprice, text:map.option[i].optionname},'</option>'));
					}
					
					for(var j=0; j<hasOptionCount.length; j++){
						let hasop = $(hasOptionCount[j]).attr('name');
						if(map.option[i].optionseq == hasop){
							let price = map.option[i].optionprice * $(hasOptionCount[j]).children('.hasC').attr('value');
								price = price.toLocaleString('ko-KR');
							let newDiv = $('<div>',{class:'hasOption',value:$(hasOptionCount[j]).attr('value'), name:$(hasOptionCount[j]).attr('name')},'</div>');
							newDiv.append($('<span>',{text:'옵션 : '+map.option[i].optionname},'</span>'));
							newDiv.append($('<input/>',{class: 'hasOpcount',type: 'number', value: $(hasOptionCount[j]).children('.hasC').attr('value'), min:1}));
							newDiv.append($('<span>',{class: 'hasOpPrice', value:map.option[i].optionprice,text: price + '원'},'</span>'));
							newDiv.append($('<button>',{class:'hasOpXBtn',text: 'X'},'</button>'));
							$('#hasOption-wrap').append(newDiv);
						}
					}
				}
				$('#select-wrap').html(newSelect);
				
				
			},error: function(e){
				console.log(e);
			}
		}) 
	})
	
	$(document).keydown(function(e){
		if($(e.target).hasClass('hasOpcount') || $(e.target).hasClass('addOpcount')){
			if(e.key == '-' || e.key == '+' || e.key == '.' || e.ctrlKey){
				return false;
			}else{
				let text = $(e.target).val();
					text = text.replaceAll('-','');
					text = text.replaceAll('+','');
					text = text.replaceAll('.','');
				$(e.target).val(text);
			}
		}
	})
</script>