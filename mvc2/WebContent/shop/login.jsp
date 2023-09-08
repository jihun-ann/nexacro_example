<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="login-wrap">
	<form id="login-form" action="${pageContext.request.contextPath}/signin.do" method="post">
		<table align="center">
			<tr>
				<td><input id="loginId" type="text" name="username" placeholder="아이디" maxlength="15"/>
			</tr>
			<tr>
				<td><input id="loginPw" type="password" name="password" placeholder="비밀번호" maxlength="15"/>
			</tr>
			<tr>
				<td style="display: flex">
					<div id="idFindBtn-wrap"><span id="idFindBtn">아이디 찾기</span></div>
					<div id="pwFindBtn-wrap"><span id="pwFindBtn">비밀번호 찾기</span></div>
				</td>
			</tr>
			<tr>
				<td align="center">
					<input type="submit" id="loginBtnn" value="로그인"/>
				</td>
			</tr>
		</table>
	</form>
</div>
<div id="modal">
	<div id="pwFindModal">
		<table>
			<tr>
				<td>
					<span>아이디</span>
				</td>
				<td>
					<input id="pwFindId" type="text" maxlength="15"/>
				</td>
			</tr>
			<tr>
				<td>
					<span>휴대폰 번호</span>
				</td>
				<td>
					<input id="pwFindPhone" type="text" maxlength="11"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<button id="pwDuplBtn">확인</button>
					<button id="cancleBtn">취소</button>
				</td>
			</tr>
		</table>
	</div>
	<div id="idFindModal">
		<table>
			<tr>
				<td>
					<span>이름</span>
				</td>
				<td>
					<input id="idFindName" type="text" maxlength="15"/>
				</td>
			</tr>
			<tr>
				<td>
					<span>휴대폰 번호</span>
				</td>
				<td>
					<input id="idFindPhone" type="text" maxlength="11"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<button id="idDuplBtn">확인</button>
					<button id="cancleBtn">취소</button>
				</td>
			</tr>
		</table>
	</div>
</div>

<script>
	$('#pwFindBtn').click(function(){
		$('#modal').css({"display":"block"});
		$('#pwFindModal').css({"display":"block"});
	})
	
	$('#idFindBtn').click(function(){
		$('#modal').css({"display":"block"});
		$('#idFindModal').css({"display":"block"});
	})
	
	$(document).click(function(e){
		if($(e.target).attr('id') == 'modal' ||$(e.target).attr('id') == 'cancleBtn'){
			$('#modal').css({"display":"none"});
			$('#pwFindModal').css({"display":"none"});
			$('#idFindModal').css({"display":"none"});
			$('#pwFindId').val('');
			$('#pwFindPhone').val('');
			$('#idFindName').val('');
			$('#idFindPhone').val('');
		}
		
	})
	
	$('#pwFindPhone, #idFindPhone').focusout(function(e){
		let exp =  /^[0-9]+$/; 
		if(!exp.test($(e.target).val())){
			$(e.target).val($(e.target).val().substr(0,0));
			alert('전화번호는 숫자만 사용 할 수 있습니다.');
		}
	})
	
	$('#pwDuplBtn').click(function(){
		let id = $('#pwFindId').val();
		let phone = $('#pwFindPhone').val();
		
		if(id != null && id != ''){
			id = id.replaceAll(" ","");
			$('#pwFindId').val(id);
		}
		if(phone != null && phone != ''){
			phone = phone.replaceAll(" ","");
			$('#pwFindPhone').val(phone);
		}
		
		if(id == null || id == ''){
			alert('아이디를 입력해주세요.');
		}else if(phone == null || phone == ''){
			alert('휴대폰 번호를 입력해주세요.');
		}else if(phone.length < 11){
			alert('휴대폰 번호 11자리 모두 입력해주세요.');
		}else{
			$.ajax({
				url:"${pageContext.request.contextPath}/pwFind.js",
				type:"post",
				data:{"id":id,"phone":phone},
				success:function(e){
					if(e == '-1'){
						alert('아이디 또는 휴대폰 번호가 상이합니다.');
					}else{
						console.log(e);
						let message = '비밀번호는 ['+e+'] 입니다.'
						alert(message);
					}
			}
		})
	}
})

$('#idDuplBtn').click(function(){
	let name = $('#idFindName').val();
	let phone = $('#idFindPhone').val();
	
	if(name != null && name != ''){
		name = name.replaceAll(" ","");
		$('#idFindName').val(name);
	}
	if(phone != null && phone != ''){
		phone = phone.replaceAll(" ","");
		$('#idFindPhone').val(phone);
	}
	
	if(name == null || name == ''){
		alert('이름을 입력해주세요.');
	}else if(phone == null || phone == ''){
		alert('휴대폰 번호를 입력해주세요.');
	}else if(phone.length < 11){
		alert('휴대폰 번호 11자리 모두 입력해주세요.');
	}else{
		$.ajax({
			url:"${pageContext.request.contextPath}/idFind.js",
			type:"post",
			data:{"name":name,"phone":phone},
			success:function(e){
				console.log(e);
				if(e == '-1'){
					alert('이름 또는 휴대폰 번호가 상이합니다.');
				}else{
					let message = '아이디는 ['+e+'] 입니다.'
						alert(message);
				}
			},
			error: function(e,r,t){
				console.log(e);
				console.log(r);
				console.log(t);
			}
		})
	}
})
</script>