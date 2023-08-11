package com.mutsa.sns.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SignupRequestDto {
    private String username;
    private String password;
    private String email;
    private String phone;
}
