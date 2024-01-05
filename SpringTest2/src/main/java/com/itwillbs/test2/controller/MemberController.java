package com.itwillbs.test2.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {
	
	// "MemberLoginForm" 서블릿 요청(GET) 시 
	// test2 디렉토리의 member_login_form.jsp 페이지로 포워딩
	@GetMapping("MemberLoginForm")
	public String loginForm() {
		return "test2/member_login_form";
	}
	
	// "MemberLoginPro" 서블릿 요청(POST) 시
	// 폼파라미터(id, passwd) 전달받아 콘솔에 출력 후
	// "MemberList" 서블릿 요청(리다이렉트)
//	@PostMapping("MemberLoginPro")
//	public String loginPro(@RequestParam String id, @RequestParam String passwd) {
//		// 메서드 파라미터로 폼파라미터의 이름과 일치하는 변수 선언하면 자동으로 전달된 데이터 저장
//		System.out.println("아이디 : " + id);
//		System.out.println("패스워드 : " + passwd);
//		
//		// 리다이렉트를 수행하기 위해 "redirect:/리다이렉트경로" 형식으로 리턴
//		return "redirect:/MemberList";
//	}
	
	// Map 타입 파라미터로 전달받을 경우 => @RequestParam 어노테이션 필수!
	@PostMapping("MemberLoginPro")
	public String loginPro(@RequestParam Map<String, String> map) {
		// 메서드 파라미터로 폼파라미터의 이름과 일치하는 변수 선언하면 자동으로 전달된 데이터 저장
		System.out.println("아이디 : " + map.get("id"));
		System.out.println("패스워드 : " + map.get("passwd"));
		
		// 리다이렉트를 수행하기 위해 "redirect:/리다이렉트경로" 형식으로 리턴
		return "redirect:/MemberList";
	}
	
	// "MemberList" 서블릿 요청(GET) 시
	// "회원 목록 출력" 메세지 콘솔에 출력
	// 리턴값은 널스트링("") 사용
	@GetMapping("MemberList")
	public String list() {
		System.out.println("회원 목록 출력!");
		return "";
	}
	
}















