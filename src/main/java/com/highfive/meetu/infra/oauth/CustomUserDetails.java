package com.highfive.meetu.infra.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private Long accountId;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long accountId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.accountId = accountId; // accountId가 제대로 설정되었는지 확인
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public Long getAccountId() {
        return this.accountId; // 반환 값이 올바르게 나오는지 확인
    }

    // 기본적으로 필요한 UserDetails 메서드들 구현
    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
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
        return true;
    }
}
