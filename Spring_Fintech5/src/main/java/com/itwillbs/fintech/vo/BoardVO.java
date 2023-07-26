package com.itwillbs.fintech.vo;

import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/*
spring_mvc_board5.board 테이블 생성
----------------------------
글번호(board_num) - 정수, PK
작성자(board_name) - 문자열(16), NN
패스워드(board_pass) - 문자열(16), NN  => 삭제
제목(board_subject) - 문자열(50), NN
내용(board_content) - 문자열(2000), NN
파일명1(board_file1) - 문자열(200), NN
파일명2(board_file2) - 문자열(200), NN
파일명3(board_file3) - 문자열(200), NN
참조글번호(board_re_ref) - 정수, NN
들여쓰기레벨(board_re_lev) - 정수, NN
순서번호(board_re_seq) - 정수, NN
조회수(board_readcount) - 정수, NN
작성일(board_date) - 날짜 및 시각, NN
---------------------------------------
CREATE TABLE board (
	board_num INT PRIMARY KEY,
	board_name VARCHAR(16) NOT NULL,
	board_subject VARCHAR(50) NOT NULL,
	board_content VARCHAR(2000) NOT NULL,
	board_file1 VARCHAR(200) NOT NULL,
	board_file2 VARCHAR(200) NOT NULL,
	board_file3 VARCHAR(200) NOT NULL,
	board_re_ref INT NOT NULL,
	board_re_lev INT NOT NULL,
	board_re_seq INT NOT NULL,
	board_readcount INT NOT NULL,
	board_date DATETIME NOT NULL
);
*/

// 게시판 1개 게시물 정보를 저장하는 BoardVO 클래스 정의
@Data
public class BoardVO {
	private int board_num;
	private String board_name;
	private String board_subject;
	private String board_content;
	private int board_re_ref;
	private int board_re_lev;
	private int board_re_seq;
	private int board_readcount;
	private Timestamp board_date;
	// 파일명을 저장할 변수 선언
	private String board_file1;
	private String board_file2;
	private String board_file3;
	// 주의! 폼에서 전달받는 실제 파일 자체를 다룰 MultipartFile 타입 변수 선언도 필요
	// => 이 때, 멤버변수명은 input type="file" 태그의 name 속성명(파라미터명)과 동일해야함
	private MultipartFile file1;
	private MultipartFile file2;
	private MultipartFile file3;
}


















