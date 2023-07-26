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
<script src="${pageContext.request.contextPath }/resources/js/jquery-3.7.0.js"></script>
<script type="text/javascript">
	// AJAX + JSON 을 활용한 게시물 목록 조회(무한스크롤 기능 포함)
	let pageNum = 1; // 기본 페이지 번호 미리 저장
	let maxPage = 1; // 최대 페이지 번호 미리 저장
	
	$(function() {
		// 게시물 목록 조회를 처음 수행하기 위해 문서 로딩 시 loadList() 함수 호출
		// => 검색타입과 검색어를 파라미터로 전달받아 변수에 저장 후 함수 호출 시 파라미터로 전달
		let searchType = $("#searchType").val();
		let searchKeyword = $("#searchKeyword").val();
// 		alert(searchType + ", " + searchKeyword);
		
		loadList(searchType, searchKeyword);
		
		// 무한스크롤 기능 추가
		// 웹브라우저의 스크롤바가 바닥에 닿으면 다음 목록 조회를 위해 loadList() 함수 호출
		$(window).on("scroll", function() { // 스크롤 동작 시 이벤트 처리
// 			console.log("scroll");
			
			// 1. window 객체와 document 객체를 활용하여 스크롤 관련 값 가져와서 제어
			// => 스크롤바의 현재 위치, 문서가 표시되는 창(window)의 높이, 문서 전체 높이
			let scrollTop = $(window).scrollTop(); // 스크롤바 현재 위치
			let windowHeight = $(window).height(); // 브라우저 창의 높이
			let documentHeight = $(document).height(); // 문서의 높이(창의 높이보다 크거나 같음)
// 			console.log("scrollTop = " + scrollTop + ", windowHeight = " + windowHeight + ", documentHeight = " + documentHeight);
			
			// 2. 스크롤바 위치값 + 창 높이 + x 가 문서 전체 높이(documentHeight) 이상일 경우
			//    다음 페이지 게시물 목록 로딩하여 목록 하단에 추가
			//    => 이 때, x 는 스크롤바의 바닥으로부터의 여유 공간
			//       (즉, x가 클 수록 스크롤바를 더 적게 내려도 다음 목록 로딩)
			//       (만약, x = 1 이면 스크롤바가 바닥에 닿아야만 다음 목록 로딩됨)
			let x = 1;
			if(scrollTop + windowHeight + x >= documentHeight) {
				// 다음 페이지 글목록 로딩을 위한 loadList() 함수 호출
				// 이 때, 페이지 번호로 사용될 pageNum 값을 1 증가시켜 다음 페이지 목록 지정
// 				pageNum++;
// 				loadList(searchType, searchKeyword);
				// -------------------------------------------------------------------------
				// 최대 페이지번호를 초과하면 다음 페이지 로딩 요청하지 않도록 처리
				// => pageNum 값이 maxPage 보다 작을 동안 페이지번호 증가하면서 다음 페이지 로딩
				if(pageNum < maxPage) {
					pageNum++;
					loadList(searchType, searchKeyword);
				} else {
// 					alert("다음 페이지가 없습니다");
				}
			}
			
		});
	});
	
	function loadList(searchType, searchKeyword) {
		let url;
		
		// searchKeyword 값이 없을 경우 pageNum 을 전달하여 BoardListJson 서블릿 요청
		// 아니면, pageNum, searchType, searchKeyword 를 전달하여 BoardListJson 서블릿 요청
// 		if(searchKeyword == "") {
// 			url = "BoardListJson?pageNum=" + pageNum;
// 		} else {
//  			url = "BoardListJson?pageNum=" + pageNum + "&searchType=" + searchType + "&searchKeyword=" + searchKeyword;
// 		}
		// --------------------------------------------------------------------------------------------
		// 자바스크립트에서 키워드 입력 여부를 판별하지 않고 컨트롤러로 요청(컨트롤러에서 판별)
		url = "BoardListJson?pageNum=" + pageNum + "&searchType=" + searchType + "&searchKeyword=" + searchKeyword;
		
		$.ajax({
			type: "GET",
			url: url,
			dataType: "json",
			success: function(data) {
// 				$("table").after(JSON.stringify(boardList));
				// JSONArray 객체를 사용하여 리턴받은 JSON 데이터(객체)를 
				// 반복문을 사용하여 차례대로 접근 후 데이터 출력
// 				for(board of data) {
// 					// board 객체의 board_re_lev 값이 0보다 크면
// 					// 제목열에 해당 값만큼 공백(&nbsp;) 추가 후 공백 뒤에 답글 아이콘 이미지(re.gif) 추가
// 					let space = "";
// 					if(board.board_re_lev > 0) {
// 						// 반복문을 사용하여 board_re_lev 값만큼 공백(&nbsp;) 추가
// 						for(let i = 0; i < board.board_re_lev; i++) {
// // 							console.log("공백 추가");
// 							space += "&nbsp;&nbsp;";
// 						}
						
// 						// 반복문 종료 후 답글 아이콘 이미지 추가
// 						space += "<img src='${pageContext.request.contextPath }/resources/images/re.gif'>";
// 					}
					
// 					// 테이블에 표시할 JSON 객체 1개 출력문 생성(= 1개 게시물) => 반복
// 					let item = "<tr height='50'>"
// 								+ "<td>" + board.board_num + "</td>" 
// 								+ "<td id='subject'>"
// 									+ space
// 									+ "<a href='BoardDetail?board_num=" + board.board_num + "'>"
// 									+ board.board_subject 
// 									+ "</a>"
// 								+ "</td>" 
// 								+ "<td>" + board.board_name + "</td>" 
// // 								+ "<td>" + board.board_date + "</td>" 
// 								+ "<td>" + getFormatDate(board.board_date) + "</td>" 
// 								+ "<td>" + board.board_readcount + "</td>" 
// 								+ "</tr>"
// 					$("table").append(item);
// 				}
				// ----------------------------------------------------------
				// 글 목록과 최대 페이지번호를 함께 전달받은 경우
				// 1. 최대 페이지 번호 꺼내기(최초 페이지 로딩 시 첫번째 목록과 함께 전달됨)
				maxPage = data.maxPage; // 무한스크롤 다음 페이지 로딩 과정에서 페이지번호와 비교 시 활용
// 				console.log("maxPage = " + maxPage);
				
				for(board of data.boardList) {
					// board 객체의 board_re_lev 값이 0보다 크면
					// 제목열에 해당 값만큼 공백(&nbsp;) 추가 후 공백 뒤에 답글 아이콘 이미지(re.gif) 추가
					let space = "";
					if(board.board_re_lev > 0) {
						// 반복문을 사용하여 board_re_lev 값만큼 공백(&nbsp;) 추가
						for(let i = 0; i < board.board_re_lev; i++) {
// 							console.log("공백 추가");
							space += "&nbsp;&nbsp;";
						}
						
						// 반복문 종료 후 답글 아이콘 이미지 추가
						space += "<img src='${pageContext.request.contextPath }/resources/images/re.gif'>";
					}
					
					// 테이블에 표시할 JSON 객체 1개 출력문 생성(= 1개 게시물) => 반복
					let item = "<tr height='50'>"
								+ "<td>" + board.board_num + "</td>" 
								+ "<td id='subject'>"
									+ space
									+ "<a href='BoardDetail?board_num=" + board.board_num + "'>"
									+ board.board_subject 
									+ "</a>"
								+ "</td>" 
								+ "<td>" + board.board_name + "</td>" 
// 								+ "<td>" + board.board_date + "</td>" 
								+ "<td>" + getFormatDate(board.board_date) + "</td>" 
								+ "<td>" + board.board_readcount + "</td>" 
								+ "</tr>"
					$("table").append(item);
				}
				
			},
			error: function() {
				alert("글 목록 요청 실패!");
			}
		});
		
	}
	
	// 자바스크립트를 통한 문자열 포맷(형식) 변경하기
	// => 현재 날짜 및 시각 형식 : "yyyy-MM-dd HH:mm:ss" (ex. 2023-07-07 12:31:30.0)
	// => 변경할 날짜 및 시각 형식 : "yy-MM-dd HH:mm" (ex. 23-07-07 12:31)
	// => replace() 메서드와 정규표현식 활용하여 포맷 변경 가능
	//    정규표현식으로 변경할 부분을 소괄호() 로 감싼 후, 해당 () 부분을 $순서번호 형식으로 지정하여
	//    replace() 메서드로 치환
	function getFormatDate(date) { // 문자열로 된 날짜 및 시각 데이터 전달받기
		let targetDate = /(\d\d)(\d\d)-(\d\d)-(\d\d)\s(\d\d):(\d\d):(\d\d).(\d)/g;
		let formatDate = "$2-$3-$4 $5:$6";
		// => 연도 숫자 4자리를 2자리씩 분리하여 소괄호로 감싸기(\d\d) => 앞 두자리 제거를 위해 2자리씩 분리
		//    나머지 월, 일, 시, 분, 초도 2자리씩 소괄호로 감싸고, 남은 밀리초는 1자리 숫자로 지정
		// => 정규표현식에서 각 소괄호 그룹마다 인덱스가 자동으로 부여됨
		//    인덱스는 1번부터 시작하며, 변환할 데이터 지정 시 
		//    치환할 문자열 대상을 $인덱스번호 형식으로 지정 시 원하는 대상 지정
		//    즉, 원하는 대상을 $인덱스번호 를 통해 원하는 형식으로 변경 가능
		// => 2번데이터-3번데이터-4번데이터 5번데이터:6번데이터 형식으로 변환
		//    연도2자리-월2자리-일2자리 시2자리:분2자리 형식으로 지정("$2-$3-$4 $5:$6")
		return date.replace(targetDate, formatDate); // 전달받은 날짜 및 시각을 지정된 형식으로 변환하여 리턴
	}
</script>
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
		</table>
	</section>
</body>
</html>













