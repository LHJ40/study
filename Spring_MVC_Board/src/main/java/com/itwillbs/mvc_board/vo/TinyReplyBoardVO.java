package com.itwillbs.mvc_board.vo;

import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/*
spring_mvc_board5.tiny_reply_board 테이블 생성
----------------------------
글번호(reply_num) - 정수, PK, 자동증가
원본글번호(board_num) - 정수, FK(board - board_num, 삭제 옵션 추가), NN
작성자(reply_name) - 문자열(16), NN
내용(reply_content) - 문자열(100), NN
참조글번호(reply_re_ref) - 정수, NN
들여쓰기레벨(reply_re_lev) - 정수, NN
순서번호(reply_re_seq) - 정수, NN
작성일(reply_date) - 날짜 및 시각, NN
---------------------------------------
CREATE TABLE tiny_reply_board (
	reply_num INT PRIMARY KEY AUTO_INCREMENT,
	board_num INT NOT NULL,
	reply_name VARCHAR(16) NOT NULL,
	reply_content VARCHAR(100) NOT NULL,
	reply_re_ref INT NOT NULL,
	reply_re_lev INT NOT NULL,
	reply_re_seq INT NOT NULL,
	reply_date DATETIME NOT NULL,
	FOREIGN KEY (board_num) REFERENCES board(board_num) ON DELETE CASCADE
);
*/

// 댓글 1개 정보를 저장하는 TinyReplyBoardVO 클래스 정의
@Data
public class TinyReplyBoardVO {
	private int reply_num;
	private int board_num;
	private String reply_name;
	private String reply_content;
	private int reply_re_ref;
	private int reply_re_lev;
	private int reply_re_seq;
	private Timestamp reply_date;
}


















