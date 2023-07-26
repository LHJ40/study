<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- 외부 CSS 파일 연결하기 -->
<%-- 
WEB-INF 디렉토리는 외부에서 접근이 불가능한 디렉토리이며
스프링의 주요 파일들이 WEB-INF 디렉토리에 위치해 있지만
외부로부터의 접근이 필요한 파일들(ex> css, js, image 등...)은 webapp/resources 디렉토리에 위치해야한다!
또한, 해당 위치를 가리키기 위해 EL 표기법을 통해 pageContext 객체로부터 request 객체의 contextPath 에 접근 필요
ex) ${pageContext.request.contextPath }/resources/css/default.css

이 접근은 servlet-context.xml 파일에 resources 디렉토리를 외부 접근용으로 사용하겠다고 선언되어 있으므로 사용 가능
ex) <resources mapping="/resources/**" location="/resources/" />
--%>
<link href="${pageContext.request.contextPath }/resources/css/default.css" rel="stylesheet" type="text/css">
</head>
<body>
	<header>
		<!-- Login, Join 링크 등 표시 영역 -->
		<jsp:include page="inc/top.jsp"></jsp:include>
	</header>
	<article>
		<!-- 본문 표시 영역 -->
		<h1>MVC 게시판</h1>
		<h3><a href="BoardWriteForm">글쓰기</a></h3>
		<h3><a href="BoardList">글목록</a></h3>
		<hr>
		<h3>최근 게시물</h3>
		<table border="1">
			<tr>
				<th width="300">제목</th>
				<th width="150">작성자</th>
				<th width="150">작성일</th>
			</tr>
			<c:forEach var="board" items="${boardList }">
				<tr>
					<td>
						<a href="BoardDetail?board_num=${board.board_num }">${board.board_subject }</a>
					</td>
					<td>${board.board_name }</td>
					<td><fmt:formatDate value="${board.board_date }" pattern="yy-MM-dd" /></td>
				</tr>
			</c:forEach>
		</table>
	</article>
</body>
</html>








