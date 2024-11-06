package com.web.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.web.demo.entity.Member;


@Configuration
@EnableWebSecurity // 스프링 시큐리티
@EnableMethodSecurity
public class SecurityConfig {
//	@Autowired
//	private TokenProvider tokenProvider;
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.authorizeHttpRequests( ( a ) -> a.requestMatchers
				( new AntPathRequestMatcher("/bbs/*/post/**") ).authenticated())
		.authorizeHttpRequests( ( a ) -> a.requestMatchers
			( new AntPathRequestMatcher("/board/cnotrol") ).hasAnyRole("ADMIN", "SUBADMIN") )	
		.authorizeHttpRequests( ( a ) -> a.requestMatchers
				( new AntPathRequestMatcher("/member/cnotrol") ).hasRole("ADMIN") )	
		.authorizeHttpRequests( ( a ) -> a.requestMatchers
				( new AntPathRequestMatcher("/**") ).permitAll() )
			
//			.csrf( (b) -> 
//			b.ignoringRequestMatchers(new AntPathRequestMatcher("/api/**")))
//			.headers( (c)-> c.addHeaderWriter(new XFrameOptionsHeaderWriter(
//					XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)) )
			
			// 로그인 관련
		.formLogin((formLogin) -> formLogin
	                .loginPage("/member/login") //로그인 주소창
	                .defaultSuccessUrl("/") //로그인 완료시 이동
	                .usernameParameter("email") // 이메일을 username으로 쓰도록 설정
	                .passwordParameter("password")
					)	
			// 로그아웃 관련
		.logout((logout) -> logout
					.logoutUrl("/member/logout") // 로그아웃 url 포스트 방식
//	                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) // 로그아웃 url request방식(post,get 둘 다 가능)
	                .logoutSuccessUrl("/member/login") //로그아웃 완료 후 이동
	                .invalidateHttpSession(true));  // 로그아웃 후 세션 초기화
//					.deleteCookies("JSESSIONID")  // 로그아웃 후 쿠키 삭제
		
		return http.build();
	}
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
















					