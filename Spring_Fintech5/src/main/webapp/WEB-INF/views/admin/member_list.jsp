<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="${pageContext.request.contextPath }/resources/css/default.css" rel="stylesheet" type="text/css">
</head>
<body>
	<header>
		<!-- Login, Join 링크 등 표시 영역 -->
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<article>
		<h1>회원 목록</h1>
		<%-- 테이블에 번호, 이름, 아이디, 이메일, 가입일 출력 --%>
		<table border="1">
			<tr>
				<th>번호</th>
				<th>이름</th>
				<th>아이디</th>
				<th>E-Mail</th>
				<th>가입일</th>
				<th></th>
			</tr>
			<%-- <c:forEach> 태그로 memberList 객체 반복 --%>
			<c:forEach var="member" items="${memberList }">
				<tr>
					<td>${member.idx }</td>
					<td>${member.name }</td>
					<td>${member.id }</td>
					<td>${member.email }</td>
					<td>${member.hire_date }</td>
					<td>
						<%-- 상세정보 버튼 클릭 시 MemberInfo.me 서블릿 요청(파라미터로 아이디 전달) --%>
						<input type="button" value="상세정보" 
							onclick="location.href = 'MemberInfo?id=${member.id }'">
					</td>
				</tr>
				
			</c:forEach>
		</table>
	</article>
</body>
</html>













