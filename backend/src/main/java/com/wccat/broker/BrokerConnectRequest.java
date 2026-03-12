package com.wccat.broker;

import jakarta.validation.constraints.NotBlank;

public record BrokerConnectRequest(
        @NotBlank String account,
        @NotBlank String password
) {}
