package com.itxiaoer.commons.security;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itxiaoer.commons.jwt.JwtAuth;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : liuyk
 */
@SuppressWarnings("all")
public abstract class JwtUserDetail extends JwtAuth implements UserDetails {
    public JwtUserDetail() {
    }

    public JwtUserDetail(String id, String loginName, String nickName, List<String> roles) {
        super(id, loginName, nickName, roles);
    }

    public JwtUserDetail(String id, String loginName, String nickName, String avatar, List<String> roles) {
        super(id, loginName, nickName, avatar, roles);
    }

    /**
     * 获取密码修改时间
     *
     * @return 密码修改时间
     */
    public abstract LocalDateTime getModifyPasswordTime();

    @Override
    @JsonIgnore
    public String getPassword() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.getLoginName();
    }


    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
