package com.itwillbs.test2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// 컨트롤러 역할을 수행하는 클래스 정의 시 @Controller 어노테이션을 클래스 선언부 윗줄에 지정
@Controller
public class TestController {
	// "/main" 서블릿 주소 요청 시 자동으로 호출되는 requestMain() 메서드 정의
	// => 파라미터 : 없음, 리턴타입 : String
	// => GET 방식 요청 시 "index.jsp" 페이지 포워딩
//	@RequestMapping(value = "/main", method = RequestMethod.POST) // method 속성 생략 시 기본값 GET
//	public String requestMain() {
//		return "index";
//	}
	// => 주의! POST 방식만 지정했을 경우 GET 방식 요청이 들어오면 일치하는 메서드가 없으므로
	//    요청을 처리할 수 없게 된다!
	// => HTTP 상태 405 – 허용되지 않는 메소드(Request method 'GET' not supported) 오류 발생! 
	
	@RequestMapping(value = "/main") // method 속성 생략 시 기본값 GET
	public String requestMain() {
//		return "index";
		return "main";
	}
	
	// "/test1" 서블릿 주소 요청을 처리할 test1() 메서드 정의
	// => @GetMapping 어노테이션을 사용하여 "GET 방식 요청 전용" 처리
	// => 만약, POST 방식의 요청이 발생하면 405 에서 발생한다! 
	//    HTTP 상태 405 – 허용되지 않는 메소드(Request method 'POST' not supported)
	@GetMapping(value = "/test1")
	public String test1() {
		// test 디렉토리의 test1.jsp 페이지로 포워딩(Dispatch)
		// => views 디렉토리의 서브디렉토리를 포함해야할 경우 "서브디렉토리명/파일명" 형식으로 지정
		return "test/test1";
	}
	
	// ----------------------------------------------------------------
	// "/test2" 서블릿 주소 요청을 처리할 test2() 메서드 정의
	// => @GetMapping 어노테이션으로 GET 방식 요청을 처리 - test2_get() 메서드 정의
	// => @PostMapping 어노테이션으로 POST 방식 요청을 처리 - test2_post() 메서드 정의
	// => 두 메서드 모두 test/test2.jsp 페이지로 포워딩
	@GetMapping("/test2") // value 속성명 생략도 가능(value = "/test2")
	public String test2_get() {
		return "test/test2";
	}
	
	@PostMapping("/test2") // value 속성명 생략도 가능(value = "/test2")
	public String test2_post() {
		return "test/test2";
	}
	
	// @RequestMapping 어노테이션을 사용하여 "/test2" 요청에 대해 GET, POST 방식 모두 매핑
//	@RequestMapping(value = "/test2", method = {RequestMethod.GET, RequestMethod.POST})
//	public String test2() {
//		System.out.println("@RequestMapping");
//		return "test/test2";
//	}
	
}












