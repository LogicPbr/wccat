package com.wccat.news;

import java.time.LocalDateTime;

/**
 * 资讯 DTO
 */
public record NewsDTO(
        Long id,
        String title,
        String content,
        String source,
        String url,
        LocalDateTime publishTime,
        LocalDateTime fetchTime
) {}
