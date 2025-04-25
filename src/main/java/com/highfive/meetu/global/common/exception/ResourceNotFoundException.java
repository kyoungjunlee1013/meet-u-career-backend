package com.highfive.meetu.global.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 리소스를 찾을 수 없을 때 발생하는 예외
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * @param resourceName 리소스 이름
     * @param fieldValue   찾으려는 값
     */
    public ResourceNotFoundException(String resourceName, Object fieldValue) {
        super(String.format("%s (ID:%s)을 찾을 수 없습니다.", resourceName, fieldValue));
    }
}
