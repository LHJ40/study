package com.itwillbs.fintech.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.fintech.vo.ResponseTokenVO;

@Service
public class BankApiService {
	// 실제 금융API 요청을 수행할 BankApiClient 타입 선언
	@Autowired
	private BankApiClient bankApiClient;

	// 엑세스토큰 발급 요청
	public ResponseTokenVO requestToken(Map<String, String> authResponse) {
		// BankApiClient - requestToken() 메서드 호출 후 결과값 리턴
		// => 파라미터 : Map 타입(authResponse)   리턴타입 : ResponseTokenVO
		return bankApiClient.requestToken(authResponse);
	}

}










