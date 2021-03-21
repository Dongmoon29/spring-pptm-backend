package io.dm29.ppmtool.services;

import io.dm29.ppmtool.domain.User;
import io.dm29.ppmtool.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if(user == null) throw new UsernameNotFoundException("User not found");
        return user;
    }

    @Transactional
    public User loadUserById(Long id) {
        User user = userRepository.getById(id);
        if(user == null) throw new UsernameNotFoundException("User not found");
        return user;
    }

}
