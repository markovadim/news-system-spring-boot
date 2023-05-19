package ru.clevertec.security.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;


@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN,
    JOURNALIST,
    SUBSCRIBER;

//    private final List<SimpleGrantedAuthority> getAuthorities;
}
