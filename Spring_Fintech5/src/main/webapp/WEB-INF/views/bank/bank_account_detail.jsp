<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- 외부 CSS 파일 연결하기 -->
<link href="${pageContext.request.contextPath }/resources/css/default.css" rel="stylesheet" type="text/css">
</head>
<body>
	<header>
		<!-- Login, Join 링크 등 표시 영역 -->
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<article>
		<h1>${user_name } 고객님의 계좌 잔고 상세정보</h1>
		<table border="1">
			<tr>
				<th>은행명</th>
				<th>계좌번호</th>
				<th>상품명</th>
				<th>계좌잔액</th>
				<th>출금가능금액</th>
				<th></th>
			</tr>
			<tr>
				<td>${accountDetail.bank_name }</td>
				<td>${account_num_masked }</td>
				<td>${accountDetail.product_name }</td>
				<td>${accountDetail.balance_amt }</td>
				<td>${accountDetail.available_amt }</td>
				<td>
					<%-- 2.5.1. 출금이체 API 요청을 위한 폼 생성(PDF p74) --%>
					<form action="bankWithdraw" method="post">
						<%-- hidden 타입으로 은행명, 핀테크이용번호 전달 --%>
						<input type="hidden" name="bank_name" value="${accountDetail.bank_name }">
						<input type="hidden" name="fintech_use_num" value="${accountDetail.fintech_use_num }">
						<input type="submit" value="출금이체">
					</form>
					<%-- 2.5.2. 입금이체 API 요청을 위한 폼 생성(PDF p83) --%>
					<form action="bankDeposit" method="post">
						<%-- hidden 타입으로 은행명, 핀테크이용번호 전달 --%>
						<input type="hidden" name="fintech_use_num" value="${accountDetail.fintech_use_num }">
						<input type="submit" value="입금이체">
					</form>
				</td>
			</tr>	
		</table>
	</article>	
</body>
</html>










