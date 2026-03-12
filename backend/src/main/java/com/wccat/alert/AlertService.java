package com.wccat.alert;

import com.wccat.common.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 报警服务 - 价格/涨跌幅报警规则
 * 支持持仓股和自选股，触发后可推送站内消息/邮件/微信等
 */
@Service
public class AlertService {

    private final AtomicLong idGenerator = new AtomicLong(1);
    private final ConcurrentHashMap<Long, AlertRuleDTO> store = new ConcurrentHashMap<>();

    public List<AlertRuleDTO> list() {
        return store.values().stream()
                .sorted((a, b) -> b.createdAt().compareTo(a.createdAt()))
                .toList();
    }

    public AlertRuleDTO create(AlertRuleCreateRequest request) {
        long id = idGenerator.getAndIncrement();
        AlertRuleDTO dto = new AlertRuleDTO(
                id,
                request.stockCode(),
                request.stockName(),
                request.source(),  // POSITION | WATCHLIST
                request.conditionType(),  // PRICE_ABOVE | PRICE_BELOW | CHANGE_ABOVE | CHANGE_BELOW
                request.conditionValue(),
                request.notifyChannels(),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        store.put(id, dto);
        return dto;
    }

    public AlertRuleDTO update(Long id, AlertRuleUpdateRequest request) {
        AlertRuleDTO existing = store.get(id);
        if (existing == null) {
            throw new BusinessException("ALERT_NOT_FOUND", "报警规则不存在", org.springframework.http.HttpStatus.NOT_FOUND);
        }
        AlertRuleDTO updated = new AlertRuleDTO(
                id,
                request.stockCode() != null ? request.stockCode() : existing.stockCode(),
                request.stockName() != null ? request.stockName() : existing.stockName(),
                existing.source(),
                request.conditionType() != null ? request.conditionType() : existing.conditionType(),
                request.conditionValue() != null ? request.conditionValue() : existing.conditionValue(),
                request.notifyChannels() != null ? request.notifyChannels() : existing.notifyChannels(),
                existing.enabled(),
                existing.createdAt(),
                LocalDateTime.now()
        );
        store.put(id, updated);
        return updated;
    }

    public void delete(Long id) {
        if (store.remove(id) == null) {
            throw new BusinessException("ALERT_NOT_FOUND", "报警规则不存在", org.springframework.http.HttpStatus.NOT_FOUND);
        }
    }

    public AlertRuleDTO toggle(Long id) {
        AlertRuleDTO existing = store.get(id);
        if (existing == null) {
            throw new BusinessException("ALERT_NOT_FOUND", "报警规则不存在", org.springframework.http.HttpStatus.NOT_FOUND);
        }
        AlertRuleDTO toggled = new AlertRuleDTO(
                id,
                existing.stockCode(),
                existing.stockName(),
                existing.source(),
                existing.conditionType(),
                existing.conditionValue(),
                existing.notifyChannels(),
                !existing.enabled(),
                existing.createdAt(),
                LocalDateTime.now()
        );
        store.put(id, toggled);
        return toggled;
    }
}
