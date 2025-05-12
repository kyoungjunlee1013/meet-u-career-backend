package com.highfive.meetu.global.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    public static String formatTimeAgo(LocalDateTime time) {
        Duration duration = Duration.between(time, LocalDateTime.now());

        long minutes = duration.toMinutes();
        if (minutes < 1) return "방금 전";
        if (minutes < 60) return minutes + "분 전";
        if (minutes < 1440) return (minutes / 60) + "시간 전";
        if (minutes < 10080) return (minutes / 1440) + "일 전";

        return time.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }
}
