<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>학생 정보</h1>
	<table border="1">
		<tr>
			<th>번호</th>
			<td>${student.idx }</td>
		</tr>
		<tr>
			<th>이름</th>
			<td>${student.name }</td>
		</tr>
		<tr>
			<th>E-Mail</th>
			<td>${student.email }</td>
		</tr>
		<tr>
			<th>학년</th>
			<td>${student.grade }</td>
		</tr>
	</table>
</body>
</html>