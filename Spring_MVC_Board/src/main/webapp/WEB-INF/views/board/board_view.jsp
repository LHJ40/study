<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%-- JSTL 의 함수를 사용하기 위해 functions 라이브러리 추가 --%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MVC 게시판</title>
<!-- 외부 CSS 파일 연결하기 -->
<link href="${pageContext.request.contextPath }/resources/css/default.css" rel="stylesheet" type="text/css">
<style type="text/css">
	#articleForm {
		width: 500px;
		height: 550px;
		border: 1px solid red;
		margin: auto;
	}
	
	h2 {
		text-align: center;
	}
	
	table {
		border: 1px solid black;
		border-collapse: collapse; 
	 	width: 500px;
	}
	
	th {
		text-align: center;
	}
	
	td {
		width: 150px;
		text-align: center;
	}
	
	#basicInfoArea {
		height: 100px;
		text-align: center;
	}
	
	#articleContentArea {
		background: orange;
		margin-top: 20px;
		height: 350px;
		text-align: center;
		overflow: auto;
		white-space: pre-line;
	}
	
	#commandList {
		margin: auto;
		width: 500px;
		text-align: center;
	}
</style>
<script>
	function confirmDelete() {
		let isDelete = confirm("정말 삭제하시겠습니까?");
		
		// isDelete 가 true 일 때 BoardDelete 서블릿 요청
		if(isDelete) {
			location.href='BoardDelete?board_num=${board.board_num}&pageNum=${param.pageNum}';
		}
	}
</script> 
</head>
<body>
	<header>
		<!-- Login, Join 링크 표시 영역 -->
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<!-- 게시판 상세내용 보기 -->
	<section id="articleForm">
		<h2>글 상세내용 보기</h2>
		<section id="basicInfoArea">
			<table border="1">
				<tr>
					<th width="70">제 목</th>
					<td colspan="3" >${board.board_subject }</td>
				</tr>
				<tr>
					<th width="70">작성자</th>
					<td>${board.board_name }</td>
					<th width="70">작성일</th>
					<td><fmt:formatDate value="${board.board_date }" pattern="yyyy-MM-dd HH:mm" /></td>
				</tr>
				<tr>
					<th width="70">첨부파일</th>
					<%-- 
					첨부파일 다운로드를 위해 하이퍼링크 생성
					=> download 속성 지정 시 다운로드 가능
					   (단, 다운로드 시 파일명 변경하여 다운하려면 download="변경할 파일명" 형식으로 지정
					------------------------------------------------------
					[ 업로드 된 파일명에서 실제 파일명 추출하기 ]
					- JSTL 의 함수를 활용(JSTL functions 라이브러리 추가 필요)
					- 업로드 된 파일명 : /날짜서브폴더명/UUID_실제파일명.확장자
					  => _ 기호를 구분자로 지정하여 뒷부분을 추출
					- split() 함수 활용(자바와 거의 동일함)
					  => ${fn:split(원본데이터, "구분자")}
					  => 분리된 문자열은 배열로 관리되므로 c:set 태그를 통해 변수에 저장하거나
					     배열 그대로 직접 접근도 가능함(배열[인덱스] 형식)
					  => _ 기호를 기준으로 분리한 후 1번 인덱스에 실제 파일명이 저장됨
					ex) ${fn:split(board.board_file1, "_")[1] }
					--%>
					<td>
						<c:if test="${not empty board.board_file1 }">
							<a href="${pageContext.request.contextPath }/resources/upload/${board.board_file1 }" download="${fn:split(board.board_file1, '_')[1] }">
								${fn:split(board.board_file1, '_')[1] }
							</a>
						</c:if>
						<c:if test="${not empty board.board_file2 }">
							<br>
							<a href="${pageContext.request.contextPath }/resources/upload/${board.board_file2 }" download="${fn:split(board.board_file2, '_')[1] }">
								${fn:split(board.board_file2, '_')[1] }
							</a>
						</c:if>
						<c:if test="${not empty board.board_file3 }">
							<br>
							<a href="${pageContext.request.contextPath }/resources/upload/${board.board_file3 }" download="${fn:split(board.board_file3, '_')[1] }">
								${fn:split(board.board_file3, '_')[1] }
							</a>
						</c:if>
					</td>
					<th width="70">조회수</th>
					<td>${board.board_readcount }</td>
				</tr>
			</table>
		</section>
		<section id="articleContentArea">
			${board.board_content }
		</section>
	</section>
	<section id="commandList">
		<%-- 로그인 한 회원에게만 답변, 수정, 삭제 버튼 표시하고, 목록 버튼은 모두에게 표시 --%>
		<c:if test="${not empty sessionScope.sId }">
			<input type="button" value="답변" onclick="location.href='BoardReplyForm?board_num=${board.board_num}&pageNum=${param.pageNum}'">
			<input type="button" value="수정" onclick="location.href='BoardModifyForm?board_num=${board.board_num}&pageNum=${param.pageNum}'">
			<%-- 삭제 버튼 클릭 시 "삭제하시겠습니까?" 메세지 출력 후 확인 버튼 누르면 삭제 서블릿 요청 --%>
			<input type="button" value="삭제" onclick="confirmDelete()">
		</c:if>
		<input type="button" value="목록" onclick="location.href='BoardList?pageNum=${param.pageNum}'">
	</section>
</body>
</html>
















