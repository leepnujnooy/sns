package com.mutsa.sns.exception;

import com.mutsa.sns.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDto> response(CustomException exception){
        return ResponseEntity
                .status(exception.getErrorCode().getHttpStatus())
                .body(new ResponseDto(exception.getMessage()));
    }
}
