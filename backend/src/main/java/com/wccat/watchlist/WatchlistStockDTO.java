package com.wccat.watchlist;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 自选股展示 DTO（含行情）
 */
public record WatchlistStockDTO(
        Long id,
        String stockCode,
        String stockName,
        String market,
        BigDecimal currentPrice,
        BigDecimal prevClose,
        BigDecimal change,
        BigDecimal changePercent,
        LocalDateTime createdAt,
        LocalDateTime priceUpdatedAt
) {}
