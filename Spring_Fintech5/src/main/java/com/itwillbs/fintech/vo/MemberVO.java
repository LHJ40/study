package com.itwillbs.fintech.vo;

import java.sql.Date;

import lombok.Data;

/*
member 테이블 정의
----------------------
번호(idx) - 정수, PK, AUTO_INCREMENT
이름(name) - 문자(10자), NN
아이디(id) - 문자(16자), UN, NN
비밀번호(passwd) - 문자(16자), NN
주민번호(jumin) - 문자(14자), UN, NN
이메일(email) - 문자(50자), UN, NN
직업(job) - 문자(10자), NN
성별(gender) - 문자(1자), NN
취미(hobby) - 문자(50자), NN
가입동기(motivation) - 문자(500자), NN
가입일(date) - 날짜(DATE), NN
이메일인증여부(mail_auth_status) - 문자(1자), NN
계좌인증여부(bank_auth_status) - 문자(1자), NN
--------------------------------------
 CREATE TABLE member (
 	idx INT PRIMARY KEY AUTO_INCREMENT,
 	name VARCHAR(10) NOT NULL,
 	id VARCHAR(16) NOT NULL UNIQUE,
 	passwd VARCHAR(16) NOT NULL,
 	email VARCHAR(50) NOT NULL UNIQUE,
 	job VARCHAR(10) NOT NULL,
 	gender VARCHAR(1) NOT NULL,
 	hobby VARCHAR(50) NOT NULL,
 	motivation VARCHAR(500) NOT NULL,
 	hire_date DATE NOT NULL,
 	mail_auth_status CHAR(1) NOT NULL,
 	bank_auth_status CHAR(1) NOT NULL
 );
 => 패스워드 암호화 기능 추가를 위해 passwd 컬럼 VARCHAR(100) 으로 변경
    ALTER TABLE MEMBER CHANGE passwd passwd VARCHAR(100) NOT NULL; 
 => 은행계좌인증 기능 추가를 위해 bank_auth_status 컬럼 추가
*/

@Data
public class MemberVO {
	private int idx;
	private String name;
	private String id;
	private String passwd;
	private String email;
	// --------- 분리된 이메일 주소를 전달받기 위한 변수 추가 ------------
	private String email1;
	private String email2;
	// -------------------------------------------------------------------
	private String job;
	private String gender;
	private String hobby;
	private String motivation;
	private Date hire_date;
	private String mail_auth_status; // 이메일 인증 여부(기본값 "N")
	// -------------------------------------------------------------------
	private String bank_auth_status; // 은행계좌 인증 여부(기본값 "N")
}





























