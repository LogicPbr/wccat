package com.wccat.watchlist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddStockRequest(
        @NotBlank(message = "股票代码不能为空")
        @Pattern(regexp = "^[0-9]{6}$", message = "股票代码需为6位数字")
        String stockCode
) {}
