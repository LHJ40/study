package com.itwillbs.fintech.mapper;

import org.apache.ibatis.annotations.Param;

import com.itwillbs.fintech.vo.ResponseTokenVO;

public interface BankMapper {

	// 토큰 정보 저장
	int insertToken(@Param("id") String id, @Param("token") ResponseTokenVO responseToken);

	// 토큰 정보 조회
	ResponseTokenVO selectToken(String id);

	// 토큰 정보 조회 요청 - JOIN 구문 활용
	ResponseTokenVO selectTokenForBankAuth(String id);
	
}
