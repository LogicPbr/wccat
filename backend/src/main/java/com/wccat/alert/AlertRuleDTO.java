package com.wccat.alert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AlertRuleDTO(
        Long id,
        String stockCode,
        String stockName,
        AlertSource source,
        AlertConditionType conditionType,
        BigDecimal conditionValue,
        List<String> notifyChannels,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
