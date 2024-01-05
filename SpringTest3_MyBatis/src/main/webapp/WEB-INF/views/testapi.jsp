<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
	$(function() {
		$("#btnOk").on("click", function() {
			// =============================================================================
			// 영화진흥위원회의 오픈API 를 사용하여 영화 정보 JSON 데이터 처리
			// (https://www.kobis.or.kr/kobisopenapi/homepg/main/main.do)
			// 일별 박스오피스 목록 조회 후 테이블에 표시
			// (https://www.kobis.or.kr/kobisopenapi/homepg/apiservice/searchServiceInfo.do?serviceId=searchDailyBoxOffice)
			// => 지정된 요청 파라미터 형식에 맞게 URL 을 수정해야함
			// 샘플 API 요청 주소
			// http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=f5eef3421c602c6cb7ea224104795888&targetDt=20120101
			$.ajax({
				type: "GET",
				url: "https://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=f5eef3421c602c6cb7ea224104795888&targetDt=20221013",
				dataType: "json", // 응답 데이터가 JSON 형식으로 전달되도록 "json" 타입 지정 
			})
			.done(function(data) { // 요청 성공 시
// 				$("#resultArea").html(JSON.stringify(data)); // JSON 데이터를 문자열로 변환
				// 박스오피스 목록이 저장된 JSON 객체(data) 로부터 영화 목록 정보 추출하기
				// 1. 일별 박스오피스 목록(10개)이 저장된 "boxOfficeResult" 객체 추출
				let boxOfficeResult = data.boxOfficeResult;
// 				$("#resultArea").html(JSON.stringify(boxOfficeResult));
				
				// 2. 박스오피스 타입(boxofficeType)과 조회날짜(showRange) 추출
				// => boxOfficeResult 객체를 통해 접근
				let boxOfficeType = boxOfficeResult.boxofficeType;
				let showRange = boxOfficeResult.showRange;
// 				$("#resultArea").html(JSON.stringify(boxOfficeType) + "(" + JSON.stringify(showRange) + ")");
				
				// 3. 일별 박스오피스 목록(dailyBoxOfficeList) 추출
				// => 복수개의 영화 정보 객체{}가 dailyBoxOfficeList 라는 이름의 배열[]로 관리됨
				let dailyBoxOfficeList = boxOfficeResult.dailyBoxOfficeList;
				
				// 4. 추출된 박스오피스 목록(배열)을 반복문을 통해 반복하면서
				//    순위(rank), 제목(movieNm), 개봉일(openDt), 누적관객수(audiAcc) 추출 및 출력
				for(let i = 0; i < dailyBoxOfficeList.length; i++) {
					$("#resultArea > table").append(
						"<tr>" + 
						"<td>" + dailyBoxOfficeList[i].rank + "</td>" + 
						"<td>" + dailyBoxOfficeList[i].movieNm + "</td>" + 
						"<td>" + dailyBoxOfficeList[i].openDt + "</td>" + 
						"<td>" + dailyBoxOfficeList[i].audiAcc + "</td>" + 
						"</tr>"
					);
				}
			})
			.fail(function() { // 요청 실패 시
				$("#resultArea").html("요청 실패!");
			});
			
		});
		
	});
</script>
</head>
<body>
	<h1>test5_json.jsp - 영화 목록</h1>
	<input type="button" value="박스오피스 순위 데이터 파싱" id="btnOk">
	<hr>
	<div id="resultArea">
		<table border="1">
			<tr>
				<th width="80">현재순위</th>
				<th width="400">영화명</th>
				<th width="150">개봉일</th>
				<th width="100">누적관객수</th>
			</tr>
		</table>
	</div>
</body>
</html>