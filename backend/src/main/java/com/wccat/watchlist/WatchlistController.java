package com.wccat.watchlist;

import com.wccat.common.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 自选股模块 - 本地自选股管理，行情来自东方财富
 */
@RestController
@RequestMapping("/watchlist")
@Tag(name = "自选股", description = "自选股管理与实时行情")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @GetMapping("/stocks")
    @Operation(summary = "获取自选股列表")
    public ApiResponse<List<WatchlistStockDTO>> getStocks() {
        return ApiResponse.ok(watchlistService.getStocks());
    }

    @PostMapping("/stocks")
    @Operation(summary = "添加自选股")
    public ApiResponse<WatchlistStockDTO> addStock(@Valid @RequestBody AddStockRequest request) {
        return ApiResponse.ok(watchlistService.addStock(request.stockCode()));
    }

    @DeleteMapping("/stocks/{id}")
    @Operation(summary = "移除自选股")
    public ApiResponse<Void> removeStock(@PathVariable Long id) {
        watchlistService.removeStock(id);
        return ApiResponse.ok();
    }

    @PostMapping("/stocks/refresh")
    @Operation(summary = "刷新行情")
    public ApiResponse<List<WatchlistStockDTO>> refreshPrices() {
        return ApiResponse.ok(watchlistService.refreshPrices());
    }

    @PostMapping("/stocks/import-from-tonghuashun")
    @Operation(summary = "从同花顺导入")
    public ApiResponse<ImportResult> importFromTonghuashun(@Valid @RequestBody ImportFromTonghuashunRequest request) {
        return ApiResponse.ok(watchlistService.importFromTonghuashun(request.text()));
    }
}
