<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="memberInfo-wrap">
	<table align="center" >
		<tr>
			<td>아이디</td>
			<td><input type="text" name="id" readonly value="<c:out value='${member.id}'/>"/></td>
		<tr>
		<tr>
			<td>비밀번호</td>
			<td><input type="text" readonly></td>
			<td><input type="button" name="passB" value="수정"/>
		<tr>
		<tr>
			<td>이름</td>
			<td><input type="text" readonly value="<c:out value='${member.name}'/>"></td>
			<td><input type="button" name="nameB" value="수정"/>
		<tr>
		<tr>
			<td>휴대폰 번호</td>
			<td><input type="text" readonly value="<c:out value='${member.phone}'/>"></td>
			<td><input type="button" name="phoneB" value="수정"/>
		<tr>
		<tr>
			<td>주소</td>
			<td><input type="text" readonly value="<c:out value='${member.address}'/>"></td>
			<td><input type="button" name="addressB" value="수정"/>
		<tr>
	</table>
	<input type="hidden" id="addressCode"/>
	<input type="hidden" id="simpleAddress"/>
	<input type="hidden" id="extraAddress"/>
</div>
<div id="modal-wrap">
	<table id="modal-table">
		<tr>
			<td><span id="modal-key"></span></td>
		</tr>
		<tr id="hiddentr">
			<td><input id="simpleAdd" type="text" readonly/></td>
		</tr>
		<tr>
			<td><input id="modal-val" type="text"/></td>
		</tr>
		<tr>
			<td><button id="modal-submit">저장</button></td>
		</tr>
	</table>
</div>
<script>
	let text = '';
	let key = '';
	$('input[type=button]').click(function(e){
		$('#modal-val').prop('text','password');
		$('#modal-val').val('');
		
		if($(e.target).attr('name') == 'addressB'){
			text="주소";
			key="address";
			addressModal();
			$('#modal-val').prop('maxlength',13);
		}else{
			$('#modal-wrap').css('display','block');
			
			switch ($(e.target).attr('name')) {
			case 'passB': text='비밀번호'; key='pass'; $('#modal-val').prop('type','password');
						  $('#modal-val').prop('maxlength',15); break;
			case 'nameB': text='이름'; key='name';
						  $('#modal-val').prop('maxlength',15);break;
			case 'phoneB': text='휴대폰 번호'; key='phone'; 
						  $('#modal-val').prop('maxlength',11);break;
			}	
		}
		
		$('#modal-key').text('새로운 '+text);
	})
	
	
	$('#modal-submit').click(function(){
		let reqval = $('#modal-val').val().replaceAll(" ","");
		$('#modal-val').val(reqval);
		console.log(key);
		
		if(key != 'address' && reqval == ''){
			alert('새로운 '+text+'를 입력하세요.');		
		}else{
			if(key == 'address'){
				let code = $('#addressCode').val();
	            let simpleAddress = $('#simpleAddress').val();
	            let extraAddress = $('#extraAddress').val();
		        let detailAddress = reqval;
	            
				let value = code+"/"+simpleAddress+","+detailAddress+extraAddress;
				let bytec = byteCheck(value);
				console.log(bytec);
				if(bytec > 100){
					alert('주소는 100Byte를 넘어갈 수 없습니다.');
					$('#modal-val').val(reqval.substr(0,6))
				}else{
					memberInfoReplace(key,value);
				}
			}else{
				if(key == 'phone'){
					let exp =  /^[0-9]+$/; 
					if(!exp.test(reqval)){
						alert("휴대폰 번호는 숫자만 입력 가능합니다.");
					}else if(reqval.length != 11){
						alert("휴대폰 번호를 모두 입력해주세요.");
					}else{
						duplicateCheck(reqval);
					}
				}else if(key == 'pass'){
					console.log(reqval);
					let exp = /[ㄱ-ㅎㅏ-ㅣ가-힣]/g;
					if(exp.test(reqval)){
						$('#modal-val').val(reqval.replaceAll( /[ㄱ-ㅎㅏ-ㅣ가-힣]/g,""));
						alert('비밀번호는 한글을 사용 할 수 없습니다.');
					}else{
						memberInfoReplace(key,reqval);
					}				
				}else{				
					let bytec = byteCheck(reqval);
					let max = $('#modal-val').attr('maxlength');
					console.log(bytec);
					if(bytec > max){
						alert(text+"은/는 "+max+'Byte를 넘어갈 수 없습니다.');
						$("#modal-val").val(reqval.substr(0,6))
					}else{
						memberInfoReplace(key,reqval);
					}
				}
			}
		}
	})
	
	$(document).click(function(e){
		if($('#modal-wrap').css('display') == 'block'){
			if($(e.target).attr('id') != null && $(e.target).attr('id').includes('modal-wrap')){
				$('#modal-wrap').css('display','none');
				$('#hiddentr').css('display','none');
			}
		}
	})

