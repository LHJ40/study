package com.itwillbs.fintech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.fintech.mapper.BankMapper;
import com.itwillbs.fintech.vo.ResponseTokenVO;

@Service
public class BankService {
	@Autowired
	private BankMapper mapper;

	// 토큰 관련 정보 저장 요청
	public boolean registToken(String id, ResponseTokenVO responseToken) {
		if(mapper.insertToken(id, responseToken) > 0) {
			return true;
		} else {
			return false;
		}
	}

	// 토큰 정보 조회 요청
	public ResponseTokenVO getToken(String id) {
		return mapper.selectToken(id);
	}

	// 토큰 정보 조회 요청 - JOIN 구문 활용
	public ResponseTokenVO getTokenForBankAuth(String id) {
		return mapper.selectTokenForBankAuth(id);
	}

}











