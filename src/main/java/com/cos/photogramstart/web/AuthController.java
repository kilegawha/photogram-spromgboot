package com.cos.photogramstart.web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor	 //final이 걸려있는 모든 매개변수의 생성자가 만들어진다.
@Controller // 1. Ioc 2. 파일을 리턴하는 컨트롤러 
public class AuthController {
	
	private final AuthService authService;
	/*
	 * public AuthController(AuthService authService) { this.authService =
	 * authService; }
	 */
	@GetMapping("/auth/signin")
	public String sigininForm() {
		return "auth/signin";
	}
	
	@GetMapping("/auth/signup")
	public String siginupForm() {
		return "auth/signup";
	}
	//회원가입 버튼 -> /auth/signin -> /auth/signin
	@PostMapping("/auth/signup")
	public String siginup(@Valid SignupDto signupDto, BindingResult bindingResult) {
			User userEntity = authService.join(signupDto);
			return "auth/signin";		
	}
}
