package com.itwillbs.test3_mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.test3_mybatis.vo.StudentVO;

// DAO 대신 활용할 Mapper 인터페이스 정의
// => 마이바티스를 통해 Mapper XML 파일과 연결되어 자동으로 XML 파일의 SQL 구문을 실행
// => 주의! 인터페이스 내의 추상메서드 이름은 XML 파일의 태그 ID 속성값과 동일하게 지정해야함
// => @Mapper 어노테이션을 적용하여 Mapper 역할의 빈으로 등록
@Mapper
public interface StudentMapper {
	/*
	 * Service 클래스로부터 호출받아 SQL 구문 실행을 위해 XML 파일과 연결될 추상메서드 정의
	 * 주의! 추상메서드명과 XML 파일의 태그 내의 id 속성값이 일치해야한다!
	 * => 이 때, root-context.xml 파일 내의 지정된 다음 코드에 의해
	 *    현재 인터페이스와 XML 파일 내의 namespace 속성에 지정된 객체가 자동으로 연결됨
	 *    <property name="mapperLocations" value="classpath:/com/itwillbs/test3_mybatis/mapper/*Mapper.xml"></property>
	 *    <mybatis-spring:scan base-package="com.itwillbs.test3_mybatis"/>    
	 */

	// 학생 정보 등록 => 추상메서드
	int insertStudent(StudentVO student); // public abstract 생략되어 있음
	// => 자동으로 StudentMapper.xml 파일의 <insert id="insertStudent"> 태그와 연결되어 자동으로 SQL 실행됨
	// => 이 때, XML 에서 단일 파라미터일 경우 전달된 파라미터에 그대로 직접 접근 가능
	
	// 학생 정보 조회
	StudentVO selectStudent(String email);
	
	// 학생 목록 조회
	List<StudentVO> selectStudentList();


}











