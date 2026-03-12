package com.wccat.backtest;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BacktestRequest(
        @NotNull Long strategyId,
        @NotNull String stockCode,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {}
