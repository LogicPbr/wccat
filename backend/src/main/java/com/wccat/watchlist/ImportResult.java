package com.wccat.watchlist;

import java.util.List;

/**
 * 批量导入结果
 */
public record ImportResult(
        int added,
        int skipped,
        int failed,
        List<ImportFailure> failures
) {
    public record ImportFailure(String stockCode, String reason) {}
}
