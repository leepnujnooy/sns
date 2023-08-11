package com.mutsa.sns.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class LoginRequestDto {
    private String username;
    private String password;
}
