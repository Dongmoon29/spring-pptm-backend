package io.dm29.ppmtool.services;

import io.dm29.ppmtool.domain.User;
import io.dm29.ppmtool.exceptions.UsernameAlreadyExistException;
import io.dm29.ppmtool.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User saveUser(User newUser) {
        try {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            // Username has to be unique (exception needed)
            newUser.setUsername(newUser.getUsername());
            // Make sure that password and confirmPassword match
            // We don't persist or show the confirmPassword
            newUser.setConfirmPassword("");
            return userRepository.save(newUser);
        } catch(Exception e) {
            throw new UsernameAlreadyExistException("User '"+newUser.getUsername() +"' already exists");
        }
    }

}
