<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- 외부 CSS 파일 연결하기 -->
<link href="${pageContext.request.contextPath }/resources/css/default.css" rel="stylesheet" type="text/css">
</head>
<body>
	<header>
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<article>
		<h1>로그인</h1>
		<form action="MemberLoginPro" method="post" name="fr">
			<table border="1">
				<tr>
					<th>아이디</th>
					<td>
						<%-- 쿠키가 존재할 경우 아이디 입력란에 표시(없으면 널스트링("") 표시) --%>
						<input type="text" name="id" value="${cookie.REMEMBER_ID.value }" required="required">
					</td>
				</tr>
				<tr>
					<th>비밀번호</th>
					<td>
						<input type="password" name="passwd" required="required">
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<%-- 아이디 저장된 쿠키가 존재할 경우 아이디 저장 체크박스 체크--%>
						<input type="checkbox" name="rememberId" 
							<c:if test="${not empty cookie.REMEMBER_ID.value }">checked</c:if>
						>아이디 저장
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<input type="submit" value="로그인">
					</td>
				</tr>
			</table>
			<div align="center"><a href="RequestAuthMailForm" >인증 메일이 오지 않을 경우 클릭</a></div>
		</form>
	</article>
</body>
</html>












