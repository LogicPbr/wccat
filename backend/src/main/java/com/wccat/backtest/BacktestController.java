package com.wccat.backtest;

import com.wccat.common.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 回测模块 - 自定义策略回测
 */
@RestController
@RequestMapping("/backtests")
@Tag(name = "回测", description = "策略回测执行与结果")
public class BacktestController {

    private final BacktestService backtestService;

    public BacktestController(BacktestService backtestService) {
        this.backtestService = backtestService;
    }

    @GetMapping
    @Operation(summary = "获取回测列表")
    public ApiResponse<List<BacktestResultDTO>> list() {
        return ApiResponse.ok(backtestService.list());
    }

    @PostMapping("/run")
    @Operation(summary = "执行回测")
    public ApiResponse<BacktestResultDTO> run(@Valid @RequestBody BacktestRequest request) {
        return ApiResponse.ok(backtestService.run(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取回测详情")
    public ApiResponse<BacktestResultDTO> getById(@PathVariable Long id) {
        return ApiResponse.ok(backtestService.getById(id));
    }
}
