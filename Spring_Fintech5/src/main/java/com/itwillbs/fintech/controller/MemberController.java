package com.itwillbs.fintech.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.fintech.handler.GenerateRandomCode;
import com.itwillbs.fintech.handler.MyPasswordEncoder;
import com.itwillbs.fintech.handler.SendMailClient;
import com.itwillbs.fintech.service.BankService;
import com.itwillbs.fintech.service.MemberService;
import com.itwillbs.fintech.service.SendMailService;
import com.itwillbs.fintech.vo.AuthInfoVO;
import com.itwillbs.fintech.vo.MemberVO;
import com.itwillbs.fintech.vo.ResponseTokenVO;

@Controller
public class MemberController {
	@Autowired
	private MemberService service;

	@Autowired
	private BankService bankService;

	// "/MemberJoinForm" 요청에 대해 "member/member_join_form.jsp" 페이지 포워딩
	// => GET 방식 요청, Dispatch 방식 포워딩
	@GetMapping("MemberJoinForm")
	public String joinForm() {
		return "member/member_join_form";
	}
	
	// "/MemberJoinPro" 요청에 대해 MemberService 객체 비즈니스 로직 수행
	// => POST 방식 요청, Redirect 방식 & Dispatch 방식 포워딩
	// => 폼 파라미터로 전달되는 가입 정보를 파라미터로 전달받기
	// => 가입 완료 후 이동할 페이지 : 가입 성공 페이지(MemberJoinSuccess 서블릿 요청)
	// => 가입 실패 시 오류 페이지(fail_back)를 통해 "회원 가입 실패!" 출력 후 이전페이지로 돌아가기
	// 추가) 패스워드 암호화 기능(BCryptPasswordEncoder 활용) 
	@PostMapping("MemberJoinPro")
	public String joinPro(MemberVO member, Model model) {
		System.out.println(member);
		
		// ------------ BCryptPasswordEncoder 객체 활용한 패스워드 암호화(= 해싱) --------------
		// => MyPasswordEncoder 클래스에 모듈화
		// 1. MyPasswordEncoder 객체 생성
		MyPasswordEncoder passwordEncoder = new MyPasswordEncoder();
		// 2. getCryptoPassword() 메서드에 평문 전달하여 암호문 얻어오기
		String securePasswd = passwordEncoder.getCryptoPassword(member.getPasswd());
		// 3. 리턴받은 암호문을 MemberVO 객체에 덮어쓰기
		member.setPasswd(securePasswd);
		// -------------------------------------------------------------------------------------
		
		// MemberService(registMember()) - MemberMapper(insertMember())
		int insertCount = service.registMember(member);
		
		// 회원 가입 성공/실패에 따른 페이지 포워딩
		// => 성공 시 MemberJoinSuccess 로 리다이렉트
		// => 실패 시 fail_back.jsp 로 포워딩(Model 객체의 "msg" 속성으로 "회원 가입 실패!" 저장)
		if(insertCount > 0) {
			// ------------------------------------------------------------------------------
			// 회원 가입 성공 시 인증 메일 발송 기능 추가
			// SendMailService - sendAuthMail() 메서드를 호출하여 인증 메일 발송 요청
			// => 파라미터 : 아이디, 수신자 메일 주소(= 회원 가입 시 입력한 이메일)
			// => 단, 메일 발송 딜레이로 인해 회원 가입 성공 페이지 이동 작업이 늦어지므로
			//    페이지 포워딩과 메일 발송을 별개로 처리를 위해 Thread 활용
			// Thread 객체를 활용하여 메일 발송 메서드 호출을 멀티쓰레딩으로 처리하고
			// 그 와 별개로 가입 성공 페이지 요청을 수행
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					SendMailService mailService = new SendMailService();
					mailService.sendAuthMail(member.getId(), member.getEmail1() + "@" + member.getEmail2());
				}
			}).start(); // start() 메서드 호출 필수!
			// ------------------------------------------------------------------------------
			return "redirect:/MemberJoinSuccess";
		} else {
			model.addAttribute("msg", "회원 가입 실패!");
			return "fail_back";
		}
	}
	
	// "/MemberJoinSuccess" 요청에 대해 "member/member_join_success.jsp" 페이지 포워딩
	// => GET 방식 요청, Dispatch 방식 포워딩
	@GetMapping("MemberJoinSuccess")
	public String joinSuccess() {
		return "member/member_join_success";
	}
	
	// "MemberCheckDupId" 요청(AJAX) 처리
	// => GET 방식
	// => 이 때, 응답 페이지 지정 대신 원하는 데이터를 body 에 담아 직접 전달하기 위해서는
	//    @ResponseBody 어노테이션 지정한 후 return 문에 응답데이터를 직접 지정하면 된다!
	//    HttpServletResponse 객체에 응답데이터가 담긴 후 클라이언트로 응답 형식으로 전송됨
	@ResponseBody
	@GetMapping("MemberCheckDupId")
	public String checkDupId(@RequestParam String id) {
		System.out.println("아이디 : " + id);
		
		// MemberService - getMemberId() 메서드 호출하여 아이디 조회
		// => 파라미터 : 아이디   리턴타입 : String
//		String dbId = service.getMemberId(id);
		
		// 단, 기존의 MeberService - getMemberInfo() 메서드 재사용도 가능
		// => 파라미터 : 아이디   리턴타입 : MemberVO(member)
		MemberVO member = service.getMemberInfo(id);
		// 조회 결과 판별
		// => MemberVO 객체가 존재할 경우 아이디 중복, 아니면 중복 아님
		if(member != null) { // 중복
			return "true"; // 리턴타입 String 일 때 응답데이터로 String 타입 "true" 문자열 리턴
			// 만약, 리턴값을 boolean 타입으로 전송하려할 경우 리턴타입 boolean 지정 및 true 리턴 가능
		} else { // 중복 아님
			return "false"; // 리턴타입 String 일 때 응답데이터로 String 타입 "false" 문자열 리턴
		}
		
	}
	
	// "/MemberLoginForm" 요청에 대해 "member/member_login_form.jsp" 페이지 포워딩
	// => GET 방식 요청, Dispatch 방식 포워딩
	// => 아이디 저장 기능을 통해 저장된 아이디를 불러오기 위한 Cookie 타입 파라미터 추가 
	//    @CookieValue 어노테이션 사용(value 속성에 쿠키 이름, required 속성에 false 설정)
	@GetMapping("MemberLoginForm")
	public String loginForm(@CookieValue(value = "REMEMBER_ID", required = false) Cookie cookie) {
		// 쿠키 객체가 존재할 경우
		if(cookie != null) {
			System.out.println("저장된 쿠키 확인 : " + cookie.getValue());
		}
		
		return "member/member_login_form";
	}
	
	// "/MemberLoginPro" 요청에 대해 MemberService 객체 비즈니스 로직 수행
	// => POST 방식 요청, Redirect 방식 & Dispatch 방식 포워딩
	// => 폼 파라미터로 전달되는 로그인 정보를 파라미터(MemberVO 타입)로 전달받기
	// => 로그인 완료 후 이동할 페이지 : 메인 페이지
	// => 로그인 실패 시 오류 페이지(fail_back)를 통해 "로그인 실패!" 출력 후 이전페이지로 돌아가기
	// => 세션 객체 활용을 위해 파라미터 타입 HttpSession 선언
	// -----------------------------------------------------------------------------
	// => 아이디 저장 기능을 위해 rememberId 파라미터 추가
	//    체크박스 상태 여부 확인을 위해서는 boolean 타입 파라미터가 더 효율적
	//    (@RequestParam 어노테이션 불필요, 만약 어노테이션 지정 시 required = false 추가)
	// => 쿠키 객체 저장을 위한 HttpServletResponse 타입 파라미터 추가
	// -----------------------------------------------------------------------------
	// => 암호화 추가
	@PostMapping("MemberLoginPro")
	public String loginPro(
			MemberVO member, @RequestParam(required = false) boolean rememberId, 
			Model model, HttpSession session, HttpServletResponse response) {
		// --------------------------- 기존 로그인 방식 ----------------------------
		// MemberService - selectCorrectUser() 메서드를 호출하여
		// member 테이블에서 id 와 passwd 가 일치하는 레코드 조회 요청
		// => 파라미터 : MemberVO 객체   리턴타입 : MemberVO(memberResult)
//		MemberVO memberResult = service.selectCorrectUser(member);
//		System.out.println(memberResult);
		
		// 로그인 성공/실패 여부 판별하여 포워딩
		// => 성공 시 MemberVO 객체에 데이터가 저장되어 있음, 실패 시 MemberVO 객체가 null
//		if(memberResult == null) {
//			model.addAttribute("msg", "로그인 실패!");
//			return "fail_back";
//		} else {
//			return "redirect:/"; // 메인페이지(루트)로 리다이렉트
//		}
		// --------------------------------------------------------------
		// MemberService - getPasswd() 메서드를 호출하여
		// member 테이블에서 id 가 일치하는 레코드의 패스워드(passwd) 조회 요청
		// => 파라미터 : MemberVO 객체   리턴타입 : String(passwd)
//		String passwd = service.getPasswd(member);
////		System.out.println(passwd);
//		
//		// 로그인 성공/실패 여부 판별하여 포워딩
//		// => 성공 : passwd 에 데이터가 저장되어 있고 입력받은 패스워드가 같음, 
//		//    실패 : passwd 가 null 이거나 입력받은 패스워드와 다름
//		if(passwd == null || !passwd.equals(member.getPasswd())) { // 로그인 실패
//			model.addAttribute("msg", "로그인 실패!");
//			return "fail_back";
//		} else { // 로그인 성공(= 패스워드 일치)
//			// 세션 객체에 아이디 저장(속성명 sId)
//			session.setAttribute("sId", member.getId());
//			
//			// --------------------- 아이디 저장 기능 추가(쿠키 활용) -------------------------
////			System.out.println(rememberId); // boolean 타입이므로 true 또는 false 
//			
//			// 쿠키 객체 생성하여 쿠키 저장 후 응답 객체에 쿠키 추가
//			// => 단, 아이디 저장을 체크했을 경우(rememberId 가 true 일 때)에만 작업 수행
//			//    (주의! 코드 실행을 하지 않는 것이 아니라 setMaxAge(0) 값을 전송해야한다!)
//			Cookie cookie = new Cookie("REMEMBER_ID", member.getId());
//			
//			// 아이디 저장 체크 여부 판별하여 쿠키 생성 or 삭제를 위한 MaxAge 값 설정
//			if(rememberId) { // 아이디 저장 체크 시(쿠키 생성)
//				cookie.setMaxAge(60 * 60 * 24 * 30); // 쿠키 유효기간 1개월(30일) 설정
//			} else { // 아이디 저장 체크 해제 시(쿠키 삭제)
//				cookie.setMaxAge(0); // 쿠키 유효기간 0으로 설정 = 쿠키 삭제
//			}
//			
//			response.addCookie(cookie); // 응답 데이터에 쿠키 추가
//			// ---------------------------------------------------------------------
//			return "redirect:/"; // 메인페이지(루트)로 리다이렉트
//		}
		
		// ==========================================================================================
		// --------------- BcryptPasswordEncoder 객체 활용한 로그인(해싱된 암호 비교) ---------------
		// 1. MemberService - getPasswd() 메서드를 호출하여
		// member 테이블에서 id 가 일치하는 레코드의 패스워드(passwd) 조회 요청
		// => 파라미터 : MemberVO 객체   리턴타입 : String(passwd)
		String securePasswd = service.getPasswd(member);
//		System.out.println(passwd);
		
		// 2. BCryptPasswordEncoder 객체 생성
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		// 3. BCryptPasswordEncoder 객체의 matches() 메서드를 호출하여 두 암호 비교
		// => 입력받은 평문은 그대로 두고, DB 에 저장된 패스워드를 가져와서 비교
		//    (주의! 평문을 암호화하여 DB 의 암호문과 직접 비교 불가능! => 솔팅때문에 서로 다름)
		// => 파라미터 : 평문, 암호화 된 암호    리턴타입 : boolean
		// => 로그인 성공/실패 여부 판별하여 포워딩
		// => 성공 : passwd 에 데이터가 저장되어 있고 입력받은 패스워드가 같음(평문 = 암호문), 
		//    실패 : passwd 가 null 이거나 입력받은 패스워드와 다름
		if(member.getPasswd() == null || !passwordEncoder.matches(member.getPasswd(), securePasswd)) { // 로그인 실패
			model.addAttribute("msg", "로그인 실패!");
			return "fail_back";
		} else { // 로그인 성공(= 패스워드 일치)
			// ------------------------- 메일 인증 확인 기능 추가 ---------------------------
			// 이메일 인증 여부 확인하여 미인증 시 인증 요청 알림 표시
			// 아니면, 로그인 후 처리 작업 수행
			// MemberService - isMailAuth()
			// => 파라미터 : MemberVO 객체(아이디 필요)
			if(!service.isMailAuth(member)) { // 메일 미인증 회원
				// "fail_back.jsp" 페이지 포워딩("이메일 인증 후 로그인이 가능합니다." 출력)
				model.addAttribute("msg", "이메일 인증 후 로그인이 가능합니다.");
				return "fail_back";
			// -------------------------------------------------------------------------------
			} else { // 메일 인증 회원
				// 세션 객체에 아이디 저장(속성명 sId)
				session.setAttribute("sId", member.getId());
				
				// --------------------- 아이디 저장 기능 추가(쿠키 활용) -------------------------
//				System.out.println(rememberId); // boolean 타입이므로 true 또는 false 
				
				// 쿠키 객체 생성하여 쿠키 저장 후 응답 객체에 쿠키 추가
				// => 단, 아이디 저장을 체크했을 경우(rememberId 가 true 일 때)에만 작업 수행
				//    (주의! 코드 실행을 하지 않는 것이 아니라 setMaxAge(0) 값을 전송해야한다!)
				Cookie cookie = new Cookie("REMEMBER_ID", member.getId());
				
				// 아이디 저장 체크 여부 판별하여 쿠키 생성 or 삭제를 위한 MaxAge 값 설정
				if(rememberId) { // 아이디 저장 체크 시(쿠키 생성)
					cookie.setMaxAge(60 * 60 * 24 * 30); // 쿠키 유효기간 1개월(30일) 설정
				} else { // 아이디 저장 체크 해제 시(쿠키 삭제)
					cookie.setMaxAge(0); // 쿠키 유효기간 0으로 설정 = 쿠키 삭제
				}
				
				response.addCookie(cookie); // 응답 데이터에 쿠키 추가
				// ---------------------------------------------------------------------
				// 핀테크 계좌 인증을 완료한 회원일 경우
				// 엑세스토큰과 사용자번호를 조회하여 세션에 저장
				// => MemberService - isBankAuth() 메서드를 호출하여 계좌인증 여부 확인
				// => 파라미터 : 아이디   리턴타입 : boolean(isBankAuth)
//				boolean isBankAuth = service.isBankAuth(member.getId());
//				System.out.println("isBankAuth : " + isBankAuth);
				
				// 계좌인증 회원일 경우(true) BankService - getToken() 메서드를 호출하여 토큰 정보 조회
				// => 파라미터 : 아이디   리턴타입 : ResponseTokenVO(token)
//				if(isBankAuth) {
//					ResponseTokenVO token = bankService.getToken(member.getId());
////					System.out.println("token : " + token);
//					
//					// 토큰 정보가 존재할 경우 세션에 엑세스토큰과 사용자번호 저장
//					if(token != null) {
//						session.setAttribute("access_token", token.getAccess_token());		
//						session.setAttribute("user_seq_no", token.getUser_seq_no());	
//					}
//				}
				// ----------- JOIN 활용 시 ----------
				// 검색하고자 하는 아이디의 member 테이블의 bank_auth_status 값이 'Y' 일 때
				// fintech_token 테이블의 레코드 조회
				// => 파라미터 : 아이디   리턴타입 : ResponseTokenVO(token)
				ResponseTokenVO token = bankService.getTokenForBankAuth(member.getId());
				
				// 토큰 정보가 존재할 경우 세션에 엑세스토큰과 사용자번호 저장
				if(token != null) {
					session.setAttribute("access_token", token.getAccess_token());		
					session.setAttribute("user_seq_no", token.getUser_seq_no());	
				}
				
				// ---------------------------------------------------------------------
				return "redirect:/"; // 메인페이지(루트)로 리다이렉트
			}
		}
		
	}
	
	// "MemberLogout" 서블릿 요청에 대해 로그아웃 작업 수행
	// => 세션 객체 접근을 위해 파라미터 타입 HttpSession 선언
	@GetMapping("MemberLogout")
	public String logout(HttpSession session) {
//		session.removeAttribute("sId"); // 세션 아이디만 제거
		
		// 세션 객체 초기화 후 메인페이지로 리다이렉트
		session.invalidate();
		return "redirect:/";
	}
	
	// "MemberInfo" 서블릿 요청에 대해 회원정보 조회 작업 수행
	// => 일반 회원일 경우 세션 아이디로 조회하고
	// => 관리자일 경우 자신의 회원 정보를 조회하거나 다른 회원의 정보를 조회하므로
	//    다른 회원 정보 조회 시 전달받는 id 파라미터값도 처리해야함
	// => 만약, URL 에 id 파라미터 없이 요청했을 경우 별도의 처리를 위해 
	//    @RequestParam 어노테이션에 required = false 속성값을 설정
	@GetMapping("MemberInfo")
	public String memberInfo(@RequestParam(required = false) String id, HttpSession session, Model model) {
		// 단, 세션 아이디가 없을 경우 "잘못된 접근입니다!" 출력 후 이전페이지로 돌아가기
		String sId = (String)session.getAttribute("sId");
		if(sId == null) {
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "fail_back";
		}
		
		// 만약, 현재 세션이 관리자가 아니거나 또는 관리자이면서 아이디 파라미터가 없을 경우
		// id 변수값을 세션 아이디로 교체
		if(!sId.equals("admin") || (sId.equals("admin") && id == null || id.equals(""))) {
			id = sId;
		}
		
		// MemberService - getMemberInfo()
		// 세션 아이디를 사용하여 회원 상세정보 조회 요청
		// => 파라미터 : 아이디    리턴타입 : MemberVO(member)
		MemberVO member = service.getMemberInfo(id);
		
		// ------------ 회원 정보 출력 시 이메일 주소 분리 출력을 위한 분리 작업 ------------
		// email(aaa@bbb.ccc) -> email1(aaa), email2(bbb.ccc)
		member.setEmail1(member.getEmail().split("@")[0]);
		member.setEmail2(member.getEmail().split("@")[1]);
		// ----------------------------------------------------------------------------------
		
		// 회원 상세정보(MemberVO) 저장
		model.addAttribute("member", member);
		
		return "member/member_info";
	}
	
	
	// "MemberModify" 서블릿 요청에 대해 회원 정보 수정 작업 요청 - POST
	@PostMapping("MemberModify")
	public String memberModify(MemberVO member, @RequestParam String newPasswd, HttpSession session, Model model) {
		String sId = (String)session.getAttribute("sId");
		
		// --------------- BcryptPasswordEncoder 객체 활용한 패스워드 비교 ---------------
		// 1. MemberService - getPasswd() 메서드를 호출하여
		// member 테이블에서 id 가 일치하는 레코드의 패스워드(passwd) 조회 요청
		// => 파라미터 : MemberVO 객체   리턴타입 : String(passwd)
		String securePasswd = service.getPasswd(member);
//			System.out.println(passwd);
		
		// 2. BCryptPasswordEncoder 객체 생성
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		// 3. BCryptPasswordEncoder 객체의 matches() 메서드를 호출하여 두 암호 비교
		// => if문 내에서 수행
		
		// 세션 아이디가 없을 경우 잘못된 접근 처리
		// 아니면, 세션 아이디가 "admin" 이 아니고, 패스워드가 입력되지 않았을 때 "패스워드 입력 필수!" 처리
		if(sId == null) {
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "fail_back";
		} else if(!sId.equals("admin")) { // 일반 회원일 때
			if(member.getPasswd().equals("")) { // 패스워드가 입력되지 않았을 경우
				model.addAttribute("msg", "패스워드 입력 필수!");
				return "fail_back";
			} else if(!passwordEncoder.matches(member.getPasswd(), securePasswd)) { // 패스워드가 일치하지 않을 경우
				model.addAttribute("msg", "패스워드 불일치!");
				return "fail_back";
			}
		}
		
		// 일반 회원이 패스워드가 일치하거나, 관리자일 때
		// MemberService - modifyMember() 메서드 호출하여 회원 정보 수정 요청
		// => 단, 관리자일 때 
		// => 파라미터 : MemberVO 객체, 새 패스워드(newPasswd)
		// => 추가) BCryptPasswordEncoder 를 활용하여 새 패스워드 암호화
//		service.modifyMember(member, newPasswd);
		service.modifyMember(member, passwordEncoder.encode(newPasswd));
		
		// "회원 정보 수정 성공!" 메세지 출력 및 "MemberInfo" 서블릿 리다이렉트를 위해 데이터 저장 후
		// success_forward.jsp 페이지로 포워딩
		model.addAttribute("msg", "회원 정보 수정 성공!");
		model.addAttribute("targetURL", "MemberInfo");
		
		return "success_forward";
	}
	
	@GetMapping("MemberCheckoutForm")
	public String checkoutForm() {
		return "member/member_checkout_form";
	}
	
	@PostMapping("MemberCheckoutPro")
	public String checkoutPro(MemberVO member, HttpSession session, Model model) {
		String sId = (String)session.getAttribute("sId");
		
		// MemberVO 객체에 세션 아이디 저장
		member.setId(sId);
		
		// --------------- BcryptPasswordEncoder 객체 활용한 패스워드 비교 ---------------
		// 1. MemberService - getPasswd() 메서드를 호출하여
		// member 테이블에서 id 가 일치하는 레코드의 패스워드(passwd) 조회 요청
		// => 파라미터 : MemberVO 객체   리턴타입 : String(passwd)
		String securePasswd = service.getPasswd(member);
//			System.out.println(passwd);
		
		// 2. BCryptPasswordEncoder 객체 생성
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		// 3. BCryptPasswordEncoder 객체의 matches() 메서드를 호출하여 두 암호 비교
		// => if문 내에서 수행
		
		// 세션 아이디가 없을 경우 잘못된 접근 처리
		// 아니면, 세션 아이디가 "admin" 이 아니고, 패스워드가 입력되지 않았을 때 "패스워드 입력 필수!" 처리
		if(sId == null) {
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "fail_back";
		} else if(!sId.equals("admin")) { // 일반 회원일 때
			if(member.getPasswd().equals("")) { // 패스워드가 입력되지 않았을 경우
				model.addAttribute("msg", "패스워드 입력 필수!");
				return "fail_back";
			} else if(!passwordEncoder.matches(member.getPasswd(), securePasswd)) { // 패스워드가 일치하지 않을 경우
				model.addAttribute("msg", "패스워드 불일치!");
				return "fail_back";
			}
		}
		
		// MemberService - removeMember() 메서드를 호출하여 회원 삭제 작업 요청
		// => 파라미터 : MemberVO 객체   리턴타입 : int(deleteCount)
		int deleteCount = service.removeMember(member);
		
		// 삭제 실패 시 "회원 탈퇴 실패!" 출력 후 이전페이지로 돌아가기 처리
		// 삭제 성공 시 세션 초기화 후 메인페이지로 리다이렉트 
		if(deleteCount == 0) {
			model.addAttribute("msg", "회원 탈퇴 실패!");
			return "fail_back";
		} else {
			// 세션 초기화
			session.invalidate();
			
			// 메인페이지로 리다이렉트 
			return "redirect:/";
		}
		
	}
	
	// 인증 메일 발송을 위한 이메일 주소 입력 폼
	@GetMapping("RequestAuthMailForm")
	public String requestAuthMailForm() {
		return "member/request_auth_mail_form";
	}
	
	// 인증 메일 발송 요청
	// => AJAX 요청에 대한 응답을 위하 @ResponseBody 어노테이션 적용
	@ResponseBody
	@GetMapping("RequestAuthMailPro")
	public String requestAuthMail(String email) {
//		System.out.println(email);
		
		// Service - getId() 메서드를 호출하여
		// member 테이블에서 email 에 해당하는 id 값 조회
		// => 파라미터 : 이메일(email)    리턴타입 : String(id)
		String id = service.getId(email);
//		System.out.println(id);
		
		// SendAuthMail 인스턴스 생성 후 sendMail() 메서드 호출하여 메일 발송 요청
		// => 파라미터 : 아이디, 이메일   리턴타입 : boolean(isSendSuccess)
		SendMailService mailService = new SendMailService();
		String authCode = mailService.sendAuthMail(id, email);
		System.out.println("메일 발송 결과 인증코드 : " + authCode);
		
		// MemberService - registAuthInfo() 메서드를 호출하여 
		// 인증 메일에 포함된 아이디와 인증코드를 인증정보 테이블에 추가
		// => 파라미터 : 아아디, 인증코드   리턴타입 : void
		// => 단, 메일 발송 후 리턴받은 인증코드가 있을 경우에만 작업 수행
		if(!authCode.equals("")) {
			// 인증 코드 DB 작업 요청
			service.registAuthInfo(id, authCode);
			
			// AJAX 요청에 대한 응답으로 "true" 값 리턴
			return "true";
		}
		
		return "false";
	}
	
	// 인증메일에 포함된 링크 클릭 시 인증 요청
	// => 아이디와 이메일 파라미터를 저장하기 위한 AuthInfoVO 타입 파라미터 선언
	@GetMapping("MemberEmailAuth")
	public String emailAuth(AuthInfoVO authInfo, Model model) {
		// 인증 요청
		// Service - emailAuth()
		// => 파라미터 : 인증정보(AuthInfoVO 객체)   리턴타입 : boolean(isAuthSuccess)
		boolean isAuthSuccess = service.emailAuth(authInfo);
		
		// 인증 수행 결과 판별
		// 성공 시 인증 완료 메세지와 로그인 폼 URL 을 Model 객체에 저장 후 success_forward.jsp 페이지로 포워딩
		// 실패 시 인증 실패 메세지를 포함하여 fail_back.jsp 페이지로 포워딩
		if(isAuthSuccess) {
			model.addAttribute("msg", "인증 완료! 로그인이 가능합니다!");
			model.addAttribute("targetURL", "MemberLoginForm");
			return "success_forward";
		} else {
			model.addAttribute("msg", "인증 실패!");
			return "fail_back";
		}
		
	}
	
}



























