package com.wccat.alert;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record AlertRuleCreateRequest(
        @NotBlank String stockCode,
        String stockName,
        @NotNull AlertSource source,
        @NotNull AlertConditionType conditionType,
        @NotNull BigDecimal conditionValue,
        List<String> notifyChannels  // WEB, EMAIL, WECHAT
) {}
