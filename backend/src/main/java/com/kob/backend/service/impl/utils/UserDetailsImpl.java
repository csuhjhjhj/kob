package com.kob.backend.service.impl.utils;

import com.kob.backend.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private User user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
    public boolean isAccountNonExpired() {//是否账户未过期
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {//是否未被锁定
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {//凭证是否未被过期
        return true;
    }

    @Override
    public boolean isEnabled() {//是否被启用
        return true;
    }
}
