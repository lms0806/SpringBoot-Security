package com.cos.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.security1.model.User;

import lombok.Data;

// 시큐리티가 /login 주소 요청을 낚아채서 로그인을 진행시킴
// 로그인을 진행이 완료가 되면 시큐리티 session을 만들어줌 (Security ContextHolder)
// 오브젝트 타입 => Authentication 타입 객체
// Authentication 안에 User정보가 있어야 됨.
// User오브젝트의 타입 => UserDetails 타입 객체

// Security Session => Authentication => UserDetails(PrincipalDetails)

@Data
public class PrincipalDetails implements UserDetails, OAuth2User{

	private User user;
	private Map<String, Object> attribues;
	
	// 일반 로그인
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	// OAuth 로그인
	public PrincipalDetails(User user, Map<String, Object> attribues) {
		this.user = user;
		this.attribues = attribues;
	}
	
	// 해당 user의 권한을 리턴하는 곳
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		// 사이트에서 1년동안 회원이 로그인을 안하면 휴면 계정으로 하기로함
		// 현재시간 - 로그인 시간 => 1년을 초과하면 return false;
		
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attribues;
	}

	@Override
	public String getName() {
		return null;
	}
}