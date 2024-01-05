package com.itwillbs.test2.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BoardController {
	
	// "BoardWriteForm" 서블릿 요청(GET) 시
	// test2 디렉토리의 board_write_form.jsp 페이지로 포워딩
	@GetMapping("BoardWriteForm")
	public String writeForm() {
		return "test2/board_write_form";
	}
	
	// "BoardWritePro" 서블릿 요청(POST) 시
	// 폼 파라미터 5개(작성자, 패스워드, 제목, 내용, 파일명) 전달받아 출력 후
	// "BoardList" 서블릿 요청(리다이렉트)
	@PostMapping("BoardWritePro")
	public String writePro(@RequestParam Map<String, String> map) {
		System.out.println("작성자 : " + map.get("board_name"));
		System.out.println("패스워드 : " + map.get("board_pass"));
		System.out.println("제목 : " + map.get("board_subject"));
		System.out.println("내용 : " + map.get("board_content"));
		System.out.println("파일명 : " + map.get("board_file"));
		
		return "redirect:/BoardList";
	}
	
	// "BoardList" 서블릿 요청(GET) 시
	// "글 목록 출력!" 메세지 콘솔에 출력 후
	// test2/board_list.jsp 페이지로 포워딩
	@GetMapping("BoardList")
	public String list() {
		System.out.println("글 목록 출력!");
		
		return "test2/board_list";
	}
	
	
}















