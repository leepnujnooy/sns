package com.mutsa.sns.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto {
    private String message;

    public ResponseDto(){

    }
    public ResponseDto(String s) {
        this.message=s;
    }
}
