package com.itwillbs.test2.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.itwillbs.test2.vo.PersonVO;
import com.itwillbs.test2.vo.TestVO;

@Controller
public class TestController2 {
	
	// "main2" 서블릿 요청 시 "test2/main.jsp" 페이지로 포워딩(GET)
	@GetMapping("main2")
	public String main() {
		return "test2/main";
	}
	
	// =========================================================================
	/*
	 * 스프링에서 다른 곳으로 Dispatcher 방식 포워딩 시 데이터를 전달하는 방법 2가지
	 *  1) 기존에 사용하던 HttpServletRequest 객체의 setAttribute() 사용
	 *     => JSP 파일이 아니므로 내장 객체 request 가 존재하지도 않고
	 *        컨트롤러 파라미터에 HttpServletRequest 객체가 명시되어 있지도 않음
	 *        따라서, 외부로부터 request 객체를 전달받아야 함
	 *     => 스프링에서는 의존주입(DI = Dependency Injection) 을 통해 전달받을 수 있음
	 *     => push() 메서드 내의 파라미터 타입으로 HttpServletRequest 타입을 선언하면
	 *        스프링에 의해 해당 객체가 자동으로 전달(= 주입) 됨
	 *  2) 스프링 전용 Model 객체의 addAttribute() 사용
	 *     => org.springframework.ui.Model 타입을 파라미터로 지정 시
	 *        데이터 저장이 가능한 Model 객체를 자동으로 주입받을 수 있음
	 *     => HttpServletRequest 객체와 성격이 유사하며,
	 *        java.util.Map 객체 기반으로 만든 스프링에서 제공하는 데이터 공유 객체
	 */
	
	// 1번 방법) 메서드 파라미터를 통해 HttpServletRequest 객체 주입받기
//	@GetMapping("push")
//	public String push(HttpServletRequest request) {
//		// request 객체에 "msg" 라는 속성명으로 "Hello, World! - request" 문자열 저장
//		request.setAttribute("msg", "Hello, World! - request");
//		
//		// request 객체에 "test" 라는 속성명으로 TestVO 객체 저장
//		request.setAttribute("test", new TestVO("제목 - request", "내용 - request"));
//		
//		// Dispatch 방식 포워딩을 통해 "WEB-INF/views/test2/push.jsp" 페이지로 포워딩
//		// => URL 유지되고, request 객체 유지됨
//		return "test2/push";
//	}
	
	// 2번 방법) 메서드 파라미터를 통해 Model 객체 주입받기
	@GetMapping("push")
	public String push(Model model) {
		// request.setAttribute() 와 마찬가지로 Model 객체의 addAttribute() 메서드로 데이터 저장
		// => request 객체와 범위(Scope) 동일(하나의 요청에 대한 응답 발생 지점까지)
		// => request 객체와 동시 사용 불가(일반적인 데이터 저장 시 request 객체보다 Model 객체 사용)
		// => 파라미터가 완벽하게 동일함
		
		// model 객체에 "msg" 라는 속성명으로 "Hello, World! - Model 객체" 문자열 저장
		model.addAttribute("msg", "Hello, World! - Model 객체");
		
		// request 객체에 "test" 라는 속성명으로 TestVO 객체 저장
		model.addAttribute("test", new TestVO("제목 - Model 객체", "내용 - Model 객체"));
		
		// Dispatch 방식 포워딩을 통해 "WEB-INF/views/test2/push.jsp" 페이지로 포워딩
		// => URL 유지되고, request 객체 유지됨
		return "test2/push";
	}
	
