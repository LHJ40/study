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
	a {
		text-decoration: none;
	}

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
	
	/* ------- 댓글 영역 ------- */
	#tinyReplyArea {
		width: 500px;
		height: 150px;
		margin: auto;
		margin-top: 20px;
	}
	
	#tinyReplyArea textarea {
		width: 400px;
		height: 50px;
		vertical-align: middle;
		resize: none;
	}
	
	#btnTinyReplySubmit {
		width: 85px;
		height: 55px;
		vertical-align: middle;
	}
	
	#tinyReplyContentArea {
		width: 500px;
		height: 100px;
		font-size: 12px;
	}
	
	#tinyReplyContentArea table, tr, td {
		border: none;
	}
	
	.replyContent {
		width: 320px;
		text-align: left;
	}
	
	.replyWriter {
		width: 90px;
	}
	
	.replyDate {
		width: 90px;
	}
	
	.replyContent img {
		width: 10px;
		height: 10px;
	}
	
	/* 대댓글 */
	#tinyReReplyTextArea {
		width: 350px;
		height: 20px;
		vertical-align: middle;
		resize: none;
	}
	
</style>
<script src="${pageContext.request.contextPath }/resources/js/jquery-3.7.0.js"></script>
<script>
	function confirmDelete() {
		let isDelete = confirm("게시물을 삭제하시겠습니까?");
		
		// isDelete 가 true 일 때 BoardDelete 서블릿 요청
		if(isDelete) {
			location.href='BoardDelete?board_num=${board.board_num}&pageNum=${param.pageNum}';
		}
	}
	
	function confirmReplyDelete(reply_num) {
		let isDelete = confirm("댓글을 삭제하시겠습니까?");
		
		// isDelete 가 true 일 때 AJAX 를 활용하여 BoardTinyReplyDelete 서블릿 요청(댓글번호 전달)
		if(isDelete) {
			$.ajax({
				type: "GET",
				url: "BoardTinyReplyDelete?reply_num=" + reply_num,
				dataType: "text",
				success: function(result) {
					if(result == "true") {
						// 댓글 삭제 성공 시 테이블의 해당 댓글이 존재하는 tr 태그 자체를 삭제
						// => 그러기 위해서는 해당 tr 태그를 다른 댓글의 tr 태그와 구별하기 위한 id 필요
						// => id 선택자로 "replyTr" 문자열과 댓글번호를 조합하여 지정
						$("#replyTr" + reply_num).remove();
					} else {
						alert("댓글 삭제 실패!");
					}
				},
				fail: function() {
					alert("댓글 삭제 실패!");
				}
			});
		}
	}
	
	// 대댓글 작성 요청 폼 표시
	function reReplyWriteForm(reply_num, reply_re_ref, reply_re_lev, reply_re_seq) {
// 		alert(reply_num + ", " + reply_re_ref + ", " + reply_re_lev + ", " + reply_re_seq);

		// 기존에 존재하는 대댓글 입력폼 제거
		$("#reReplyTr").remove();

		// "replyTr" 문자열과 전달받은 댓글 번호를 조합하여 id 선택자로 지정한 후
		// 해당 태그 바깥쪽 뒷부분에 댓글 작성 폼 추가(after() 메서드 활용)
		// => 전달 데이터 : 원본 글번호, 댓글 참조번호, 들여쓰기레벨, 순서번호, 작성자, 본문
		// => 댓글 관련 정보는 현재 스크립트 로딩 시점에 존재하지 않는 데이터이므로
		//    함수 호출 시 파라미터로 전달하며, 원본 글번호는 이미 자바 객체로 전달받았으므로 직접 접근 가능
		$("#replyTr" + reply_num).after(
				'<tr id="reReplyTr">'
				+ '		<td colspan="3">'
				+ '			<form action="BoardTinyReReplyWrite" method="post" id="reReplyForm">'
				+ '				<input type="hidden" name="board_num" value="' + ${board.board_num} + '">'
				+ '				<input type="hidden" name="reply_re_ref" value="' + reply_re_ref + '">'
				+ '				<input type="hidden" name="reply_re_lev" value="' + reply_re_lev + '">'
				+ '				<input type="hidden" name="reply_re_seq" value="' + reply_re_seq + '">'
				+ '				<input type="hidden" name="reply_name" value="${sessionScope.sId }">'
				+ '				<textarea id="tinyReReplyTextArea" name="reply_content"></textarea>'
				+ '				<input type="button" value="댓글쓰기" id="btnTinyReReplySubmit" onclick="reReplyWrite()">'
				+ '			</form>'
				+ '		</td>'
				+ '</tr>'
		);
	}
	
	// 대댓글 작성 요청
	function reReplyWrite() {
// 		alert($("#reReplyForm").serialize());
		$.ajax({
			type: "POST",
			url: "BoardTinyReReplyWrite",
			data: $("#reReplyForm").serialize(),
			dataType: "text",
			success: function(result) {
// 				console.log(result);
				if(result == "true") {
// 					console.log("화면 갱신");
					location.reload(); // 화면 갱신(전달받은 POST 데이터도 유지됨, 브라우저에 이력 남지 않음)
// 					location.replace(location.href); // 화면 갱신(전달받은 POST 데이터 유지 X, 브라우저에 이력 남지 않음)
// 					location.href = location.href; // 화면 갱신(현재 페이지로 다시 새로 이동, 브라우저에 이력 남음)
				} else {
					alert("댓글 작성 실패!");
				}
			},
			fail: function() {
				alert("댓글 작성 실패!");
			}		
		});
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
	<section id="tinyReplyArea">
		<form action="BoardTinyReplyWrite" method="post">
			<%-- hidden 속성으로 원본 글번호와 작성자 아이디(세션 아이디)를 함께 포함 --%>
			<input type="hidden" name="board_num" value="${board.board_num}">
			<input type="hidden" name="reply_name" value="${sessionScope.sId }">
			<%-- 세션 아이디가 없을 경우(= 미로그인) 댓글 작성 차단 --%>
			<c:choose>
				<c:when test="${empty sessionScope.sId }">
					<textarea id="tinyReplyTextArea" name="reply_content" placeholder="로그인 한 사용자만 댓글 작성 가능" disabled></textarea>
					<input type="submit" value="댓글쓰기" id="btnTinyReplySubmit" disabled>
				</c:when>
				<c:otherwise>
					<textarea id="tinyReplyTextArea" name="reply_content"></textarea>
					<input type="submit" value="댓글쓰기" id="btnTinyReplySubmit">
				</c:otherwise>
			</c:choose>
		</form>
		<%-- 댓글 목록 표시 영역 --%>
		<div id="tinyReplyContentArea">
			<table>
				<%-- 반복문을 통해 tinyReplyBoardList 객체로부터 댓글 목록(내용, 작성자, 작성일) 출력 --%>
				<c:forEach var="tinyReplyBoard" items="${tinyReplyBoardList }">
					<%-- 댓글 1개 행 작성 시 삭제 등에 활용하기 위한 id 값 지정(댓글 번호를 조합하여 고유번호로 활용) --%>
					<tr id="replyTr${tinyReplyBoard.reply_num }">
						<td class="replyContent">
							<%-- reply_re_lev 값이 0보다 크면 대댓글이므로 들여쓰기 후 이미지(re.gif) 표시 --%>
							<c:if test="${tinyReplyBoard.reply_re_lev > 0 }">
								<%-- 반복문을 통해 공백 추가 --%>
								<c:forEach var="i" begin="1" end="${tinyReplyBoard.reply_re_lev }">
									&nbsp;&nbsp;
								</c:forEach>
							</c:if>
							${tinyReplyBoard.reply_content }
							<%-- 세션 아이디가 존재할 경우 대댓글 작성 이미지(reply-icon.png) 추가 --%>
							<c:if test="${not empty sessionScope.sId }">
								<%-- 대댓글 작성 아이콘 클릭 시 자바스크립트 reReplyWriteForm() 함수 호출 --%>
								<%-- 파라미터 : 댓글 번호, 댓글 참조글번호, 댓글 들여쓰기 레벨, 댓글 순서번호 --%>
								<a href="javascript:reReplyWriteForm(${tinyReplyBoard.reply_num }, ${tinyReplyBoard.reply_re_ref }, ${tinyReplyBoard.reply_re_lev }, ${tinyReplyBoard.reply_re_seq })">
									<img src="${pageContext.request.contextPath }/resources/images/reply-icon.png">
								</a>
								<%-- 또한, 관리자 또는 세션 아이디와 댓글 아이디가 같으면 댓글 삭제 이미지(delete-icon.png) 추가 --%>
								<c:if test="${sessionScope.sId eq 'admin' or sessionScope.sId eq tinyReplyBoard.reply_name}">
									<%-- 삭제 버튼 클릭 시 자바스크립트를 통해 삭제 작업 확인(댓글번호 전달) --%>
									<a href="javascript:confirmReplyDelete(${tinyReplyBoard.reply_num })">
										<img src="${pageContext.request.contextPath }/resources/images/delete-icon.png">
									</a>
								</c:if>
							</c:if>
						</td>
						<td class="replyWriter">
							${tinyReplyBoard.reply_name }	
						</td>
						<td class="replyDate">
							<fmt:formatDate value="${tinyReplyBoard.reply_date }" pattern="yy-MM-dd HH:mm" />	
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</section>
</body>
</html>
















