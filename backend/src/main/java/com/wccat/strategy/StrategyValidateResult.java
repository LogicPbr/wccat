package com.wccat.strategy;

public record StrategyValidateResult(
        boolean success,
        String message,
        String errorDetail
) {}
