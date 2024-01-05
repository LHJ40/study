<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%@ include file="../inc/top.jsp" %>
	<h1>redirect.jsp</h1>
	<hr>
	<%-- URL 파라미터로 전달받은 name, age 파라미터 출력할 경우 --%>
	<h3>name 파라미터값 : ${param.name }</h3>
	<h3>age 파라미터값 : ${param.age }</h3>
</body>
</html>