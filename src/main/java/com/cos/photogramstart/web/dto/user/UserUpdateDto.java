package com.cos.photogramstart.web.dto.user;

import javax.validation.constraints.NotBlank;

import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data
public class UserUpdateDto {
	@NotBlank
	private String name;
	@NotBlank
	private String password;
	private String website;
	private String bio;
	private String phone;
	private String gender;
	
	//조금 위험함. 코드 수정이 필요할 예정
	public User toEntity(){
		return User.builder()
				.name(name)					//이름 기재안했으면 문제가 생긴다. Validaation 체크를 해야한다.
				.password(password)		//패스워드를 기재안하면 DB에 공백이 들어간다. Validation체크를해야한다.
				.website(website)
				.bio(bio)
				.phone(phone)
				.gender(gender)
				.build();
	}
}