	// =================================================================================
	// 스프링의 컨트롤러에서 리다이렉트 방식의 포워딩 처리를 수행하려면
	// return 문에 "redirect:/리다이렉트할주소" 형식으로 지정
	// => 주의! 리다이렉트 방식은 request 객체를 통해 데이터 전달 불가
	//    (URL 에 파라미터를 붙여서 전달해야함)
	// => 리다이렉트 방식은 새로운 서블릿 주소로 요청이 발생할 때 사용
//	@GetMapping("redirect")
//	public String redirect() {
//		// 리다이렉트 방식으로 포워딩 할 새 서블릿 주소 "redirectServlet" 지정
//		System.out.println("redirect 서블릿 주소 요청에 대한 리다이렉트!");
//		
//		return "redirect:/redirectServlet";
//	}
//	
//	// "redirectServlet" 주소 요청에 대해
//	// Dispatch 방식으로 포워딩 수행할 redirectServlet() 메서드 정의
//	// => test2/redirect.jsp 페이지로 포워딩
//	@GetMapping("redirectServlet")
//	public String redirectServlet() {
//		return "test2/redirect";
//	}
	// ------------------------------------------------------------------------
	// 리다이렉트 방식의 데이터 전달 방법
	// => request 객체 사용 불가능하며, URL 을 통한 파라미터 전달
	//    정확히는 데이터 저장 후 request.getAttribute() 메서드로 데이터를 가져올 수 없다!
	// => 단, Model 객체로 데이터 저장 후 리다이렉트 수행 시 URL 파라미터를 통해 자동으로 데이터가 전달됨
//	@GetMapping("redirect")
//	public String redirect(Model model) {
//		String name = "hong";
//		int age = 20;
//		
////		return "redirect:/redirectServlet?name=" + name + "&age=" + age;
//		
//		// 주의! request 객체를 통한 데이터 전달 시 새 request 객체에 의해 기존 request 객체는 제거됨
////		request.setAttribute("nameRequest", name);
////		request.setAttribute("ageRequest", age);
//		
//		// Model 객체를 통한 데이터 전달 시 자동으로 Redirect URL 뒤에 파라미터 형식으로 변환되어 전달됨
//		model.addAttribute("name", name); 
//		model.addAttribute("age", age); 
//
//		return "redirect:/redirectServlet";
//		// => 리다이렉트 과정에서 Model 객체에 저장된 데이터가 URL 파라미터로 변환되어 전송됨
//		//    ex) http://localhost:8089/test2/redirectServlet?name=hong&age=20
//		// => 이 과정에서 속성명이 파라미터명으로, 속성값이 파라미터값으로 변환됨
//	}
	// -------------------------	
	// 파라미터 데이터를 컨트롤러 메서드 내에서 접근하는 방식
	// 1) HttpServletRequest 객체를 활용한 기존 방법(스프링에서 사용하지 않는 방식)
	//    => 컨트롤러의 메서드 내에서 HttpServletRequest 객체 타입 파라미터 선언 필요
//	@GetMapping("redirectServlet")
//	public String redirectServlet(HttpServletRequest request) {
////		System.out.println(request.getAttribute("name")); // 사용 불가(null 출력됨)
//		
//		// URL 파라미터로 전달받은 데이터는 request 객체의 getParameter() 메서드로 가져올 수 있다!
//		String name = request.getParameter("name");
//		int age = Integer.parseInt(request.getParameter("age"));
//		System.out.println("이름 : " + name + ", 나이 : " + age);
//		
//		return "test2/redirect";
//	}
	// -------------------------	
	// 2) 전달받은 파라미터명과 동일한 이름의 파라미터를 메서드 선언부에 선언하면
	//    이름이 일치하는 파라미터 데이터를 자동으로 저장(= 자동 주입)됨 => GET or POST 방식 무관
//	@GetMapping("redirectServlet")
//	public String redirectServlet(String name, int age) {
//		// name 파라미터값이 String name 변수에 저장되고
//		// age 파라미터값이 int age 변수에 자동으로 형변환 처리되어 저장되어 즉시 사용 가능
//		// 단, 변수명과 파라미터명이 일치하지 않으면 저장되지 않는다!
//		System.out.println("메서드 파라미터에 대한 각각의 변수를 선언하는 방식");
//		System.out.println("이름 : " + name + ", 나이 : " + age);
//		
//		return "test2/redirect";
//	}
	// -------------------------	
	// 3) 전달받은 파라미터명과 동일한 이름의 멤버변수가 선언된 VO 클래스를 파라미터 타입으로 지정하면
	//    해당 VO 클래스의 인스턴스 생성 및 Setter 호출을 통한 데이터 저장까지 자동으로 수행
	//    => 주의! VO 클래스에 멤버변수, 기본 생성자, Getter/Setter 정의 필수!
//	@GetMapping("redirectServlet")
//	public String redirectServlet(PersonVO person) {
//		// PersonVO 객체 생성 및 데이터 저장까지 자동으로 수행되므로 즉시 PersonVO 객체에 접근 가능
//		// (person.setXXX() 메서드 호출을 통한 데이터 저장 과정 자동화)
//		// 단, 멤버 변수명과 파라미터명이 일치하지 않으면 저장되지 않는다!
//		// 또한, VO 객체에 일치하지 않는 파라미터는 별도로 변수 또는 다른 VO 객체 타입 선언하여 처리 가능
//		// ex) public String redirectServlet(PersonVO person, int num)
//		System.out.println("PersonVO 타입 파라미터 선언하는 방식");
//		System.out.println("이름 : " + person.getName() + ", 나이 : " + person.getAge());
//		
//		return "test2/redirect";
//	}
	// -------------------------
	// 4) java.util.Map 타입을 파라미터로 선언하는 방법
	//    => 파라미터명을 key 로, 파라미터값을 value 한 쌍으로 갖는 Map 객체를 자동으로 생성하고
	//       각 파라미터값을 자동으로 저장
	//    => 단, Map 타입 객체 내에는 일치하는 파라미터가 없고, 파라미터를 생성하여 데이터를 저장해야하므로
	//       파라미터 선언 시 파라미터 앞에 @RequestParam 어노테이션을 지정하여
	//       파라미터 저장용 변수라는 표시해야한다!
	@GetMapping("redirectServlet")
	public String redirectServlet(@RequestParam Map<String, String> map) {
		// Map 객체 생성 및 데이터 저장까지 자동으로 수행되므로 즉시 Map 객체에 접근 가능
		System.out.println("Map 타입 파라미터 선언하는 방식");
		System.out.println("이름 : " + map.get("name") + ", 나이 : " + map.get("age"));
		
		return "test2/redirect";
	}
	
