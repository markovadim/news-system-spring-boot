package ru.clevertec.security.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.security.user.dto.UserDto;
import ru.clevertec.security.user.model.User;
import ru.clevertec.security.user.mapping.UserMapper;
import ru.clevertec.security.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> findAllUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    public UserDto findUserById(long id) {
        return userMapper.toDto(userRepository.findById(id).orElseThrow());
    }

    public void saveUser(UserDto user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userMapper.toEntity(user));
    }

    public void deleteUserById(long id) {
        findUserById(id);
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateUserById(long id, UserDto user) {
        User currentUser = userMapper.toEntity(findUserById(id));
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userMapper.update(user, currentUser);
        userRepository.save(currentUser);
    }
}
