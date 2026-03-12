package com.wccat.strategy;

import java.time.LocalDateTime;

public record StrategyDTO(
        Long id,
        String name,
        String description,
        String ruleExpression,
        StrategyStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
