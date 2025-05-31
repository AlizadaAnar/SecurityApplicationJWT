package com.javah.config;

import com.javah.entity.UserInfo;
import com.javah.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> byName = userInfoRepository.findByName(username);
        return byName.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found with name: " + username));
    }
}
