package com.cos.photogramstart.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.CMRespDto;

@RestController // 낚아채서 응답해주기위해서
@ControllerAdvice // 모든 exception을 모두 다 낚아챈다.
public class ControllerExceptionHandler {
	// 자바스크립트 응답
	@ExceptionHandler(CustomValidationException.class)
	public String validationException(CustomValidationException e) {
		System.out.println("============나 실행 되고 있나~~~~==============");
		if(e.getErrorMap() == null) {
			return Script.back(e.getMessage());
		}else {
			return Script.back(e.getErrorMap().toString());
		}
	}

	//데이터 응답    ajax이용하면 데이터를 응답해줘야한다.
	@ExceptionHandler(CustomValidationApiException.class)
	public ResponseEntity<?> validationApiException(CustomValidationApiException e) {
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CustomApiException.class)
	public ResponseEntity<?> apiException(CustomApiException e) {
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(CustomException.class)
	public String exception(CustomException e) {
		return Script.back(e.getMessage());
	}

}
