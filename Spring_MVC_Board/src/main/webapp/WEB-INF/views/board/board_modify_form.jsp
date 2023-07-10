<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- JSTL 의 함수를 사용하기 위해 functions 라이브러리 추가 --%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MVC 게시판</title>
<style type="text/css">
	#modifyForm {
		width: 500px;
		height: 450px;
		border: 1px solid red;
		margin: auto;
	}
	
	h1 {
		text-align: center;
	}
	
	table {
		margin: auto;
		width: 450px;
	}
	
	.td_left {
		width: 150px;
		background: orange;
		text-align: center;
	}
	
	.td_right {
		width: 300px;
		background: skyblue;
	}
	
	#commandCell {
		text-align: center;
	}
</style>
</head>
<body>
	<!-- 게시판 글 수정 -->
	<section id="modifyForm">
		<h1>게시판 글 수정</h1>
		<form action="BoardModifyPro" name="boardForm" method="post" enctype="multipart/form-data">
			<%-- 입력받지 않은 글번호, 페이지번호를 hidden 속성으로 전달 --%>
			<input type="hidden" name="board_num" value="${param.board_num }">
			<input type="hidden" name="pageNum" value="${param.pageNum }">
			<table>
				<tr>
					<td class="td_left"><label for="board_name">글쓴이</label></td>
					<td class="td_right"><input type="text" name="board_name" value="${sessionScope.sId }" readonly="readonly" required="required" /></td>
				</tr>
				<!-- 비밀번호 입력기능 삭제 -->
<!-- 			<tr> -->
<!-- 				<td class="td_left"><label for="board_pass">비밀번호</label></td> -->
<!-- 				<td class="td_right"> -->
<!-- 					<input type="password" name="board_pass" required="required" /> -->
<!-- 				</td> -->
<!-- 			</tr> -->
				<tr>
					<td class="td_left"><label for="board_subject">제목</label></td>
					<td class="td_right">
						<input type="text" name="board_subject" value="${board.board_subject }" required="required" />
					</td>
				</tr>
				<tr>
					<td class="td_left"><label for="board_content">내용</label></td>
					<td class="td_right">
						<textarea id="board_content" name="board_content" cols="40" rows="15" required="required">${board.board_content }</textarea>
					</td>
				</tr>
				<tr>
					<td class="td_left"><label for="board_file">파일 첨부</label></td>
					<!-- 파일 첨부 형식은 input 태그의 type="file" 속성 사용 -->
					<!-- 최대 3개 파일 업로드 가능(파일 업로드는 선택 사항) -->
					<!-- 스프링 spring-context.xml 파일에서 CommonsMultipartResolver 객체 설정 필수! -->
					<!-- 스프링 VO 파일 정의 시 MultipartFile 타입 파라미터 선언 필수! (파라미터명과 변수명 같아야 함) -->
					<td class="td_right">
						<%-- 파일이 존재할 경우 파일명과 삭제버튼 표시하고, 아니면 파일 등록 버튼 표시 --%>
						<c:choose>
							<c:when test="${empty board.board_file1 }">
								<input type="file" name="file1" /><br>
							</c:when>
							<c:otherwise>
								${fn:split(board.board_file1, '_')[1] }
								<input type="button" value="삭제">
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${empty board.board_file2 }">
								<input type="file" name="file2" /><br>
							</c:when>
							<c:otherwise>
								${fn:split(board.board_file2, '_')[1] }
								<input type="button" value="삭제">
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${empty board.board_file3 }">
								<input type="file" name="file3" /><br>
							</c:when>
							<c:otherwise>
								${fn:split(board.board_file3, '_')[1] }
								<input type="button" value="삭제">
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
			<section id="commandCell">
				<input type="submit" value="수정">&nbsp;&nbsp;
				<input type="reset" value="다시쓰기">&nbsp;&nbsp;
				<input type="button" value="취소" onclick="history.back()">
			</section>
		</form>
	</section>
</body>
</html>








