package com.itwillbs.mvc_board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.mvc_board.mapper.MemberMapper;
import com.itwillbs.mvc_board.vo.MemberVO;

@Service
public class MemberService {
	@Autowired
	private MemberMapper mapper;

	// 회원 가입
	public int registMember(MemberVO member) {
		return mapper.insertMember(member);
	}

	// 로그인 정보 조회(아이디와 패스워드가 일치하는 레코드 조회)
	public MemberVO selectCorrectUser(MemberVO member) {
		return mapper.selectCorrectUser(member);
	}

	// 로그인 정보 조회(아이디가 일치하는 레코드의 패스워드 조회)
	public String getPasswd(MemberVO member) {
		return mapper.selectPasswd(member);
	}

	// 회원 상세정보 조회
	public MemberVO getMemberInfo(String id) {
		return mapper.selectMemberInfo(id);
	}

	// 회원 목록 조회(관리자모드)
	public List<MemberVO> getMemberList() {
		return mapper.selectMemberList();
	}

	// 회원 정보 수정(일반, 관리자모드)
	public int modifyMember(MemberVO member, String newPasswd) {
		return mapper.updateMember(member, newPasswd);
	}

	// 회원 정보 삭제(탈퇴)(일반, 관리자모드)
	public int removeMember(MemberVO member) {
		return mapper.deleteMember(member);
	}

	// 이메일 인증 여부 확인
	public boolean isMailAuth(MemberVO member) {
		// Mapper - selectMailAuthStatus() 메서드를 호출하여 메일 인증 여부 조회
		// 조회 결과가 "Y" 이면 true, 아니면 false 리턴
		if(mapper.selectMailAuthStatus(member).equals("Y")) {
			return true;
		} else {
			return false;
		}
	}

	// 이메일과 일치하는 아이디 조회
	public String getId(String email) {
		return mapper.selectId(email);
	}

	// 인증 정보 추가 or 변경
	public void registAuthInfo(String id, String authCode) {
		// 기존 인증정보가 존재하는지 여부 확인
		// MemberMapper - getAuthInfo() 메서드를 호출하여 기존 인증정보 조회
		// => 파라미터 : 아이디    리턴타입 : AuthInfoVO(authInfo)
		
	}

}











