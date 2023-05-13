package me.moonjounghyun.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.moonjounghyun.springbootdeveloper.domain.User;
import me.moonjounghyun.springbootdeveloper.dto.AddUserRequest;
import me.moonjounghyun.springbootdeveloper.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                // password 암호화
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }
}
