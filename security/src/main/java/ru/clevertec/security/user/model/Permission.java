package ru.clevertec.security.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    READ("READ"),
    UPDATE("UPDATE"),
    DELETE("DELETE"),
    POST("POST");

    private final String permission;
}
