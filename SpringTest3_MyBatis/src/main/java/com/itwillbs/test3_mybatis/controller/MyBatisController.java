package com.itwillbs.test3_mybatis.controller;


import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.test3_mybatis.service.StudentService;
import com.itwillbs.test3_mybatis.vo.Movie;
import com.itwillbs.test3_mybatis.vo.StudentVO;

@Controller
public class MyBatisController {
	/*
	 * 컨트롤러 클래스가 서비스 클래스에 의존적일 때
	 * 서비스 클래스 인스턴스를 직접 생성하지 않고, 자동 주입(DI)을 통해 접근할 수
	 * => @Inject(자바-플랫폼 공용) 또는 @Autowired(스프링 전용) 어노테이션 사용
	 * => 어노테이션 지정 후 자동 주입으로 객체 생성 시 객체가 저장될 클래스 타입 변수 선언
	 * => 자동 주입받는 클래스는 용도에 따라 @Service, @Component 등의 어노테이션을 적용하여 정의
	 */
	@Autowired
	private StudentService service;
	
	// "/main" 서블릿 주소 매핑하는 main() 메서드 정의 - GET
	// => main.jsp 페이지로 포워딩
	@GetMapping("main")
	public String main() {
		return "main";
	}
	
	// "registStudent" 매핑 registStudent() 메서드 정의 - GET
	// => regist_form.jsp 포워딩
	@GetMapping("registStudent")
	public String registStudent() {
		return "regist_form";
	}
	
	// "registStudentPro" 매핑 registStudentPro() 메서드 정의 - POST
	// => 폼 파라미터 데이터를 전송받아 저장할 StudentVO 타입 파라미터 설정
	@PostMapping("registStudentPro")
	public String registStudentPro(StudentVO student, Model model) {
		System.out.println(student);
		
		// StudentService - registStudent() 메서드를 호출하여 학생 정보 등록 요청
		// => 파라미터 : StudentVO 객체   리턴타입 : int(insertCount)
//		StudentService service = new StudentService(); // 객체 자동 주입으로 인해 객체 생성 불필요
		// => 단, @Service 어노테이션이 적용된 Service 클래스 정의 및 @Autowired 필수!
		// => 해당 인스턴스 생성 없이도 자동 주입되므로 service 멤버변수를 바로 사용 가능
		int insertCount = service.registStudent(student);
		
		// 등록 실패(insertCount == 0) 시 Model 객체에 "등록 실패!" 저장(속성명 msg) 후 fail_back.jsp 로 포워딩
		if(insertCount == 0) {
			model.addAttribute("msg", "등록 실패!");
			return "fail_back";
		}
		
		// studentList 서블릿 주소로 리다이렉트
		return "redirect:/studentList";
	}
	
	// 학생 정보 조회
	// "studentInfo" 매핑 studentInfo() 메서드 정의 - GET
	// => 파라미터 : 학생 이메일(email)
	// => 포워딩 페이지 : student_info.jsp
	@GetMapping("studentInfo")
	public String studentInfo(@RequestParam String email, Model model) {
		// 학생 정보 조회를 위해 StudentService - getStudent() 메서드 호출
		// => 파라미터 : 이메일(email)    리턴타입 : StudentVO(student)
		StudentVO student = service.getStudent(email);
//		System.out.println(student);
		
		// student_info.jsp 페이지로 포워딩 => StudentVO 객체 전달
		model.addAttribute("student", student);
		
		return "student_info";
	}
	
	// 학생 목록 조회
	// "studentList" 매핑 studentList() 메서드 정의 - GET
	// => student_list.jsp 포워딩
	@GetMapping("studentList")
	public String studentList(Model model) {
		// 학생 목록 조회를 위해 StudentService - getStudentList() 메서드 호출
		// => 파라미터 : 없음    리턴타입 : List<StudentVO>(studentList)
		List<StudentVO> studentList = service.getStudentList();
//		System.out.println(studentList);
		
		// student_list.jsp 페이지로 포워딩 시 전달할 List 객체 저장
		model.addAttribute("studentList", studentList);
		
		return "student_list";
	}
	@GetMapping("testapi")
	public String testapi() throws ParseException {
		String clientID = "Ac0iTMGlvd3VcoxyZKSn";
		String clientSecret = "_vBWUiJ2qC";
		String text =null;

		try {
		    text = URLEncoder.encode("그린팩토리", "UTF-8");
		} catch (UnsupportedEncodingException e) {
		    throw new RuntimeException("검색어 인코딩 실패", e);
		}

		String apiURL = "https://openapi.naver.com/v1/search/movie.json?query="+text+"&display=50&start=11";
		Map < String,String > requestHeaders = new HashMap<>();
		requestHeaders.put("X-Naver-Client-Id", clientID);
		requestHeaders.put("X-Naver-Client-Secret", clientSecret);

		String responseBody = get(apiURL, requestHeaders);
//		String json = responseBody;
//
//		JSONParser parser = new JSONParser();
//		JSONObject obj = (JSONObject)parser.parse(json);
//		JSONArray item = (JSONArray)obj.get("items");
//		List < Movie > list = null;
//		list = new ArrayList<Movie>();
//
//		for (int i = 0; i < item.size(); i ++) {
//		    Movie m = new Movie();
//		    JSONObject tmp = (JSONObject)item.get(i);
//		    String title = (String)tmp.get("title");
//		    String image = (String)tmp.get("image");
//		    String description = (String)tmp.get("description");
//		    String link = (String)tmp.get("link");
//		    String subtitle = (String)tmp.get("subtitle");
//		    m.setTitle(title);
//		    m.setImage(image);
//		    m.setDescription(description);
//		    m.setLink(link);
//		    m.setSubtitle(subtitle);
//		    if (m != null) list.add(m);
//		}
//		for (Movie movie : list) {
//		    System.out.println(movie.getTitle());
//		}
		System.out.println(responseBody);
		return "testapi";
	}
   
	
	private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }
	 private static HttpURLConnection connect(String apiUrl){
	        try {
	            URL url = new URL(apiUrl);
	            return (HttpURLConnection)url.openConnection();
	        } catch (MalformedURLException e) {
	            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
	        } catch (IOException e) {
	            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
	        }
	    }

	    private static String readBody(InputStream body){
	        InputStreamReader streamReader = new InputStreamReader(body);
	        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
	            StringBuilder responseBody = new StringBuilder();
	            String line;
	            while ((line = lineReader.readLine()) != null) {
	                responseBody.append(line);
	            }
	            return responseBody.toString();
	        } catch (IOException e) {
	            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
	        }
	    }


	
}















