package com.wccat.alert;

import java.math.BigDecimal;
import java.util.List;

public record AlertRuleUpdateRequest(
        String stockCode,
        String stockName,
        AlertConditionType conditionType,
        BigDecimal conditionValue,
        List<String> notifyChannels
) {}
