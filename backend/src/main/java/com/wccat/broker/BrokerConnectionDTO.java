package com.wccat.broker;

import java.time.LocalDateTime;

public record BrokerConnectionDTO(
        BrokerType brokerType,
        String maskedAccount,
        BrokerConnectionStatus status,
        LocalDateTime connectedAt
) {}
