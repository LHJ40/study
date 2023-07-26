<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MVC 게시판</title>
<!-- 외부 CSS 파일 연결하기 -->
<link href="${pageContext.request.contextPath }/resources/css/default.css" rel="stylesheet" type="text/css">
<style type="text/css">
	#replyForm {
		width: 500px;
		height: 450px;
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
	<header>
		<!-- Login, Join 링크 등 표시 영역 -->
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<!-- 게시판 답글 작성 -->
	<section id="replyForm">
		<h1>게시판 답글 작성</h1>
		<form action="BoardReplyPro" name="boardForm" method="post" enctype="multipart/form-data">
			<%-- 입력받지 않은 정보는 "hidden" 속성으로 전달 --%>
			<%-- 답글 작성에 필요한 정보(글번호, 참조글번호, 들여쓰기레벨, 순서번호)와 페이지번호를 전달 --%>
			<input type="hidden" name="board_num" value="${param.board_num }">
			<input type="hidden" name="pageNum" value="${param.pageNum }">
			<input type="hidden" name="board_re_ref" value="${board.board_re_ref }">
			<input type="hidden" name="board_re_lev" value="${board.board_re_lev }">
			<input type="hidden" name="board_re_seq" value="${board.board_re_seq }">
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
				<tr>
					<td class="td_left"><label for="board_subject">제목</label></td>
					<td class="td_right">
						<input type="text" name="board_subject" value="Re: ${board.board_subject }" required="required" />
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
						<input type="file" name="file1" /><br>
						<input type="file" name="file2" /><br>
						<input type="file" name="file3" /><br>
					</td>
				</tr>
			</table>
			<section id="commandCell">
				<input type="submit" value="답글등록">&nbsp;&nbsp;
				<input type="reset" value="다시쓰기">&nbsp;&nbsp;
				<input type="button" value="취소" onclick="history.back()">
			</section>
		</form>
	</section>
</body>
</html>








