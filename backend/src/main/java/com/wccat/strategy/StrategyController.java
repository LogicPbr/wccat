package com.wccat.strategy;

import com.wccat.common.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 策略模块 - 验证交易策略
 */
@RestController
@RequestMapping("/strategies")
@Tag(name = "策略", description = "交易策略验证与管理")
public class StrategyController {

    private final StrategyService strategyService;

    public StrategyController(StrategyService strategyService) {
        this.strategyService = strategyService;
    }

    @GetMapping
    @Operation(summary = "获取策略列表")
    public ApiResponse<List<StrategyDTO>> list() {
        return ApiResponse.ok(strategyService.list());
    }

    @PostMapping
    @Operation(summary = "创建策略")
    public ApiResponse<StrategyDTO> create(@Valid @RequestBody StrategyCreateRequest request) {
        return ApiResponse.ok(strategyService.create(request));
    }

    @PostMapping("/{id}/validate")
    @Operation(summary = "验证策略")
    public ApiResponse<StrategyValidateResult> validate(@PathVariable Long id) {
        return ApiResponse.ok(strategyService.validate(id));
    }
}
