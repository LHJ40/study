package com.itwillbs.test3_mybatis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.test3_mybatis.mapper.StudentMapper;
import com.itwillbs.test3_mybatis.vo.StudentVO;

// 스프링의 서비스 클래스 역할 수행을 위한 클래스 정의 시 @Service 어노테이션을 지정
// => @Service 어노테이션이 붙은 클래스는 컨트롤러 클래스에서
//    @Autowired 어노테이션 등으로 자동 주입 할 수 있다!
@Service
public class StudentService {
	// MyBatis 를 통해 SQL 구문 처리를 담당할 XXXMapper.xml 파일과 연동되는
	// XXXMapper 객체를 자동 주입받기 위해 @Autowired 어노테이션으로 멤버변수 선언
	// 단, @Mapper 어노테이션이 적용된 Mapper 인터페이스 정의 필수! (클래스 아님!)
	@Autowired
	private StudentMapper mapper;

	// 학생 등록 요청 작업을 위한 registStudent() 메서드 정의
	public int registStudent(StudentVO student) {
		/*
		 * DB 작업을 수행할 Mapper 객체의 메서드를 호출하여 SQL 구문 실행 요청
		 * => DAO 클래스 없이 마이바티스 활용을 위한 Mapper 객체의 메서드 호출 후
		 *    리턴되는 결과값을 전달받아 Controller 클래스로 다시 리턴해주는 역할 수행
		 * => 단, 별도의- 추가적인 작업이 없으므로 return 문 뒤에 메서드 호출 코드를 직접 기술하고
		 *    만약, 메서드 호출 전후 추가적인 작업이 필요할 경우 호출 코드와 리턴문을 분리
		 * ---------------------------------------------------------------------------------------
		 * Mapper 역할을 수행하는 XXXMapper 인터페이스는 인스턴스 생성이 불가능하며
		 * 스프링(마이바티스)에서 자동 주입으로 객체를 전달받아 사용
		 * => 업캐스팅을 통해 실제 구현체 객체를 알지 못하더라도 메서드 호출 가능하다!
		 */
//		System.out.println("StudentService - registStudent()");
		// StudentMapper - insertStudent() 메서드를 호출하여 학생 정보 등록 요청 후 리턴값을 전달받아 다시 리턴
		// => 파라미터 : StudentVO 객체   리턴타입 : int
		return mapper.insertStudent(student);
	}
	
	// 학생 정보 조회 요청을 위한 getStudent() 메서드 정의
	public StudentVO getStudent(String email) {
		// StudentMapper - selectStudent()
		return mapper.selectStudent(email);
	}

	// 학생 목록 조회 요청을 위한 getStudentList() 메서드 정의
	public List<StudentVO> getStudentList() {
		// StudentMapper - selectStudentList()
		return mapper.selectStudentList();
	}

}









