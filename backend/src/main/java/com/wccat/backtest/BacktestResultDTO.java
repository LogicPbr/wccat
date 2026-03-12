package com.wccat.backtest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record BacktestResultDTO(
        Long id,
        Long strategyId,
        String stockCode,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalReturn,
        BigDecimal sharpeRatio,
        int tradeCount,
        LocalDateTime runAt
) {}
