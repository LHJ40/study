package com.itwillbs.fintech.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.fintech.service.BoardService;
import com.itwillbs.fintech.vo.BoardVO;
import com.itwillbs.fintech.vo.PageInfoVO;
import com.itwillbs.fintech.vo.TinyReplyBoardVO;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService service;
	
	// 글쓰기 폼
	@GetMapping("BoardWriteForm")
	public String writeForm(HttpSession session, Model model) {
		// 세션 아이디가 존재하지 않으면(미로그인) "로그인 필수!" 출력 후 이전 페이지 돌아가기 처리
		String sId = (String)session.getAttribute("sId");
		if(sId == null) {
			model.addAttribute("msg", "로그인 필수!");
			return "fail_back";
		}
		
		return "board/board_write_form";
	}
	
	// 글쓰기 비즈니스 로직 요청
	@PostMapping("BoardWritePro")
	public String writePro(BoardVO board, HttpSession session, Model model, HttpServletRequest request) {
		String sId = (String)session.getAttribute("sId");
		if(sId == null) {
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "fail_back";
		}
		
//		System.out.println(board);
		
		// 이클립스 프로젝트 상에 업로드 폴더(upload) 생성 필요 
		// => 주의! 외부에서 접근하도록 하려면 resources 폴더 내에 upload 폴더 생성
		// 이클립스가 관리하는 프로젝스 상의 가상 업로드 경로에 대한 실제 업로드 경로 알아내기
		// => request.getRealPath() 대신 request.getServletContext.getRealPath() 메서드 또는
		//    세션 객체를 활용한 session.getServletContext().getRealPath() 메서드 사용
//		System.out.println(request.getRealPath("/resources/upload")); // Deprecated 처리된 메서드
		String uploadDir = "/resources/upload"; 
//		String saveDir = request.getServletContext().getRealPath(uploadDir); // 사용 가능
		String saveDir = session.getServletContext().getRealPath(uploadDir);
//		System.out.println("실제 업로드 경로 : "+ saveDir);
		// 실제 업로드 경로 : D:\Shared\Spring\workspace_spring5\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Spring_MVC_Board\resources\ upload
		
		String subDir = ""; // 서브디렉토리(날짜 구분)
		
		try {
			// ------------------------------------------------------------------------------
			// 업로드 디렉토리를 날짜별 디렉토리로 자동 분류하기
			// => 하나의 디렉토리에 너무 많은 파일이 존재하면 로딩 시간 길어지며 관리도 불편
			// => 따라서, 날짜별 디렉토리 구별 위해 java.util.Date 클래스 활용
			// 1. Date 객체 생성(기본 생성자 호출하여 시스템 날짜 정보 활용)
			Date date = new Date(); // Mon Jun 19 11:26:52 KST 2023
//		System.out.println(date);
			// 2. SimpleDateFormat 클래스를 활용하여 날짜 형식을 "yyyy/MM/dd" 로 지정
			// => 디렉토리 구조로 바로 활용하기 위해 날짜 구분 기호를 슬래시(/)로 지정
			// => 디렉토리 구분자를 가장 정확히 표현하려면 File.pathSeperator 또는 File.seperator 상수 활용
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			// 3. 기존 업로드 경로에 날짜 경로 결합하여 저장
			subDir = sdf.format(date);
			saveDir += "/" + subDir;
			// --------------------------------------------------------------
			// java.nio.file.Paths 클래스의 get() 메서드를 호출하여
			// 실제 경로를 관리하는 java.nio.file.Path 타입 객체 리턴받기
			// => 파라미터 : 실제 업로드 경로
			Path path = Paths.get(saveDir);
			
			// Files 클래스의 createDirectories() 메서드를 호출하여
			// Path 객체가 관리하는 경로 생성(존재하지 않으면 거쳐가는 경로들 중 없는 경로 모두 생성)
			Files.createDirectories(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// BoardVO 객체에 전달된 MultipartFile 객체 꺼내기
		MultipartFile mFile1 = board.getFile1();
		MultipartFile mFile2 = board.getFile2();
		MultipartFile mFile3 = board.getFile3();
//		System.out.println("원본파일명1 : " + mFile1.getOriginalFilename());
//		System.out.println("원본파일명2 : " + mFile2.getOriginalFilename());
//		System.out.println("원본파일명3 : " + mFile3.getOriginalFilename());
		
		// 파일명 중복 방지를 위한 대첵
		// 현재 시스템(서버)에서 랜덤ID 값을 추출하여 파일명 앞에 붙여서
		// "랜덤ID값_파일명.확장자" 형식으로 중복 파일명 처리
		// => 랜덤ID 생성은 java.util.UUID 클래스 활용(UUID = 범용 고유 식별자)
		String uuid = UUID.randomUUID().toString();
//		System.out.println("uuid : " + uuid);
		
		// 생성된 UUID 값을 원본 파일명 앞에 결합(파일명과 구분을 위해 _ 기호 추가)
		// => 나중에 사용자 다운로드 시 원본 파일명 표시를 위해 분리할 때 구분자로 사용
		//    (가장 먼저 만나는 _ 기호를 기준으로 문자열 분리하여 처리)
		// => 단, 파일명 길이 조절을 위해 임의로 UUID 중 맨 앞자리 8자리 문자열만 활용
//		System.out.println(uuid.substring(0, 8));
		// 생성된 UUID 값(8자리 추출)과 업로드 파일명을 결합하여 BoardVO 객체에 저장(구분자로 _ 기호 추가)
		// => 단, 파일명이 존재하는 경우에만 파일명 생성(없을 경우를 대비하여 기본 파일명 널스트링으로 처리)
		board.setBoard_file1("");
		board.setBoard_file2("");
		board.setBoard_file3("");
		
		// 파일명을 저장할 변수 선언
		String fileName1 = uuid.substring(0, 8) + "_" + mFile1.getOriginalFilename();
		String fileName2 = uuid.substring(0, 8) + "_" + mFile2.getOriginalFilename();
		String fileName3 = uuid.substring(0, 8) + "_" + mFile3.getOriginalFilename();
		
		if(!mFile1.getOriginalFilename().equals("")) {
			board.setBoard_file1(subDir + "/" + fileName1);
		}
		
		if(!mFile2.getOriginalFilename().equals("")) {
			board.setBoard_file2(subDir + "/" + fileName2);
		}
		
		if(!mFile3.getOriginalFilename().equals("")) {
			board.setBoard_file3(subDir + "/" + fileName3);
		}
		
		System.out.println("실제 업로드 파일명1 : " + board.getBoard_file1());
		System.out.println("실제 업로드 파일명2 : " + board.getBoard_file2());
		System.out.println("실제 업로드 파일명3 : " + board.getBoard_file3());
		
		// -----------------------------------------------------------------------------------
		// BoardService - registBoard() 메서드를 호출하여 게시물 등록 작업 요청
		// => 파라미터 : BoardVO 객체    리턴타입 : int(insertCount)
		int insertCount = service.registBoard(board);
		
		// 게시물 등록 작업 요청 결과 판별
		// => 성공 시 업로드 파일을 실제 디렉토리에 이동시킨 후 BoardList 서블릿 리다이렉트
		// => 실패 시 "글 쓰기 실패!" 메세지 출력 후 이전페이지 돌아가기 처리
		if(insertCount > 0) { // 성공
			try {
				// 업로드 된 파일은 MultipartFile 객체에 의해 임시 디렉토리에 저장되어 있으며
				// 글쓰기 작업 성공 시 임시 디렉토리 -> 실제 디렉토리로 이동 작업 필요
				// MultipartFile 객체의 transferTo() 메서드를 호출하여 실제 위치로 이동(업로드)
				// => 비어있는 파일은 이동할 수 없으므로(= 예외 발생) 제외
				// => File 객체 생성 시 지정한 디렉토리에 지정한 이름으로 파일이 이동(생성)됨
				//    따라서, 이동할 위치의 파일명도 UUID 가 결합된 파일명을 지정해야한다!
				if(!mFile1.getOriginalFilename().equals("")) {
					mFile1.transferTo(new File(saveDir, fileName1));
				}
				
				if(!mFile2.getOriginalFilename().equals("")) {
					mFile2.transferTo(new File(saveDir, fileName2));
				}
				
				if(!mFile3.getOriginalFilename().equals("")) {
					mFile3.transferTo(new File(saveDir, fileName3));
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// 글쓰기 작업 성공 시 글목록(BoardList)으로 리다이렉트
			return "redirect:/BoardList";
		} else { // 실패
			model.addAttribute("msg", "글 쓰기 실패!");
			return "fail_back";
		}
		
	}
	
	// "BoardList" 서블릿 요청에 대해 글목록 조회 비즈니스 로직 요청
	// => 파라미터 : 검색타입(searchType) => 기본값 널스트링("") 으로 설정
	//               검색어(searchKeyword) => 기본값 널스트링("") 으로 설정
	//               페이지번호(pageNum) => 기본값 0 으로 설정
	//               데이터공유객체(model)
//	@GetMapping("BoardList")
//	public String list(
//			@RequestParam(defaultValue = "") String searchType, 
//			@RequestParam(defaultValue = "") String searchKeyword, 
//			@RequestParam(defaultValue = "1") int pageNum, 
//			Model model) {
//		
//		System.out.println("검색타입 : " + searchType);
//		System.out.println("검색어 : " + searchKeyword);
//		// -------------------------------------------------------------------------
//		// 페이징 처리를 위해 조회 목록 갯수 조절 시 사용될 변수 선언
//		int listLimit = 10; // 한 페이지에서 표시할 목록 갯수 지정
//		int startRow = (pageNum - 1) * listLimit; // 조회 시작 행(레코드) 번호
//		// -------------------------------------------------------------------------
//		// BoardService - getBoardList() 메서드 호출하여 게시물 목록 조회 요청
//		// => 파라미터 : 검색타입, 검색어, 시작행번호, 목록갯수
//		// => 리턴타입 : List<BoardVO>(boardList)
//		List<BoardVO> boardList = service.getBoardList(searchType, searchKeyword, startRow, listLimit);
////		System.out.println(boardList);
//		// -------------------------------------------------------------------------
//		// 페이징 처리를 위한 계산 작업
//		// 한 페이지에서 표시할 페이지 목록(번호) 계산
//		// 1. BoardService - getBoardListCount() 메서드를 호출하여
//		//    전체 게시물 수 조회 요청(페이지 목록 계산에 활용)
//		// => 파라미터 : 검색타입, 검색어   리턴타입 : int(listCount)
//		int listCount = service.getBoardListCount(searchType, searchKeyword);
////		System.out.println("전체 게시물 수 : " + listCount);
//		
//		// 2. 한 페이지에서 표시할 목록 갯수 설정(페이지 번호의 갯수)
//		int pageListLimit = 2;
//		
//		// 3. 전체 페이지 목록 갯수 계산
//		int maxPage = listCount / listLimit + (listCount % listLimit > 0 ? 1 : 0);
////		System.out.println("전체 페이지 목록 갯수 : " + maxPage);
//		
//		// 4. 시작 페이지 번호 계산
//		int startPage = (pageNum - 1) / pageListLimit * pageListLimit + 1;
////		System.out.println(startPage);
//		
//		// 5. 끝 페이지 번호 계산
//		int endPage = startPage + pageListLimit - 1;
//		
//		// 6. 만약, 끝 페이지 번호(endPage)가 전체(최대) 페이지 번호(maxPage) 보다
//		//    클 경우 끝 페이지 번호를 최대 페이지 번호로 교체
//		if(endPage > maxPage) {
//			endPage = maxPage;
//		}
////		System.out.println(endPage);
//		
//		// 페이징 처리 정보를 저장할 PageInfoVO 객체에 계산된 데이터 저장
//		PageInfoVO pageInfo = new PageInfoVO(listCount, pageListLimit, maxPage, startPage, endPage);
//		// -----------------------------------------------------------------------------------------
//		// 조회된 게시물 목록 객쳬(boardList) 와 페이징 정보 객체(pageInfo) 를 Model 객체에 저장
//		model.addAttribute("boardList", boardList);
//		model.addAttribute("pageInfo", pageInfo);
//		
//		return "board/board_list";
//	}
	
	@GetMapping("BoardList")
	public String list() {
		return "board/board_list_ajax";
	}
	
	// AJAX 요청을 통한 글목록 조회
	// => AJAX 요청에 대한 글목록 데이터를 JSON 데이터 형식으로 응답
	// => 현재 컨트롤러 메서드에서 JSON 타입 응답데이터를 바로 리턴하기 위해
	//    @ResponseBody 어노테이션 필요
	// => 응답데이터를 JSON 형식의 문자열로 리턴하기 위해 리턴타입을 String 으로 지정
	//    (만약, 출력스트림을 사용하려면 void 타입을 지정)
	
	@ResponseBody
	@GetMapping("BoardListJson")
	public String listJson(
			@RequestParam(defaultValue = "") String searchType, 
			@RequestParam(defaultValue = "") String searchKeyword, 
			@RequestParam(defaultValue = "1") int pageNum, 
			Model model,
			HttpServletResponse response) {

		// -------------------------------------------------------------------------
		// 페이징 처리를 위해 조회 목록 갯수 조절 시 사용될 변수 선언
		int listLimit = 10; // 한 페이지에서 표시할 목록 갯수 지정
		int startRow = (pageNum - 1) * listLimit; // 조회 시작 행(레코드) 번호
		// -------------------------------------------------------------------------
		// BoardService - getBoardList() 메서드 호출하여 게시물 목록 조회 요청
		// => 파라미터 : 검색타입, 검색어, 시작행번호, 목록갯수
		// => 리턴타입 : List<BoardVO>(boardList)
		List<BoardVO> boardList = service.getBoardList(searchType, searchKeyword, startRow, listLimit);
//		System.out.println(boardList);
		// -------------------------------------------------------------------------
//		// 페이징 처리를 위한 계산 작업
//		// 한 페이지에서 표시할 페이지 목록(번호) 계산
//		// 1. BoardService - getBoardListCount() 메서드를 호출하여
//		//    전체 게시물 수 조회 요청(페이지 목록 계산에 활용)
//		// => 파라미터 : 검색타입, 검색어   리턴타입 : int(listCount)
		int listCount = service.getBoardListCount(searchType, searchKeyword);
//		System.out.println("전체 게시물 수 : " + listCount);
		
		// 2. 전체 페이지 목록 갯수 계산
		int maxPage = listCount / listLimit + (listCount % listLimit > 0 ? 1 : 0);
//		System.out.println("전체 페이지 목록 갯수 : " + maxPage);
		// -------------------------------------------------------------------------
		// 자바 객체(데이터)를 JSON 형식의 객체로 변환
		// => org.json 패키지의 JSONObject 또는 JSONArray 클래스를 활용하여 JSON 객체 다룰 수 있음
		// => JSONObject 클래스는 1개의 객체를 관리하고, JSONArray 클래스는 배열(리스트 포함)을 관리함
		// => 즉, JSONArray 클래스를 활용하여 JSONObject 객체 복수개 또는 복수개의 데이터 관리 가능함
		// 1. JSONObject 객체 복수개를 저장할 JSONArray 클래스의 인스턴스 생성
		//    => 이 때, 파라미터로 List 객체(= 컬렉션) 전달 시 전체를 JSON 객체 형식으로 변환 가능
//		JSONArray jsonArray = new JSONArray(boardList);
		// => BoardVO 객체 여러개가 저장된 List 객체를 JSON 형식으로 변환
		// => 만약, BoardVO 객체 1개일 경우 JSONObject 객체 생성자에 전달 시 1개 객체를 JSON 형식으로 변환
//		System.out.println(jsonArray);
		
		// 2. 생성된 JSON 객체를 그대로 리턴(문자열로 변환은 필요)
//		return jsonArray.toString();
		
		// ------------------------------------------------------------------------------------
		// 최대 페이지번호(maxPage)값도 JSON 데이터로 함께 넘기려면
		// 기존 목록을 JSONObject 객체를 통해 객체 형태로 변환하고, 최대 페이지번호도 함께 추가
		JSONObject jsonObject = new JSONObject();
		// JSONXXX 객체의 put() 메서드를 사용하여 데이터 추가 가능
		jsonObject.put("boardList", boardList);
		jsonObject.put("maxPage", maxPage);
		
//		System.out.println(jsonObject);
		
		return jsonObject.toString();
	}
	
	// "BoardDetail" 서블릿 요청에 대한 글 상세정보 조회 요청
	@GetMapping("BoardDetail")
	public String detail(@RequestParam int board_num, Model model) {
		// BoardService - getBoard() 메서드 호출하여 글 상세정보 조회 요청
		// => 파라미터 : 글번호   리턴타입 : BoardVO 객체(board)
		BoardVO board = service.getBoard(board_num);
		
		// 상세정보 조회 결과 저장
		model.addAttribute("board", board);
		
		// ---------------------------------------------------------------------
		// 해당 게시물에 포함된 댓글 목록 조회
		// Service - getTinyReplyBoardList() 메서드 호출하여 댓글 목록 조회
		// => 파라미터 : 글번호   리턴타입 : List<TinyReplyBoardVO>(tinyReplyBoardList)
		List<TinyReplyBoardVO> tinyReplyBoardList = service.getTinyReplyBoardList(board.getBoard_num());
		
		// Model 객체에 댓글 목록 추가(tinyReplyBoardList)
		model.addAttribute("tinyReplyBoardList", tinyReplyBoardList);
		// ---------------------------------------------------------------------
		
		return "board/board_view";
	}
	
	// "BoardDelete" 서블릿 요청에 대한 글 삭제 요청
	@GetMapping("BoardDelete")
	public String delete(
			@RequestParam int board_num, 
			@RequestParam(defaultValue = "1") int pageNum, 
			HttpSession session, Model model) {
		// 세션 아이디가 존재하지 않으면(미로그인) "잘못된 접근입니다!" 출력 후 이전 페이지 돌아가기 처리
		String sId = (String)session.getAttribute("sId");
		if(sId == null) {
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "fail_back";
		}
		
		// BoardService - isBoardWriter() 메서드 호출하여 작성자 판별 요청
		// => 파라미터 : 글번호, 세션아이디   리턴타입 : boolean(isBoardWriter)
		// => 단, 세션아이디가 "admin" 이 아닐 경우에만 수행
		if(!sId.equals("admin")) {
			boolean isBoardWriter = service.isBoardWriter(board_num, sId);
			
			if(!isBoardWriter) {
				model.addAttribute("msg", "권한이 없습니다!");
				return "fail_back";
			}
		}
		
		// BoardService - removeBoard() 메서드 호출하여 글 삭제 요청
		// => 파라미터 : 글번호   리턴타입 : int(deleteCount)
		int deleteCount = service.removeBoard(board_num);
		
		// 삭제 실패 시 "삭제 실패!" 처리 후 이전페이지 이동
		// 아니면, BoardList 서블릿 요청(파라미터 : 페이지번호)
		if(deleteCount == 0) {
			model.addAttribute("msg", "삭제 실패!");
			return "fail_back";
		} 
		
		return "redirect:/BoardList?pageNum=" + pageNum;
	}
	
	@GetMapping("BoardReplyForm")
	public String replyForm(
			@RequestParam int board_num, 
			@RequestParam(defaultValue = "1") int pageNum, 
			HttpSession session, Model model) {
		// 세션 아이디가 존재하지 않으면(미로그인) "잘못된 접근입니다!" 출력 후 이전 페이지 돌아가기 처리
		String sId = (String)session.getAttribute("sId");
		if(sId == null) {
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "fail_back";
		}
		
		// Service - getBoard() 메서드 호출하여 게시물 조회(재사용)
		BoardVO board = service.getBoard(board_num);
		
		model.addAttribute("board", board);
		
		return "board/board_reply_form";
	}
	
	@PostMapping("BoardReplyPro")
	public String replyPro(
			BoardVO board, 
			@RequestParam(defaultValue = "1") int pageNum,
			HttpSession session, Model model, HttpServletRequest request) {
		String sId = (String)session.getAttribute("sId");
		if(sId == null) {
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "fail_back";
		}
		
//		System.out.println(board);
		
		String uploadDir = "/resources/upload"; 
		String saveDir = session.getServletContext().getRealPath(uploadDir);
		String subDir = ""; // 서브디렉토리(날짜 구분)
		
		try {
			Date date = new Date(); // Mon Jun 19 11:26:52 KST 2023
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			subDir = sdf.format(date);
			saveDir += "/" + subDir;
			// --------------------------------------------------------------
			Path path = Paths.get(saveDir);
			Files.createDirectories(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// BoardVO 객체에 전달된 MultipartFile 객체 꺼내기
		MultipartFile mFile1 = board.getFile1();
		MultipartFile mFile2 = board.getFile2();
		MultipartFile mFile3 = board.getFile3();
		
		String uuid = UUID.randomUUID().toString();
		board.setBoard_file1("");
		board.setBoard_file2("");
		board.setBoard_file3("");
		
		// 파일명을 저장할 변수 선언
		String fileName1 = uuid.substring(0, 8) + "_" + mFile1.getOriginalFilename();
		String fileName2 = uuid.substring(0, 8) + "_" + mFile2.getOriginalFilename();
		String fileName3 = uuid.substring(0, 8) + "_" + mFile3.getOriginalFilename();
		
		if(!mFile1.getOriginalFilename().equals("")) {
			board.setBoard_file1(subDir + "/" + fileName1);
		}
		
		if(!mFile2.getOriginalFilename().equals("")) {
			board.setBoard_file2(subDir + "/" + fileName2);
		}
		
		if(!mFile3.getOriginalFilename().equals("")) {
			board.setBoard_file3(subDir + "/" + fileName3);
		}
		
		System.out.println("실제 업로드 파일명1 : " + board.getBoard_file1());
		System.out.println("실제 업로드 파일명2 : " + board.getBoard_file2());
		System.out.println("실제 업로드 파일명3 : " + board.getBoard_file3());
		
		// -----------------------------------------------------------------------------------
		// BoardService - registReplyBoard() 메서드를 호출하여 답글 등록 작업 요청
		// => 파라미터 : BoardVO 객체    리턴타입 : int(insertCount)
		int insertCount = service.registReplyBoard(board);
		
		// 답글 등록 작업 요청 결과 판별
		// => 성공 시 업로드 파일을 실제 디렉토리에 이동시킨 후 BoardList 서블릿 리다이렉트
		// => 실패 시 "글 쓰기 실패!" 메세지 출력 후 이전페이지 돌아가기 처리
		if(insertCount > 0) { // 성공
			try {
				if(!mFile1.getOriginalFilename().equals("")) {
					mFile1.transferTo(new File(saveDir, fileName1));
				}
				
				if(!mFile2.getOriginalFilename().equals("")) {
					mFile2.transferTo(new File(saveDir, fileName2));
				}
				
				if(!mFile3.getOriginalFilename().equals("")) {
					mFile3.transferTo(new File(saveDir, fileName3));
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// 답글쓰기 작업 성공 시 글목록(BoardList)으로 리다이렉트
			return "redirect:/BoardList?pageNum=" + pageNum;
		} else { // 실패
			model.addAttribute("msg", "답글 쓰기 실패!");
			return "fail_back";
		}
		
	}
	
	@GetMapping("BoardModifyForm")
	public String modifyForm(
			@RequestParam int board_num, 
			@RequestParam(defaultValue = "1") int pageNum, 
			HttpSession session, Model model) {
		// 세션 아이디가 존재하지 않으면(미로그인) "잘못된 접근입니다!" 출력 후 이전 페이지 돌아가기 처리
		String sId = (String)session.getAttribute("sId");
		if(sId == null) {
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "fail_back";
		}
		
		// Service - getBoard() 메서드 호출하여 게시물 조회(재사용)
		BoardVO board = service.getBoard(board_num);
		
		model.addAttribute("board", board);
		
		return "board/board_modify_form";
	}
	
	@PostMapping("BoardModifyPro")
	public String modifyPro(
			BoardVO board, 
			@RequestParam(defaultValue = "1") int pageNum,
			HttpSession session, Model model, HttpServletRequest request) {
		String sId = (String)session.getAttribute("sId");
		if(sId == null) {
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "fail_back";
		}
		
//		System.out.println(board);
		
		String uploadDir = "/resources/upload"; 
		String saveDir = session.getServletContext().getRealPath(uploadDir);
		String subDir = ""; // 서브디렉토리(날짜 구분)
		
		try {
			Date date = new Date(); // Mon Jun 19 11:26:52 KST 2023
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			subDir = sdf.format(date);
			saveDir += "/" + subDir;
			// --------------------------------------------------------------
			Path path = Paths.get(saveDir);
			Files.createDirectories(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// BoardVO 객체에 전달된 MultipartFile 객체 꺼내기
		MultipartFile mFile1 = board.getFile1();
		MultipartFile mFile2 = board.getFile2();
		MultipartFile mFile3 = board.getFile3();
		
		String uuid = UUID.randomUUID().toString();
		board.setBoard_file1("");
		board.setBoard_file2("");
		board.setBoard_file3("");
		
		// 파일명을 저장할 변수 선언
		String fileName1 = uuid.substring(0, 8) + "_" + mFile1.getOriginalFilename();
		String fileName2 = uuid.substring(0, 8) + "_" + mFile2.getOriginalFilename();
		String fileName3 = uuid.substring(0, 8) + "_" + mFile3.getOriginalFilename();
		
		if(!mFile1.getOriginalFilename().equals("")) {
			board.setBoard_file1(subDir + "/" + fileName1);
		}
		
		if(!mFile2.getOriginalFilename().equals("")) {
			board.setBoard_file2(subDir + "/" + fileName2);
		}
		
		if(!mFile3.getOriginalFilename().equals("")) {
			board.setBoard_file3(subDir + "/" + fileName3);
		}
		
		System.out.println("실제 업로드 파일명1 : " + board.getBoard_file1());
		System.out.println("실제 업로드 파일명2 : " + board.getBoard_file2());
		System.out.println("실제 업로드 파일명3 : " + board.getBoard_file3());
		
		// -----------------------------------------------------------------------------------
		// BoardService - modifyBoard() 메서드를 호출하여 글 수정 작업 요청
		// => 파라미터 : BoardVO 객체    리턴타입 : int(updateCount)
		int updateCount = service.modifyBoard(board);
		
		// 답글 등록 작업 요청 결과 판별
		// => 성공 시 업로드 파일을 실제 디렉토리에 이동시킨 후 BoardList 서블릿 리다이렉트
		// => 실패 시 "글 쓰기 실패!" 메세지 출력 후 이전페이지 돌아가기 처리
		if(updateCount > 0) { // 성공
			try {
				if(!mFile1.getOriginalFilename().equals("")) {
					mFile1.transferTo(new File(saveDir, fileName1));
				}
				
				if(!mFile2.getOriginalFilename().equals("")) {
					mFile2.transferTo(new File(saveDir, fileName2));
				}
				
				if(!mFile3.getOriginalFilename().equals("")) {
					mFile3.transferTo(new File(saveDir, fileName3));
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// 수정 작업 성공 시 글 상세정보 조회(BoardDetail)로 리다이렉트(글번호, 페이지번호 전달)
			return "redirect:/BoardList?board_num=" + board.getBoard_num() + "&pageNum=" + pageNum;
		} else { // 실패
			model.addAttribute("msg", "글 수정 실패!");
			return "fail_back";
		}
		
	}
	
	// ==========================================================================
	// 게시물 내의 댓글 작성 비즈니스 로직 요청
	@PostMapping("BoardTinyReplyWrite")
	public String tinyReplyWrite(TinyReplyBoardVO board, HttpSession session, Model model) {
		String sId = (String)session.getAttribute("sId");
		if(sId == null) {
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "fail_back";
		}
		
		// Service - registTinyReplyBoard() 메서드를 호출하여 댓글 등록 작업 요청
		// => 파라미터 : TinyReplyBoardVO 객체    리턴타입 : int(insertCount)
		int insertCount = service.registTinyReplyBoard(board);
		// 만약, 자동 증가된 reply_num 값이 필요할 경우 현재 컨트롤러에서도 VO 객체의 reply_num 값 접근 가능
		System.out.println("자동 증가된 reply_num = " + board.getReply_num());
		
		// 댓글 등록 작업 결과 판별
		// => 성공 시 "BoardDetail" 페이지 리다이렉트(글번호 전달)
		//    실패 시 "댓글 쓰기 실패!" 메세지와 함께 fail_back.jsp 페이지로 포워딩
		if(insertCount > 0) {
			return "redirect:/BoardDetail?board_num=" + board.getBoard_num();
		} else {
			model.addAttribute("msg", "댓글 쓰기 실패!");
			return "fail_back";
		}
		
	}
	
	// "BoardTinyReplyDelete" 서블릿 요청에 대한 글 삭제 요청(AJAX 요청 응답 문자열 리턴)
	@ResponseBody
	@GetMapping("BoardTinyReplyDelete")
	public String deleteTinyReply(TinyReplyBoardVO board, HttpSession session) {
		// 세션 아이디가 존재하지 않으면(미로그인) "잘못된 접근입니다!" 출력 후 이전 페이지 돌아가기 처리
		// 또한, 관리자가 아니고 세션아이디와 작성자 아이디가 다르면 "잘못된 접근입니다!" 
		// => 이 때, 작성자 아이디는 댓글 번호를 통해 조회 후 판별(service - getTinyReplyWriter(board))
		String sId = (String)session.getAttribute("sId");
		if(sId == null || !sId.equals("admin") && !service.getTinyReplyWriter(board).equals(sId)) {
			return "false";
		}
		
		// BoardService - removeTinyReplyBoard() 메서드 호출하여 댓글 삭제 요청
		// => 파라미터 : TinyReplyBoardVO 객체   리턴타입 : int(deleteCount)
		int deleteCount = service.removeTinyReplyBoard(board);
		
		// 삭제 실패 시 "삭제 실패!" 처리 후 이전페이지 이동
		// 아니면, BoardList 서블릿 요청(파라미터 : 페이지번호)
		if(deleteCount == 0) {
			return "false";
		} else {
			return "true";
		}
	}
	
	// "BoardTinyReReplyWrite" 서블릿 요청에 대한 대댓글 작성 요청(AJAX 요청 응답 문자열 리턴)
	@ResponseBody
	@PostMapping("BoardTinyReReplyWrite")
	public String writeTinyReReply(TinyReplyBoardVO board, HttpSession session) {
		System.out.println(board);
		
		// 세션 아이디가 존재하지 않으면(미로그인) "false" 리턴
		String sId = (String)session.getAttribute("sId");
		if(sId == null) {
			return "false";
		}
		
		// BoardService - writeTinyReReplyBoard() 메서드 호출하여 대댓글 작성 요청
		// => 파라미터 : TinyReplyBoardVO 객체   리턴타입 : int(insertCount)
		int insertCount = service.writeTinyReReplyBoard(board);
		
		// 작성 실패 시 "false" 리턴, 아니면 "true" 리턴
		if(insertCount == 0) {
			return "false";
		} else {
			return "true";
		}
	}
	

	
}















