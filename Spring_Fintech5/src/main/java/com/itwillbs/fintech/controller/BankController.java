package com.itwillbs.fintech.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.fintech.service.BankApiService;
import com.itwillbs.fintech.service.BankService;
import com.itwillbs.fintech.vo.ResponseTokenVO;

@Controller
public class BankController {
	@Autowired
	private BankService bankService;
	
	@Autowired
	private BankApiService bankApiService;
	
	// 로그 출력을 위한 변수 선언 => getLogger() 메서드 파라미터로 로그를 처리할 현재 클래스 지정
	private static final Logger logger = LoggerFactory.getLogger(BankController.class);
	
	// 2.1.1. 사용자인증 API (3-legged)
	// 사용자 인증 요청에 따른 콜백을 통해 인증코드를 전달받기
	// => 파라미터 : Map<String, String>, Model, HttpSession
	//               (Map 타입 객체로 응답데이터를 모두 저장)
	@GetMapping("callback")
	public String responseAuthCode(@RequestParam Map<String, String> authResponse, Model model, HttpSession session) {
		// 로거 사용 시 logger.xxx() 메서드로 로그에 출력할 데이터를 파라미터로 전달
		logger.info(authResponse.toString());
		
		// 인증 실패 시(= 인증 정보 존재하지 않을 경우) 오류 메세지 출력 및 인증 창 닫기
		if(authResponse == null || authResponse.get("code") == null) {
			// Model 객체를 통해 출력할 메세지(msg) 전달 및 창 닫기 여부(isClose)도 전달
			model.addAttribute("msg", "인증 오류 발생! 다시 인증하세요!");
			model.addAttribute("isClose", true); // true : 창 닫기, false : 창 닫기 X(이전페이지로 돌아가기)
			return "fail_back";
		}
		
		// 2.1.2. 토큰발급 API - 토큰 발급 요청
		// REST 방식 요청을 통해 인증코드를 사용하여 엑세스토큰 발급 요청
		// => BankApiService - requestToken() 메서드를 호출하여 엑세스토큰 발급 요청
		// => 파라미터 : 사용자 인증을 통해 전달받은 정보(Map 객체)
		//    리턴타입 : 엑세스토큰 관련 정보를 관리할 ResponseTokenVO 타입(responseToken)
		ResponseTokenVO responseToken = bankApiService.requestToken(authResponse);
		logger.info("★★★★★ Access Token : " + responseToken.toString());
		
		// 만약, 리턴받은 엑세스토큰이 없을 경우 오류 메세지 출력 및 인증창 닫기
		if(responseToken.getAccess_token() == null) {
			// Model 객체를 통해 출력할 메세지(msg) 전달 및 창 닫기 여부(isClose)도 전달
			model.addAttribute("msg", "토큰 발급 오류 발생! 다시 인증하세요!");
			model.addAttribute("isClose", true); // true : 창 닫기, false : 창 닫기 X(이전페이지로 돌아가기)
			return "fail_back";
		}
		
		
		return "";
	}
	
}














