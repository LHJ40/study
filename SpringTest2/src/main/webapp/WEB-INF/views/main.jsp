<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>main.jsp</h1>	
	<hr>
	<%-- form 태그를 사용하여 submit 버튼("test1(GET)") 클릭 시 GET 방식 "test1" 서블릿 요청 --%>
	<form action="test1" method="get">
		<input type="submit" value="test1(GET)">
	</form>
	<%-- form 태그를 사용하여 submit 버튼("test1(POST)") 클릭 시 POST 방식 "test1" 서블릿 요청 --%>
	<form action="test1" method="post">
		<input type="submit" value="test1(POST)">
	</form>
	<hr>
	<%-- form 태그를 사용하여 submit 버튼("test2(GET)") 클릭 시 GET 방식 "test2" 서블릿 요청 --%>
	<form action="test2" method="get">
		<input type="submit" value="test2(GET)">
	</form>
	<%-- form 태그를 사용하여 submit 버튼("test2(POST)") 클릭 시 POST 방식 "test2" 서블릿 요청 --%>
	<form action="test2" method="post">
		<input type="submit" value="test2(POST)">
	</form>
	
	
</body>
</html>












