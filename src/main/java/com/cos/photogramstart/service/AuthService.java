package com.cos.photogramstart.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service	//Ioc    2. 트랜잭션 관리
public class AuthService {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;	
	private final UserRepository userRepository;
	
	@Transactional	//Write(Insert, Update, Delete)
	public User join(SignupDto user) {
		//회원가입진행
		if(userRepository.findByUsername(user.getUsername()) == null) {
			User userEntity = user.toEntity();			
			String rawPassword = user.getPassword();
			String encPassword = bCryptPasswordEncoder.encode(rawPassword);
			userEntity.setPassword(encPassword);
			userEntity.setRole("ROLE_USER");			//관리자 ROLE_ADMIN		
			return userRepository.save(userEntity);
		}else {
			throw new CustomException("이미 등록된 유저네임입니다.");
		}
	}
}
