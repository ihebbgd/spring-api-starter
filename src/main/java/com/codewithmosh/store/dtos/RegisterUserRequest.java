package com.codewithmosh.store.dtos;

import lombok.Data;

@Data //GETTER SETTER TOSTRING ...
public class RegisterUserRequest {
    private String name;
    private String email;
    private String password;
}
