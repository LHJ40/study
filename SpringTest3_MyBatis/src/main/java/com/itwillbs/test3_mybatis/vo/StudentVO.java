package com.itwillbs.test3_mybatis.vo;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/*
study_jsp5.student 테이블 정의
----------------------------------
번호(idx) - 정수, PK, 자동증가
이름(name) - 문자 20자, NN
이메일(email) - 문자 100자, NN, UN
학년(grade) - 정수, NN
----------------------------------
CREATE TABLE student (
	idx INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(20) NOT NULL,
	email VARCHAR(100) NOT NULL UNIQUE,
	grade INT NOT NULL
);
*/

// Lombok 라이브러리를 통해 자원관리 메서드 자동 생성을 위해서
// VO 클래스 선언 시 어노테이션을 통해 자동 생성할 대상 지정
// 1) @Getter : Getter 메서드 자동 생성
// 2) @Setter : Setter 메서드 자동 생성
// 3) @ToString : ToString 메서드 자동 생성
// 4) @Data : 모든 기본 사항 자동 생성
// 5) 기타...
@Data
public class StudentVO {
	private int idx;
	private String name;
	private String email;
	private int grade;

}















