<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="${pageContext.request.contextPath }/resources/js/jquery-3.7.0.js"></script>
<script type="text/javascript">
	function requestAuthMail() {
		let email = $("#email").val().trim();
// 		alert(email);

		// AJAX 를 활용하여 인증메일 발송 작업 요청
		// 요청 주소 : RequestAuthMailPro
		// 요청 파라미터 : email
		// 응답 데이터 타입 : text
		$.ajax({
			type: "GET",
			url: "RequestAuthMailPro",
			data: {
				email: email
			},
			dataType: "text",
			success: function(result) {
				if(result == "true") {
					alert("인증 메일 발송 완료!");
				} else {
					alert("인증 메일 발송 실패!");
				}
			},
			error: function() {
				alert("인증 메일 발송 요청 실패!");
			}
		});
	}
</script>
</head>
<body>
	<h1>인증 메일 요청</h1>
	<table border="1">
		<tr>
			<th>E-Mail</th>
			<td>
				<input type="text" name="email" id="email" required="required">
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<input type="button" value="인증메일 발송요청" onclick="requestAuthMail()">
			</td>
		</tr>
	</table>
	<div>
		<input type="button" value="홈으로" onclick="location.href='./'">
		<input type="button" value="로그인" onclick="location.href='MemberLoginForm'">
	</div>
</body>
</html>







