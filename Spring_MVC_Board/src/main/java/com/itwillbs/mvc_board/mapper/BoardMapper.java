package com.itwillbs.mvc_board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.mvc_board.vo.BoardVO;

@Mapper
public interface BoardMapper {

	// 글 쓰기
	int insertBoard(BoardVO board);

	// 글 목록 조회
	// => 복수개의 파라미터 구분을 위해 @Param 어노테이션 사용
	List<BoardVO> selectBoardList(
			@Param("searchType") String searchType, 
			@Param("searchKeyword") String searchKeyword, 
			@Param("startRow") int startRow, 
			@Param("listLimit") int listLimit);

	// 전체 글 목록 갯수 조회
	int selectBoardListCount(
			@Param("searchType") String searchType, 
			@Param("searchKeyword") String searchKeyword);

	// 글 상세정보 조회
	BoardVO selectBoard(int board_num);

	// 조회수 증가
//	void updateReadcount(int board_num);
	void updateReadcount(BoardVO board);

	// 글 삭제
	int deleteBoard(int board_num);

	// 답글 등록 전 순서번호 증가
	void updateBoardReSeq(BoardVO board);

	// 답글 등록
	int insertReplyBoard(BoardVO board);

	// 글 수정
	int updateBoard(BoardVO board);

}











