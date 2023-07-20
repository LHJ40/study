<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- JSTL 에서 split() 등의 함수 사용을 위해 functions 라이브러리 추가 --%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%-- JSTL 함수 사용 기본 문법 : ${fn:함수명(전달인자...)} --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- 외부 CSS 파일 연결하기 -->
<link href="${pageContext.request.contextPath }/resources/css/default.css" rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath }/resources/js/jquery-3.7.0.js"></script>
<script type="text/javascript">
	$(function() {
		$("#btnAccountAuth").on("click", function() {
			// 새 창에서 사용자 인증 페이지 요청
			let requestUri = "https://testapi.openbanking.or.kr/oauth/2.0/authorize?"
					+ "response_type=code"
					+ "&client_id=4066d795-aa6e-4720-9383-931d1f60d1a9"
					+ "&redirect_uri=http://localhost:8082/fintech/callback"
					+ "&scope=login inquiry transfer"
					+ "&state=12345678901234567890123456789012"
					+ "&auth_type=0";
			window.open(requestUri, "authWindow", "width=600, height=800");
		});
	});
</script>
</head>
<body>
	<header>
		<!-- Login, Join 링크 등 표시 영역 -->
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<article>
		<h1>회원 정보</h1>
		<form action="MemberModify" method="post" name="fr">
			<%-- 회원 정보 수정에 사용될 아이디를 hidden 타입으로 전달(입력박스 표시 후 readonly 도 가능) --%>
			<input type="hidden" name="id" value="${member.id }">
			<table border="1">
				<tr>
					<th>아이디</th>
					<td>${member.id }</td>
				</tr>
				<tr>
					<th>이름</th>
					<td><input type="text" name="name" value="${member.name }"></td>
				</tr>
				<tr>
					<th>현재 비밀번호</th>
					<td>
						<!-- 키 누를때마다 checkPasswd() 함수에 입력받은 패스워드 전달하여 호출 -->
						<input type="password" name="passwd" onkeyup="checkPasswd(this.value)" placeholder="8 ~ 16글자 사이 입력">
						<span id="checkPasswdResult"></span>
					</td>
				</tr>
				<tr>
					<th>변경할 비밀번호</th>
					<td>
						<!-- 키 누를때마다 checkNewPasswd() 함수에 입력받은 패스워드 전달하여 호출 -->
						<input type="password" name="newPasswd" onkeyup="checkNewPasswd(this.value)" placeholder="비밀번호 변경 시 입력">
						<span id="checkNewPasswdResult"></span>
					</td>
				</tr>
				<tr>
					<th>변경할 비밀번호 확인</th>
					<td>
						<!-- 키 누를때마다 checkConfirmNewPasswd() 함수에 입력받은 패스워드 전달하여 호출 -->
						<input type="password" onkeyup="checkConfirmNewPasswd(this.value)" placeholder="비밀번호 변경 시 입력">
						<span id="checkConfirmNewPasswdResult"></span>
					</td>
				</tr>
				<tr>
					<th>E-Mail</th>
					<td>
						<%-- 컨트롤러에서 이메일을 email1, email2 로 분리 후 전달받은 경우 --%>
<%-- 						<input type="text" name="email1" value="${member.email1 }"> @ --%>
<%-- 						<input type="text" name="email2" value="${member.email2 }"> --%>
						<%-- 
						JSTL - functions 라이브러리의 split() 함수를 활용하여 이메일 분리 시
						1. JSTL 의 functions 라이브러리 등록 필요(prefix="fn")
						2. split() 함수를 사용하여 문자열 분리 후 리턴되는 배열 활용
						   => ${fn:split(검색대상, 구분자)} 
						--%>
<%-- 						<input type="text" name="email1" value="${fn:split(member.email, '@')[0]}"> @ --%>
<%-- 						<input type="text" name="email2" value="${fn:split(member.email, '@')[1]}"> --%>
						<%-- 분리 결과를 배열로 저장한 후 별도로 처리하는 경우 => 변수 필요 --%>
						<c:set var="arrEmail" value="${fn:split(member.email, '@')}" />
						<input type="text" name="email1" value="${arrEmail[0]}"> @
						<input type="text" name="email2" value="${arrEmail[1]}">
						
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
							<option value="개발자" <c:if test="${member.job eq '개발자' }">selected</c:if>>개발자</option>
							<option value="DB엔지니어" <c:if test="${member.job eq 'DB엔지니어' }">selected</c:if>>DB엔지니어</option>
							<option value="서버엔지니어" <c:if test="${member.job eq '서버엔지니어' }">selected</c:if>>서버엔지니어</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>성별</th>
					<td>
						<input type="radio" name="gender" value="남" <c:if test="${member.gender eq '남' }">checked</c:if>>남
						<input type="radio" name="gender" value="여" <c:if test="${member.gender eq '여' }">checked</c:if>>여
					</td>
				</tr>
				<tr>
					<th>취미</th>
					<td>
						<%-- 
						JSTL - functions 라이브러리의 contains() 함수를 활용하여 포함된 텍스트 검색 시
						1. JSTL 의 functions 라이브러리 등록 필요(prefix="fn")
						2. contains() 함수를 사용하여 문자열 탐색하여 if문 조건식으로 활용
						   => ${fn:contains(검색대상, 찾을문자열)} 
						--%>
						<input type="checkbox" name="hobby" value="여행" <c:if test="${fn:contains(member.hobby, '여행')}">checked</c:if>>여행
						<input type="checkbox" name="hobby" value="독서" <c:if test="${fn:contains(member.hobby, '독서')}">checked</c:if>>독서
						<input type="checkbox" name="hobby" value="게임" <c:if test="${fn:contains(member.hobby, '게임')}">checked</c:if>>게임
						<!-- 전체선택 체크박스 클릭 시 체크상태(checked 속성의 true 또는 false) 를 함수에 전달 -->
						<input type="checkbox" value="전체선택" onclick="checkAll(this.checked)">전체선택
					</td>
				</tr>
				<tr>
					<th>가입동기</th>
					<td>
						<textarea rows="5" cols="40" name="motivation">${member.motivation }</textarea>
					</td>
				</tr>
				<tr>
					<th>계좌정보</th>
					<td>
						<input type="button" value="계좌인증" id="btnAccountAuth">
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<input type="submit" value="정보수정">
						<input type="button" value="돌아가기" onclick="history.back()">
						<input type="button" value="탈퇴하기" 
								onclick="location.href='MemberCheckoutForm'">
					</td>
				</tr>
			</table>
		</form>
	</article>
</body>
</html>










