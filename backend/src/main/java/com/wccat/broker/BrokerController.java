package com.wccat.broker;

import com.wccat.common.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 券商模块 - 连接平安证券、华宝证券获取持仓
 */
@RestController
@RequestMapping("/broker")
@Tag(name = "券商", description = "券商账户连接与持仓同步")
public class BrokerController {

    private final BrokerService brokerService;

    public BrokerController(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    @GetMapping("/connections")
    @Operation(summary = "获取已连接券商列表")
    public ApiResponse<List<BrokerConnectionDTO>> listConnections() {
        return ApiResponse.ok(brokerService.listConnections());
    }

    @PostMapping("/connections/{brokerType}/connect")
    @Operation(summary = "连接券商账户")
    public ApiResponse<BrokerConnectionDTO> connect(
            @PathVariable BrokerType brokerType,
            @Valid @RequestBody BrokerConnectRequest request) {
        return ApiResponse.ok(brokerService.connect(brokerType, request));
    }

    @GetMapping("/positions")
    @Operation(summary = "获取合并持仓")
    public ApiResponse<List<PositionDTO>> getPositions() {
        return ApiResponse.ok(brokerService.getPositions());
    }

    @PostMapping("/positions/sync")
    @Operation(summary = "同步持仓")
    public ApiResponse<Void> syncPositions() {
        brokerService.syncPositions();
        return ApiResponse.ok();
    }
}
