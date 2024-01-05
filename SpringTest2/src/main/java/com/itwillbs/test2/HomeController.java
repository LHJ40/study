package com.itwillbs.test2;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 * 웹 요청들을 처리하기 위한 홈페이지 역할을 수행하는 클래스(= 컨트롤러)
 * => 반드시 클래스 선언부 위에 @Controller 어노테이션 붙임
 * => 컨트롤러 클래스 내에서 메서드 단위로 매핑 가능
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/*
	 * @RequestMapping 어노테이션을 사용하여 URL 매핑 작업 수행
	 * - 파라미터 중 value 속성값에 지정된 URL 을 매핑 주소로 사용하며
	 *   method 속성값에 지정된 RequestMethod.XXX 방식을 요청 메서드로 인식
	 *   (이 때, method 속성값은 복수개 지정 가능하며, 중괄호로 묶어서 복수개의 방식을 모두 처리)
	 *   (ex. method = RequestMethod.GET 지정 시 GET 방식 요청만 처리하며
	 *        method = {RequestMethod.GET, RequestMethod.POST} 지정 시 GET 방식, POST 방식 요청을 모두 처리)
	 * - 단, 스프링4 버전부터 지원되는 @GetMapping, @PostMapping 어노테이션을 통해
	 *   좀 더 간단히 매핑이 가능하나 유연성은 @RequestMapping 에 비해 떨어짐(상황에 맞는 방식 사용)
	 * - 형식 상의 메서드를 정의하여 해당 URL 매핑 시 자동으로 메서드가 호출되도록 정의(메서드별 매핑 이루어짐)
	 *   => 메서드 파라미터 : 상황에 따라 없을 수도 있고, 객체 등의 변수 선언 가능
	 *   => 메서드 리턴타입 : String 또는 다른 타입 지정 가능
	 */
	
	// 현재 프로젝트에 서블릿 주소("/")가 GET 방식으로 요청되면 자동으로 home() 메서드가 호출됨
//	@RequestMapping(value = "/", method = RequestMethod.GET)
	// 현재 프로젝트에 서블릿 주소("/")가 GET 또는 POST 방식으로 요청되면 자동으로 home() 메서드가 호출됨
	@RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		/*
		 * WEB-INF/views 디렉토리에 위치한 특정 jsp 페이지로 포워딩(Dispatcher 방식)할 경우
		 * return 문 뒤에 이동할 포워딩 대상 파일명만 지정(views 까지의 디렉토리 구조 및 확장자 생략)
		 * => servlet-context.xml 파일에 기술된 InternalResourceViewResolver 에 의해
		 *    prefix 값과 suffix 값을 return 문 뒤의 문자열과 결합하여 포워딩 할 페이지 경로가 생성됨
		 * 
		 * [ servlet-context.xml 파일의 내용 일부 ]
		 * <!-- Resolves views selected for rendering by @Controllers to .jsp resources in the /WEB-INF/views directory -->
		 * 	<beans:bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		 * 		<beans:property name="prefix" value="/WEB-INF/views/" />
		 * 		<beans:property name="suffix" value=".jsp" />
		 * 	</beans:bean>
		 * => bean:property 태그의 prefix 값으로 지정된 "/WEB-INF/views/" 문자열을
		 *    return 문 뒤에 기술된 "home" 문자열의 앞에 결합하고
		 *    suffix 부분에 기술된 ".jsp" 문자열을 "home" 문자열 뒤에 결합하여
		 *    "webapp/WEB-INF/views/home.jsp" 문자열이 생성되고
		 *    DispatcherServlet 객체에 의해 포워딩 작업이 이루어진다!
		 */
		return "home"; // "webapp/WEB-INF/views/home.jsp" 페이지 지정
	}
	
	// GET 방식으로 "/main" 서블릿 주소가 요청되면
	// WEB-INF/views/index.jsp 페이지로 포워딩(Dispatcher 방식)
//	@RequestMapping(value = "/main", method = RequestMethod.GET)
//	public String main() {
//		// webapp 디렉토리의 WEB-INF/views 디렉토리에 있는 index.jsp 페이지로 포워딩
//		return "index";
//	}
	
}












