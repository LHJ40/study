package com.itwillbs.fintech.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.fintech.service.MemberService;
import com.itwillbs.fintech.vo.MemberVO;

@Controller
public class AdminController {
	
	@Autowired
	private MemberService service;
	
	// "AdminMain" 서블릿 요청에 대해 관리자 메인페이지로 포워딩
	// => 단, 세션 아이디가 "admin" 일 경우에만 포워딩을 수행하고
	//    아닐 경우 "fail_back" 페이지를 사용하여 "잘못된 접근입니다!" 출력 후 이전페이지로 돌아가기
	@GetMapping("AdminMain")
	public String main(HttpSession session, Model model) {
		String id = (String)session.getAttribute("sId");
		
		if(id == null || !id.equals("admin")) { // 미로그인 또는 "admin" 이 아닐 경우
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "fail_back";
		} else { // "admin" 일 경우
			return "admin/admin_main";
		}
		
	}
	
	// "MemberList" 서블릿 요청에 대해 회원목록 조회 작업 수행
	// 단, 미로그인 또는 세션 아이디가 "admin" 이 아닐 경우 잘못된 접근 처리
	@GetMapping("MemberList")
	public String memberList(HttpSession session, Model model) {
		String id = (String)session.getAttribute("sId");
		
		if(id == null || !id.equals("admin")) { // 미로그인 또는 "admin" 이 아닐 경우
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "fail_back";
		} else {
			// MemberService - getMemberList()
			// 전체 회원 목록 조회 작업 요청
			// => 파라미터 : 없음   리턴타입 : List<MemberVO>(memberList)
			List<MemberVO> memberList = service.getMemberList();
			
			// 회원 목록 저장
			model.addAttribute("memberList", memberList);
			
			return "admin/member_list";
		}

	}
	
}









