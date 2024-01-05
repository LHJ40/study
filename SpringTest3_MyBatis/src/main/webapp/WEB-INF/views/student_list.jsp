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
	<h1>학생 리스트</h1>	
	<table border="1">
		<tr>
			<th>번호</th>
			<th>이름</th>
			<th>E-Mail</th>
			<th>학년</th>
		</tr>
		<%-- JSTL 반복문을 통해 studentList 객체를 반복 --%>
		<c:forEach var="student" items="${studentList }">
			<tr>
				<td>${student.idx }</td>
				<td>${student.name }</td>
				<td>${student.email }</td>
				<td>${student.grade }</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>









