package com.cos.photogramstart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.photogramstart.config.auth.oauth.Oauth2DetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity		//해당 파일로 시큐리티를 활성화
@Configuration				//Ioc
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final Oauth2DetailsService oauth2DetailsService;
	
	@Bean
	public  BCryptPasswordEncoder encoded() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// super 삭제 - 기존 시큐리티가 가지고 있는 기능이 다 비활성화됨.
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/", "/user/**", "/image/**" , "/subscribe/**","/comment/**", "api/**").authenticated()
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/auth/signin")  //GET
			.loginProcessingUrl("/auth/signin")	//POST -> 스프링 시큐리티가 로그인프로세스를 진행
			.defaultSuccessUrl("/")
			.and()
			.oauth2Login()		//form 로그인도 하는데, oauth2로그인도 할거야
			.userInfoEndpoint()	// oauth2로그인을 하면 최종응답을 회원정보를 바로 받을 수 있다.
			.userService(oauth2DetailsService)
			;
	}
}
