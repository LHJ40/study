<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<header>
		<%-- inc/top.jsp 페이지 삽입 --%>
		<%@ include file="../inc/top.jsp" %>
	</header>
	<article>
		<h1>메인 페이지</h1>
		<hr>
		<h3><a href="BoardWriteForm">글쓰기</a></h3>
		<h3><a href="BoardList">글목록</a></h3>
	</article>
</body>
</html>












