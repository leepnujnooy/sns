package com.mutsa.sns.exception;

import com.mutsa.sns.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_USER_NAME(HttpStatus.CONFLICT,"중복된 아이디 입니다."),
    OMISSION_OF_ESSENTIAL(HttpStatus.BAD_REQUEST,"아이디와 비밀번호 입력은 필수입니다."),
    USER_NAME_NOT_FOUND(HttpStatus.NOT_FOUND,"일치하는 아이디가 없습니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST,"비밀번호가 틀립니다."),
    NO_FILE(HttpStatus.NOT_FOUND,"업로드할 파일이 없습니다."),
    FAILED_TO_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR,"파일을 업로드하지 못했습니다."),
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND,"피드를 찾을 수 없습니다.");


    private HttpStatus httpStatus;
    private String message;
}
