<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- 외부 CSS 파일 연결하기 -->
<link href="${pageContext.request.contextPath }/resources/css/default.css" rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath }/resources/js/jquery-3.7.0.js"></script>
<script type="text/javascript">
	function checkDupId() {
		// AJAX 를 통해 아이디 중복 확인 작업 수행
		// => test2_check_id_result.jsp 페이지 요청 후 응답 데이터를 result 로 전달받기
		//    파라미터 : 입력받은 아이디
		$.ajax({
			url: "MemberCheckDupId",
			data: {
				id: $("#id").val()
			},
			success: function(result) {
				// "checkIdResult" span 태그 영역에 결과 표시
// 				$("#checkIdResult").html(result);
				
				// 만약, 응답데이터로 전달받은 데이터를 비교 판별 해야할 경우
				// => 반드시 앞뒤 공백 제거 후 비교 수행해야한다!
				// => jQuery 에서 앞뒤 공백 제거 : $.trim()
				if($.trim(result) == "true") {
					$("#checkIdResult").html("사용 불가능한 아이디");
					$("#checkIdResult").css("color", "red");
				} else {
					$("#checkIdResult").html("사용 가능한 아이디");
					$("#checkIdResult").css("color", "green");
				}
			}
		});
	}
	
	// 문서 로딩 후 아이디 입력 영역에서 포커스가 해제될 때 checkDupId() 함수 호출
	$(function() {
		$("#id").blur(function() {
			checkDupId();
		});
	});
</script>
</head>
<body>
	<header>
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<article>
		<h1>회원 가입</h1>
		<form action="MemberJoinPro" method="post" name="fr">
			<table border="1">
				<tr>
					<th>이름</th>
					<td><input type="text" name="name"></td>
				</tr>
				<tr>
					<th>아이디</th>
					<td>
						<input type="text" name="id" id="id" placeholder="4 ~ 8글자 사이 입력">
						<span id="checkIdResult"></span>
					</td>
				</tr>
				<tr>
					<th>비밀번호</th>
					<td>
						<!-- 키 누를때마다 checkPasswd() 함수에 입력받은 패스워드 전달하여 호출 -->
						<input type="password" name="passwd" onkeyup="checkPasswd(this.value)" placeholder="8 ~ 16글자 사이 입력">
						<span id="checkPasswdResult"></span>
					</td>
				</tr>
				<tr>
					<th>비밀번호확인</th>
					<td>
						<!-- 키 누를때마다 checkConfirmPasswd() 함수에 입력받은 패스워드 전달하여 호출 -->
						<input type="password" name="passwd2" onkeyup="checkConfirmPasswd(this.value)">
						<span id="checkConfirmPasswdResult"></span>
					</td>
				</tr>
				<tr>
					<th>E-Mail</th>
					<td>
						<input type="text" name="email1"> @
						<input type="text" name="email2">
						<select name="emailDomain" onchange="selectDomain(this.value)">
							<option value="">직접입력</option>
							<option value="naver.com">naver.com</option>
							<option value="gmail.com">gmail.com</option>
							<option value="nate.com">nate.com</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>직업</th>
					<td>
						<select name="job">
							<option value="">항목을 선택하세요</option>
							<option value="개발자">개발자</option>
							<option value="DB엔지니어">DB엔지니어</option>
							<option value="서버엔지니어">서버엔지니어</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>성별</th>
					<td>
						<input type="radio" name="gender" value="남">남
						<input type="radio" name="gender" value="여">여
					</td>
				</tr>
				<tr>
					<th>취미</th>
					<td>
						<input type="checkbox" name="hobby" value="여행">여행
						<input type="checkbox" name="hobby" value="독서">독서
						<input type="checkbox" name="hobby" value="게임">게임
						<!-- 전체선택 체크박스 클릭 시 체크상태(checked 속성의 true 또는 false) 를 함수에 전달 -->
						<input type="checkbox" value="전체선택" onclick="checkAll(this.checked)">전체선택
					</td>
				</tr>
				<tr>
					<th>가입동기</th>
					<td>
						<textarea rows="5" cols="40" name="motivation"></textarea>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<input type="submit" value="가입">
						<input type="reset" value="초기화">
						<input type="button" value="돌아가기">
					</td>
				</tr>
			</table>
		</form>
	</article>
</body>
</html>