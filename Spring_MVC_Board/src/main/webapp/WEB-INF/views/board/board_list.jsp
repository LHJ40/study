<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MVC 게시판</title>
<!-- 외부 CSS 파일 연결하기 -->
<link href="${pageContext.request.contextPath }/resources/css/default.css" rel="stylesheet" type="text/css">
<style type="text/css">
	#listForm {
		width: 1024px;
		max-height: 610px;
		margin: auto;
	}
	
	h2 {
		text-align: center;
	}
	
	table {
		margin: auto;
		width: 1024px;
	}
	
	#tr_top {
		background: orange;
		text-align: center;
	}
	
	table td {
		text-align: center;
	}
	
	#subject {
		text-align: left;
		padding-left: 20px;
	}
	
	#pageList {
		margin: auto;
		width: 1024px;
		text-align: center;
	}
	
	#emptyArea {
		margin: auto;
		width: 1024px;
		text-align: center;
	}
	
	#buttonArea {
		margin: auto;
		width: 1024px;
		text-align: right;
	}
	
	/* 하이퍼링크 밑줄 제거 */
	a {
		text-decoration: none;
	}
	
	/* 제목 좌측 정렬 및 여백 설정 */
	#subject {
		text-align: left;
		margin-left: 20px;
	}
	
</style>
</head>
<body>
	<%-- pageNum 파라미터 가져와서 저장(없을 경우 기본값 1로 설정) --%>
	<c:set var="pageNum" value="1" />
	<c:if test="${not empty param.pageNum }">
		<c:set var="pageNum" value="${param.pageNum }" />
	</c:if>
	
	<header>
		<!-- Login, Join 링크 등 표시 영역 -->
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	
	<!-- 게시판 리스트 -->
	<section id="listForm">
	<h2>게시판 글 목록</h2>
	<section id="buttonArea">
		<form action="BoardList">
			<%-- 검색타입목록, 검색어입력창 추가 --%>
			<select name="searchType" id="searchType">
				<option value="subject" <c:if test="${param.searchType eq 'subject' }">selected</c:if>>제목</option>			
				<option value="content" <c:if test="${param.searchType eq 'content' }">selected</c:if>>내용</option>			
				<option value="subject_content" <c:if test="${param.searchType eq 'subject_content' }">selected</c:if>>제목&내용</option>			
				<option value="name" <c:if test="${param.searchType eq 'name' }">selected</c:if>>작성자</option>			
			</select>
			<input type="text" name="searchKeyword" value="${param.searchKeyword }" id="searchKeyword">
			<input type="submit" value="검색">
			<input type="button" value="글쓰기" onclick="location.href='BoardWriteForm'" />
		</form>
	</section>
	<table>
		<tr id="tr_top">
			<th width="100px">번호</th>
			<th>제목</th>
			<th width="150px">작성자</th>
			<th width="150px">날짜</th>
			<th width="100px">조회수</th>
		</tr>
		<%-- JSTL 과 EL 활용하여 글목록 표시 작업 반복(boardList 객체 활용) --%>
		<c:forEach var="board" items="${boardList }">
			<tr>
				<td>${board.board_num }</td>
				<td id="subject">
					<%-- ================== 답글 관련 처리 ======================== --%>
					<%-- board_re_lev 값이 0 보다 크면 답글이므로 들여쓰기 후 이미지 추가 --%>
					<c:if test="${board.board_re_lev > 0 }">
						<%-- 반복문을 통해 board_re_lev 값만큼 공백(&nbsp;) 추가 --%>
						<c:forEach var="i" begin="1" end="${board.board_re_lev }">
							&nbsp;
						</c:forEach>
						<%-- 답글 제목 표시 전 답글 이미지(re.gif) 표시 --%>
						<img src="${pageContext.request.contextPath }/resources/images/re.gif">
					</c:if>
					<%-- ========================================================== --%>
					<%-- 제목 클릭을 위한 하이퍼링크 설정(BoardDetail.bo) --%>
					<%-- 파라미터 : 글번호(board_num), 페이지번호(pageNum) --%>
					<a href="BoardDetail?board_num=${board.board_num }&pageNum=${pageNum}">${board.board_subject }</a>
				</td>
				<td>${board.board_name }</td>
				<td>
					<%--
					JSTL 의 fmt(format) 라이브러리를 활용하여 날짜 및 시각 표현 형식 변경
					1) fmt:formatDate - Date 타입 날짜 형식 변경
					   <fmt:formatDate value="날짜 및 시각 객체" pattern="표현패턴">
					2) fmt:parseDate - String 타입 날짜 형식 변경
					--%>
					<fmt:formatDate value="${board.board_date }" pattern="yy-MM-dd HH:mm" /> 
				</td>
				<td>${board.board_readcount }</td>
			</tr>
		</c:forEach>
	</table>
	</section>
	<section id="pageList">
		<%--
		현재 페이지 번호(pageNum)가 1보다 클 경우에만 [이전] 버튼 동작
		=> 클릭 시 BoardList.bo 서블릿 요청(파라미터 : 현재 페이지번호 - 1)
		--%>
		<c:choose>
			<c:when test="${pageNum > 1 }">
				<input type="button" value="이전" onclick="location.href='BoardList?pageNum=${pageNum - 1}'">
			</c:when>
			<c:otherwise>
				<input type="button" value="이전" disabled="disabled">
			</c:otherwise>
		</c:choose>

		<%-- 위의 문장을 간소화할 경우(조건에 따라 onclick 속성을 추가) --%>
<%-- 		<input type="button" value="이전" <c:if test="${pageNum > 1 }">onclick="location.href='BoardList.bo?pageNum=${pageNum - 1}'"</c:if>> --%>
		<%-- ===================================================================== --%>
		<%-- 페이지번호 목록은 시작페이지(startPage) 부터 끝페이지(endPage) 까지 표시 --%>
		<c:forEach var="i" begin="${pageInfo.startPage }" end="${pageInfo.endPage }">
			<%-- 각 페이지마다 하이퍼링크 설정(단, 현재 페이지는 하이퍼링크 제거) --%>
			<c:choose>
				<c:when test="${pageNum eq i }">
					<b>${i }</b>
				</c:when>
				<c:otherwise>
					<a href="BoardList?pageNum=${i }">${i }</a>
				</c:otherwise>
			</c:choose>
		</c:forEach>		
		<%-- ===================================================================== --%>
		<%--
		현재 페이지 번호(pageNum)가 최대 페이지 번호(maxPage) 보다 작을 경우에만 [다음] 버튼 동작
		=> 클릭 시 BoardList.bo 서블릿 요청(파라미터 : 현재 페이지번호 + 1)
		--%>
		<c:choose>
			<c:when test="${pageNum < pageInfo.maxPage }">
				<input type="button" value="다음" onclick="location.href='BoardList?pageNum=${pageNum + 1}'">
			</c:when>
			<c:otherwise>
				<input type="button" value="다음" disabled="disabled">
			</c:otherwise>
		</c:choose>
	</section>
</body>
</html>













