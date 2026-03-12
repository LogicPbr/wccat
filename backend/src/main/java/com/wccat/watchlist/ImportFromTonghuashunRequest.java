package com.wccat.watchlist;

import jakarta.validation.constraints.NotBlank;

/**
 * 从同花顺导入请求 - 粘贴导出的文本内容
 */
public record ImportFromTonghuashunRequest(
        @NotBlank(message = "请粘贴同花顺导出的内容")
        String text
) {}
