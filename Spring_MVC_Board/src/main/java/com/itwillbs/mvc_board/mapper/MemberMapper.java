package com.itwillbs.mvc_board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.mvc_board.vo.MemberVO;

@Mapper
public interface MemberMapper {
	
	// 회원 가입
	int insertMember(MemberVO member);

	// 로그인(아이디와 패스워드가 일치하는 레코드 조회)
	MemberVO selectCorrectUser(MemberVO member);

	// 로그인(아이디가 일치하는 레코드의 패스워드 조회)
	String selectPasswd(MemberVO member);

	// 회원 상세정보 조회
	MemberVO selectMemberInfo(String id);

	// 회원 목록 조회(관리자모드)
	List<MemberVO> selectMemberList();

	// 회원 정보 수정(일반, 관리자모드)
	// 주의! 메서드 파라미터 2개 이상을 XML 에서 접근하려면
	// @Param 어노테이션을 통해 각 파라미터의 변수명을 직접 지정해야한다!
//	int updateMember(MemberVO member, String newPasswd); // SQL 구문 실행하여 데이터 매핑 시 오류 발생!
	int updateMember(@Param("member") MemberVO member, @Param("newPasswd") String newPasswd);

	// 회원 정보 삭제(탈퇴)(일반, 관리자모드)
	int deleteMember(MemberVO member);

}













