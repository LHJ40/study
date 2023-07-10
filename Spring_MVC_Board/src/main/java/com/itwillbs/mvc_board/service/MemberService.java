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
	public boolean isMailAuth() {
		// TODO Auto-generated method stub
		return false;
	}

}











