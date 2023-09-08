<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="signUp-wrap">
	<form id="signUp-form" action="${pageContext.request.contextPath}/saveMemeber.do" method="post">
		<table align="center">
			<tr>
				<td><input type="text" name="id" id="formid" maxlength="15" placeholder="아이디"/></td>
			</tr>
			<tr>
				<td><input type="password" name="pass" id="formpass" maxlength="15" placeholder="비밀번호"/></td>
			</tr>
			<tr>
				<td><input type="password" name="pass2" id="formpass2" maxlength="15" placeholder="비밀번호 확인"/></td>
			</tr>
			<tr>
				<td><input type="text" name="name" id="formname" maxlength="15" placeholder="이름"/></td>
			</tr>
			<tr>
				<td><input type="text" name="phone" id="formphone" maxlength="11"  placeholder="휴대폰 번호(-제외)"/></td>
			</tr>
			<tr>
				<td><input type="text" id="addressCode" name="addressCode" class="address" placeholder="우편번호" readonly/></td>
			</tr>
			<tr>
				<td colspan="2"><input type="text" id="simpleAddress" class="address" name="simpleAddress" placeholder="주소" readonly/></td>
			</tr>
			<tr>
				<td><input type="text" id="detailAddress" name="detailAddress" placeholder="상세 주소" /></td><td><input type="text" id="extraAddress" class="address" name="extraAddress" placeholder="참조" readonly/></td>
			</tr>
			<tr>
				<td align="center" colspan="2"><input type="button" id="formBtn" value="회원가입"/></td>
			</tr>
		</table>
	</form>
</div>
<script>
	$('.address').click(function(){
		if($('#simpleAddress').val() == ''){
			addressModal();
		}else{
			if(confirm('주소를 수정하시겠습니까?')){
				addressModal();
			}else{
				
			}
		}
	});
	
	$('input[name=id],input[name=pass],input[name=pass2],input[name=name],input[name=phone]').focusout(function(e){
		if($(e.target).val().includes(" ")){
			alert($(e.target).attr("placeholder")+"은/는"+"스페이스바를 사용할 수 없습니다.");
			$(e.target).val($(e.target).val().replaceAll(" ",""));
		}
		
		if(byteCheck($(e.target).val()) > $(e.target).attr('maxlength')){
			alert($(e.target).attr("placeholder")+"은/는"+$(e.target).attr('maxlength')+'Byte를 넘길 수 없습니다.');
			let res = $(e.target).attr('maxlength') / 3;
			$(e.target).val($(e.target).val().substr(0,res));
		}
		
		if($(e.target).attr('id') == 'formpass' || $(e.target).attr('id') == 'formpass2'){
			let exp = /[ㄱ-ㅎㅏ-ㅣ가-힣]/g;
			if(exp.test($(e.target).val())){
				$(e.target).val($(e.target).val().replaceAll( /[ㄱ-ㅎㅏ-ㅣ가-힣]/g,""));
				alert('비밀번호는 한글을 사용 할 수 없습니다.');
			}
		}
		if($(e.target).attr('id') == 'formphone'){
			let exp =  /^[0-9]+$/; 
			console.log(exp.test($(e.target).val()));
			if(!exp.test($(e.target).val()) && $(e.target).val() != ""){
				$(e.target).val($(e.target).val().substr(0,0));
				alert('전화번호는 숫자만 사용 할 수 있습니다.');
			}
		}
	});
	
	
	$('#formBtn').click(function(){
		if($('#formid').val() == ''){
			alert('아이디를 입력해주세요.');
		}else if($('#formpass').val() == ''){
			alert('비밀번호를 입력해주세요.');
		}else if($('#formname').val() == ''){
			alert('이름을 입력해주세요.');
		}else if($('#formphone').val() == '' || $('#formphone').val().length<11){
			alert('휴대폰번호를 입력해주세요.');
		}else if($('#simpleAddress').val() == ''){
			alert('주소를 입력해주세요.');
		}else{
			if($('#formpass').val() != $('#formpass2').val()){
				alert('비밀번호가 상이합니다. 다시 확인해주세요.');
			}else if($('#detailAddress').val()==''){
				if(confirm('상세 주소를 입력하지 않고 진행하시겠습니까?')){
					duplicateCheck();
					//$('#signUp-form').submit();
				}else{
					
				}
			}else{
				duplicateCheck();
				//$('#signUp-form').submit();
			}
		}
		
	})

function duplicateCheck(){
	let id = $('#formid').val();
	let phonenum = $('#formphone').val();
	
	$.ajax({
		url:"${pageContext.request.contextPath}/duplicate.js",
		type:"post",
		dataType:"json",
		data:{"id":id,"phone":phonenum},
		success:function(e){
			if(e == '1'){
				alert('사용중인 아이디 입니다.');
			}else if(e == '2'){
				alert('사용중인 휴대폰 번호 입니다.');
			}else{
				$('#signUp-form').submit();
			}
		}
	});
}
	
	
	
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
	
	
	
function checkText(e){
	const regExp = /[^0-9a-zA-Z]/g; // 숫자와 영문자만 허용
	//const regExp = /[^ㄱ-ㅎ|가-힣]/g; // 한글만 허용
	const del = event.target;
	if (regExp.test(del.value)) {
	  del.value = del.value.replace(regExp, '');
	}	
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
              // 커서를 상세주소 필드로 이동한다.
              document.getElementById("detailAddress").focus();
          }
      }).open();
};
</script>