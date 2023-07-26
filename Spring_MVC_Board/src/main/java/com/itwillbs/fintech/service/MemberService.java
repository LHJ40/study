package com.itwillbs.fintech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.fintech.mapper.MemberMapper;
import com.itwillbs.fintech.vo.AuthInfoVO;
import com.itwillbs.fintech.vo.MemberVO;

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
		// MemberMapper - selectAuthInfo() 메서드를 호출하여 기존 인증정보 조회
		// => 파라미터 : 아이디    리턴타입 : AuthInfoVO(authInfo)
		AuthInfoVO authInfo = mapper.selectAuthInfo(id);
		
		// 기존 인증정보가 존재하지 않을 경우 = 첫 인증메일 발송
		// => 새 인증정보를 추가(INSERT)
		// 아니면, 기존 인증정보가 존재하지 않을 경우 = 두번째 이상의 인증메일 발송
		// => 기존 인증정보를 갱신(= 아이디가 일치하는 레코드의 인증코드를 UPDATE)
		if(authInfo == null) {
			System.out.println("기존 인증정보 없음!");
			
			// MemberMapper - insertAuthInfo() 메서드를 호출하여 인증정보 추가
			// => 파라미터 : 아이디, 인증코드   리턴타입 : void
			mapper.insertAuthInfo(id, authCode);
		} else {
			System.out.println("기존 인증정보 있음! - " + authInfo);
			
			// MemberMapper - updateAuthInfo() 메서드를 호출하여 인증정보 갱신
			// => 파라미터 : 아이디, 인증코드   리턴타입 : void
			mapper.updateAuthInfo(id, authCode);
		}
		
	}

	// 이메일 인증 수행
	public boolean emailAuth(AuthInfoVO authInfo) {
		boolean isAuthSuccess = false;
		
		// Mapper - selectAuthInfo() 메서드를 호출(재사용)하여 아이디가 일치하는 인증정보 조회
		// => 조회 결과가 있을 경우
		//    리턴받은 결과의 인증코드를 전달받은 인증코드와 비교하여 일치하면 인증정보 갱신하고
		//    아니면 인증실패로 false 값 리턴
		// => 조회 결과가 없을 경우 인증실패로 false 값 리턴
		AuthInfoVO currentAuthInfo = mapper.selectAuthInfo(authInfo.getId());
		
		System.out.println("전달받은 인증정보 : " + authInfo);
		System.out.println("조회된 인증정보 : " + currentAuthInfo);
		
		if(currentAuthInfo == null) { // 조회된 인증정보가 없을 경우
			isAuthSuccess = false;
		} else { // 조회된 인증정보가 있을 경우
			if(authInfo.getAuth_code().equals(currentAuthInfo.getAuth_code())) { // 인증코드가 동일할 경우
				// Mapper - updateMailAuthStatus() 메서드를 호출하여 member 테이블 인증상태 변경("Y")
				// => 파라미터 : 아이디   리턴타입 : void
				mapper.updateMailAuthStatus(authInfo.getId());
				
				// Mapper - deleteAuthInfo() 메서드를 호출하여 auth_info 테이블의 인증정보 삭제
				// => 파라미터 : 아이디   리턴타입 : void
				mapper.deleteAuthInfo(authInfo.getId());
				
				// 인증결과를 true 로 변경
				isAuthSuccess = true;
			} else { // 인증코드가 다를 경우
				isAuthSuccess = false;
			}
		}
		
		return isAuthSuccess;
	}

}











