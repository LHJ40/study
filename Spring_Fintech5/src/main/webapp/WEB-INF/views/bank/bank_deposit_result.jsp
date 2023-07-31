<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
		<h1>입금이체 결과 내역</h1>
		<c:forEach var="depositInfo" items="${depositResult.res_list }">
			<table border="1">
				<tr>
					<th>입금은행명(은행코드)</th>
					<th>거래일자</th>
					<th>수취인성명</th>
					<th>입금금액</th>
				</tr>
				<tr>
					<td>${depositInfo.bank_name }(${depositInfo.bank_code_std })</td>
					<td>${depositInfo.bank_tran_date }</td>
					<td>${depositInfo.account_holder_name }</td>
					<td>${depositInfo.tran_amt } 원</td>
				</tr>
			</table>
		</c:forEach>	
	</article>	
</body>
</html>