function memberInfoReplace(k, v){
		$.ajax({
			url:"${pageContext.request.contextPath}/member/memberReplace.js",
			type:"post",
			dataType:"json",
			data:{"key":k,"val":v},
			success:function(e){
				if(e == '0'){
					alert('정상적으로 수정되었습니다.');
					location.reload();
				}else if(e == '98'){
					window.location.href = "${pageContext.request.contextPath}/cookieLogout.do";
				}else if(e == '99'){
					window.location.href = "${pageContext.request.contextPath}/cookiedelete.do";
				}else{
					alert('수정의 실패하였습니다.')
				}
			}
		})
		
	}	
	
	
function addressModal(){
	new daum.Postcode({
          oncomplete: function(data) {
              // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

              // 각 주소의 노출 규칙에 따라 주소를 조합한다.
              // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
              var addr = ''; // 주소 변수
              var extraAddr = ''; // 참고항목 변수

              //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
              if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                  addr = data.roadAddress;
              } else { // 사용자가 지번 주소를 선택했을 경우(J)
                  addr = data.jibunAddress;
              }

              // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
              if(data.userSelectedType === 'R'){
                  // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                  // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                  if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                      extraAddr += data.bname;
                  }
                  // 건물명이 있고, 공동주택일 경우 추가한다.
                  if(data.buildingName !== '' && data.apartment === 'Y'){
                      extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                  }
                  // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                  if(extraAddr !== ''){
                      extraAddr = ' (' + extraAddr + ')';
                  }
                  // 조합된 참고항목을 해당 필드에 넣는다.
                  document.getElementById("extraAddress").value = extraAddr;
              
              } else {
                  document.getElementById("extraAddress").value = '';
              }

              // 우편번호와 주소 정보를 해당 필드에 넣는다.
              document.getElementById('addressCode').value = data.zonecode;
              document.getElementById("simpleAddress").value = addr;
          },
		
		onclose: function(state) {
        //state는 우편번호 찾기 화면이 어떻게 닫혔는지에 대한 상태 변수 이며, 상세 설명은 아래 목록에서 확인하실 수 있습니다.
        if(state === 'FORCE_CLOSE'){
            //사용자가 브라우저 닫기 버튼을 통해 팝업창을 닫았을 경우, 실행될 코드를 작성하는 부분입니다.

        } else if(state === 'COMPLETE_CLOSE'){
            //사용자가 검색결과를 선택하여 팝업창이 닫혔을 경우, 실행될 코드를 작성하는 부분입니다.
            //oncomplete 콜백 함수가 실행 완료된 후에 실행됩니다.
        	$('#modal-wrap').css('display','block');
        	$('#hiddentr').css('display','contents');
        	
            let code = $('#addressCode').val();
            let simpleAddress = $('#simpleAddress').val();
            let extraAddress = $('#extraAddress').val();
            
			$('#simpleAdd').val(code+"/"+simpleAddress+","+extraAddress);
			$('#modal-val').focus();
        }
    }
      }).open();
};

function byteCheck(str){
	var bytelength = 0;
	
	for(var i=0; i<str.length; i++){
		var code = str.charCodeAt(i);
		if(code > 12592){
			bytelength += 3;
		}else{
			bytelength += 1;
		}
	}
	return bytelength;		
}	

function duplicateCheck(val){
	$.ajax({
		url:"${pageContext.request.contextPath}/member/duplicate.js",
		type:"post",
		dataType:"json",
		data:{"phone":val},
		success:function(e){
			if(e == 1){
				alert('사용중인 휴대폰 번호 입니다.');
			}else{
				memberInfoReplace("phone",val);
			}
		}
	});
}
</script>