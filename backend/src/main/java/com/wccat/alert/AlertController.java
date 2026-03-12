package com.wccat.alert;

import com.wccat.common.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 报警模块 - 持仓/自选股买卖提醒
 */
@RestController
@RequestMapping("/alerts")
@Tag(name = "报警", description = "买卖报警规则管理")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    @Operation(summary = "获取报警规则列表")
    public ApiResponse<List<AlertRuleDTO>> list() {
        return ApiResponse.ok(alertService.list());
    }

    @PostMapping
    @Operation(summary = "创建报警规则")
    public ApiResponse<AlertRuleDTO> create(@Valid @RequestBody AlertRuleCreateRequest request) {
        return ApiResponse.ok(alertService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新报警规则")
    public ApiResponse<AlertRuleDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AlertRuleUpdateRequest request) {
        return ApiResponse.ok(alertService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除报警规则")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        alertService.delete(id);
        return ApiResponse.ok();
    }

    @PostMapping("/{id}/toggle")
    @Operation(summary = "启用/禁用报警规则")
    public ApiResponse<AlertRuleDTO> toggle(@PathVariable Long id) {
        return ApiResponse.ok(alertService.toggle(id));
    }
}
