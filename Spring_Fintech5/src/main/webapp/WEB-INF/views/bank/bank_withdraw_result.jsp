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
	<%--
	출금은행명(bank_name), 출금은행기관코드(bank_code_std)
	거래일자(bank_tran_date), 송금인성명(account_holder_name)
	출금금액(tran_amt), 출금한도잔여금액(wd_limit_remain_amt), 출금계좌인자내역(print_content)
	--%>
	<header>
		<!-- Login, Join 링크 등 표시 영역 -->
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<article>
		<h1>출금이체 결과 내역</h1>
		<table border="1">
			<tr>
				<th>출금은행명(기관코드)</th>
				<th>거래일자</th>
				<th>송금인성명</th>
				<th>출금금액</th>
				<th>출금한도잔여금액</th>
				<th>출금계좌인자내역</th>
			</tr>
			<tr>
				<td>${withdrawResult.bank_name }(${withdrawResult.bank_code_std })</td>
				<td>${withdrawResult.bank_tran_date }</td>
				<td>${withdrawResult.account_holder_name }</td>
				<td>${withdrawResult.tran_amt } 원</td>
				<td>${withdrawResult.wd_limit_remain_amt } 원</td>
				<td>${withdrawResult.print_content }</td>
			</tr>	
		</table>
	</article>		
</body>
</html>