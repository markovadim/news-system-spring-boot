package ru.clevertec.security.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.security.user.dto.UserDto;
import ru.clevertec.security.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.findUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto dto) {
        userService.saveUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id) {
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("User was deleted with id:%d", id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateById(@PathVariable long id,
                                              @RequestBody UserDto dto) {
        userService.updateUserById(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
