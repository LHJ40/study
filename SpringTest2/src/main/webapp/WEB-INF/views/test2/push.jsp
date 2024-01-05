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
	<h1>push.jsp</h1>
	<%-- Dispatch 방식 포워딩 시 함께 전달된 request 객체의 msg 속성값 가져와서 출력 --%>
	<h3>msg 속성값 : <%=request.getAttribute("msg") %></h3>
	<h3>msg 속성값 : ${msg }</h3>
	<hr>
	<h3>test 속성값(객체)의 제목 : ${test.subject }</h3>
	<h3>test 속성값(객체)의 내용 : ${test.content }</h3>
	
</body>
</html>