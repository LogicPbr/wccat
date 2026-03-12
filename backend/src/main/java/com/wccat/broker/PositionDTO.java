package com.wccat.broker;

import java.math.BigDecimal;

public record PositionDTO(
        String stockCode,
        String stockName,
        int quantity,
        BigDecimal costPrice,
        BigDecimal currentPrice,
        BigDecimal marketValue,
        BigDecimal profitLoss,
        BrokerType brokerType
) {}
