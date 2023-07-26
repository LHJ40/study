package com.itwillbs.fintech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.fintech.mapper.BoardMapper;
import com.itwillbs.fintech.vo.BoardVO;
import com.itwillbs.fintech.vo.TinyReplyBoardVO;

@Service
public class BoardService {
	@Autowired
	private BoardMapper mapper;

	// 글 쓰기 작업 요청
	public int registBoard(BoardVO board) {
		return mapper.insertBoard(board);
	}

	// 글 목록 조회 요청
	public List<BoardVO> getBoardList(String searchType, String searchKeyword, int startRow, int listLimit) {
		return mapper.selectBoardList(searchType, searchKeyword, startRow, listLimit);
	}

	// 전체 글 목록 갯수 조회 요청
	public int getBoardListCount(String searchType, String searchKeyword) {
		return mapper.selectBoardListCount(searchType, searchKeyword);
	}

	// 글 상세정보 조회 요청
	// => 조회 성공 시 조회수 증가
	public BoardVO getBoard(int board_num) {
		// 1. Mapper - selectBoard() 메서드 호출하여 상세정보 조회 후 결과를 리턴받아 BoardVO 객체에 저장
		// => 파라미터 : 글번호   리턴타입 : BoardVO
		BoardVO board = mapper.selectBoard(board_num);
		
		// 2. 조회 결과가 있을 경우 Mapper - updateReadcount() 메서드 호출하여 조회수 증가 요청
		// => 파라미터 : 글번호   리턴타입 : void
		if(board != null) {
//			mapper.updateReadcount(board_num);
			
			// 3. 증가된 조회수를 반영하기 위해 BoardVO 객체의 board_readcount 변수값을 1 증가
//			board.setBoard_readcount(board.getBoard_readcount() + 1);
			// --------------------------------------------------------
			// 글번호 대신 BoardVO 객체를 파라미터로 전달(board_num 포함되어 있음)
			// => 마이바티스를 통해 BoardVO 객체의 값이 변경되면 별도로 리턴받지 않아도
			//    주소값이 전달되어 사용된 BoardVO 객체의 변경된 값을 함께 공유함
			mapper.updateReadcount(board);
		}
		
		return board;
	}

	// 작성자 확인
	public boolean isBoardWriter(int board_num, String id) {
		// Mapper - selectBoard() 메서드 재사용하여 글번호에 해당하는 레코드 조회 후
		// 조회된 결과(BoardVO)의 board_name 과 전달받은 세션 아이디 비교 결과 리턴
		BoardVO board = mapper.selectBoard(board_num);
		return id.equals(board.getBoard_name());
	}

	// 글 삭제
	public int removeBoard(int board_num) {
		return mapper.deleteBoard(board_num);
	}

	// 답글 등록
	public int registReplyBoard(BoardVO board) {
		// 기존 답글들의 순서 번호 조정을 위해 updateBoardReSeq() 메서드 호출
		// => 파라미터 : BoardVO 객체   리턴타입 : void
		mapper.updateBoardReSeq(board);
		
		// 답글 등록 작업을 위해 insertReplyBoard() 메서드 호출
		// => 파라미터 : BoardVO 객체   리턴타입 : int
		return mapper.insertReplyBoard(board);
	}

	// 글 수정
	public int modifyBoard(BoardVO board) {
		return mapper.updateBoard(board);
	}

	// 댓글 작성
	public int registTinyReplyBoard(TinyReplyBoardVO board) {
		// 댓글 작성 시 사용된 댓글 번호(자동 증가값)를 자동으로 VO 객체에 저장하므로
		// 해당 값을 사용하여 댓글 참조번호를 UPDATE 하기
		mapper.insertTinyReplyBoard(board); // 댓글 작성
		return mapper.updateRe_ref(board);  // 댓글 참조글 번호 갱신
	}

	// 댓글 목록 조회
	public List<TinyReplyBoardVO> getTinyReplyBoardList(int board_num) {
		return mapper.selectTinyReplyBoardList(board_num);
	}

	// 댓글 작성자 검색
	public String getTinyReplyWriter(TinyReplyBoardVO board) {
		return mapper.selectTinyReplyWriter(board);
	}
	
	// 댓글 삭제
	public int removeTinyReplyBoard(TinyReplyBoardVO board) {
		return mapper.deleteTinyReplyBoard(board);
	}

	// 대댓글 작성
	public int writeTinyReReplyBoard(TinyReplyBoardVO board) {
		return mapper.insertTinyReReplyBoard(board);
	}

	
	
}














