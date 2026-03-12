package com.wccat.strategy;

import jakarta.validation.constraints.NotBlank;

public record StrategyCreateRequest(
        @NotBlank(message = "策略名称不能为空")
        String name,
        String description,
        @NotBlank(message = "策略规则不能为空")
        String ruleExpression
) {}
