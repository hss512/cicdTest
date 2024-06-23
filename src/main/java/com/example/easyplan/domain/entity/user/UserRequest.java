package com.example.easyplan.domain.entity.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String email;
    private String password;
    private String name;
}