	// ----------------
	// @RequestParam 어노테이션 테스트용 age 파라미터 삭제를 위한 redirect() 메서드 정의
	@GetMapping("redirect")
	public String redirect(Model model) {
		String name = "hong";
		int age = 20;
		
		model.addAttribute("name", name); 
//		model.addAttribute("age", age); // age 파라미터를 전달하지 않도록 주석 처리

		return "redirect:/redirectServlet";
	}
	
	// 만약, 파라미터를 전달받을 변수를 직접 선언했을 경우
	// 문자열 파라미터는 전달받은 파라미터가 없으면 null 값이 자동으로 처리되지만
	// 수치 데이터 타입(int, long, float, double)이 파라미터로 전달될 경우
	// 문자열로 전달된 후 해당 타입으로 형변환 과정이 추가되므로
	// 해당 파라미터가 전달되지 않았을 때 변환 과정에서 오류가 발생한다!
	// => HTTP 상태 메세지 500 번 오류 발생함
	// 해결1) 모든 파라미터를 String 타입으로 선언한 후 필요에 따라 원하는 타입으로 형변환 작업 수행
	// 해결2) @RequestParam 어노테이션을 사용하여 해당 변수가 파라미터 데이터 저장용이라는 표시를 달고,
	//        기본값을 설정하여 값이 전달되지 않았을 경우 사용할 값을 지정할 수 있다!
	//        (단, @RequestParam 어노테이션만 지정하고 데이터가 전달되지 않으면 HTTP 상태 메세지 400 오류 발생함!)
	//        => @RequestParam(defaultValue = "기본값) 데이터타입 변수명
	//           (단, 모든 기본값은 문자열로 지정하며, 파라미터가 전달되면 defaultValue 는 사용되지 않음)
//	@GetMapping("redirectServlet")
//	public String redirectServlet(
//			@RequestParam(defaultValue = "") String name, 
//			@RequestParam(defaultValue = "0") int age) {
//		System.out.println("이름 : " + name + ", 나이 : " + age);
//		// => 전달되지 않은 파라미터가 있을 경우 defaultValue 속성에 지정된 값이 자동으로 사용됨
//		
//		return "test2/redirect";
//	}
	
	// =================================================================================
	// ModelAndView 객체
	// - 데이터 저장 용도의 Model 객체와 view 페이지 포워딩 기능을 함께 수행하는 객체
	// - 매핑 메서드 정의 시 리턴타입을 ModelAndView 타입으로 지정
	// - 데이터 저장 및 뷰페이지 포워딩을 일관된 방식으로 처리할 수 있음	
	@GetMapping("mav")
	public ModelAndView modelAndView() {
		// ModelAndView 객체 사용할 경우 데이터 저장용 객체로 Map 객체 사용(Model 타입도 가능)
		// => Map<String, 저장데이터타입> 형식으로 제네릭타입 지정
		// Map 객체(map)에 "person" 이라는 속성명으로 PersonVO 객체 1개 저장
//		Map<String, PersonVO> map = new HashMap<String, PersonVO>();
//		
//		PersonVO person = new PersonVO();
//		person.setName("이순신");
//		person.setAge(44);
//		map.put("person", person);
		// ------------------------
		// Map 객체에 "test" 라는 속성명으로 TestVO 객체 1개 저장
		Map<String, TestVO> map = new HashMap<String, TestVO>();
		map.put("test", new TestVO("제목입니다", "내용입니다"));
		
		// ------------------------------------------------------------------------
		// 기존 방식을 통해 뷰페이지로 Dispatch 방식 포워딩 수행하는 방법
		// => Model 객체에 Map 객체 저장 후 return 문을 통해 문자열로 뷰페이지 지정
		// => 메서드 리턴타입 : String, 메서드 파라미터 : Model
//		model.addAttribute("person", person);
//		return "test2/model_and_view";
		// ------------------------------------------------------------------------
		// ModelAndView 객체를 사용하여 객체 저장 후 뷰페이지 지정 수행
		// => 객체 생성 : new ModelAndView("이동할 페이지", "저장 데이터 속성명", 저장 데이터);
		//    (Dispatch 방식 포워딩 시 return 문 뒤에 기술했던 경로 그대로 지정)
		// => 포워딩 방식은 기존의 Dispatch 방식 그대로 사용
		// => 주의! 매핑 메서드 리턴타입을 반드시 ModelAndView 타입으로 지정
//		ModelAndView mav = new ModelAndView("test2/model_and_view", "map", map);
//		return mav;
		return new ModelAndView("test2/model_and_view", "map", map);
	}
	
	
	
}














