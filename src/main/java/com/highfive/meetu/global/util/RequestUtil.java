package com.highfive.meetu.global.util;

import com.highfive.meetu.global.common.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;

public class RequestUtil {
    public static Long getUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");

        if (userId instanceof Long) {
            return (Long) userId;
        } else if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        }

        throw new NotFoundException("요청에서 userId를 찾을 수 없습니다.");
    }
}
