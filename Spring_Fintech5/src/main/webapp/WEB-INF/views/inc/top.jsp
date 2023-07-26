<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<script>
	function logout() {
		let isLogout = confirm("로그아웃 하시겠습니까?");
		
		// isLogout 이 true 일 때 MemberLogout 서블릿 요청
		if(isLogout) {
			location.href = "MemberLogout";
		}
	}
</script> 
<div id="member_area">
	<a href="./">Home</a>
	<%-- 로그인 여부 판별하여 각각 다른 링크 표시 --%>
	<%-- EL 의 내장객체 sessionScope 에 접근하여 "sId" 속성값 판별 --%>
	<c:choose>
		<c:when test="${empty sessionScope.sId }">
			| <a href="MemberLoginForm">Login</a>
			| <a href="MemberJoinForm">Join</a>
		</c:when>
		<c:otherwise>
			<%-- 아이디 클릭 시 회원 정보 조회를 위한 MemberInfo 요청 --%>
			| <a href="MemberInfo">${sessionScope.sId } 님</a>
			<%-- 로그아웃 링크 클릭 시 자바스크립트 logout() 함수 실행 --%>
			| <a href="javascript:logout()">Logout</a>
			<%-- 만약, 세션 아이디가 "admin" 일 경우 관리자페이지(MemberList) 링크 표시 --%>
			<c:if test="${sessionScope.sId eq 'admin' }">
				| <a href="AdminMain">관리자페이지</a>
			</c:if>
		</c:otherwise>
	</c:choose>
</div>









