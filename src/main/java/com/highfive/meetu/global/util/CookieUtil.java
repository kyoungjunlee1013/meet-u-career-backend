package com.highfive.meetu.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieUtil {

    /**
     * 요청에서 특정 이름의 쿠키 값을 추출
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null)
            return null;

        for (Cookie cookie : request.getCookies()) {
            if (cookieName.trim().equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    /**
     * RefreshToken 전용 추출 메서드
     */
    public static String extractRefreshToken(HttpServletRequest request) {
        return getCookieValue(request, "refreshToken");
    }
}
