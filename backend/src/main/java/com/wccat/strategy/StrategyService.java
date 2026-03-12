package com.wccat.strategy;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 策略服务 - 策略存储与验证逻辑
 */
@Service
public class StrategyService {

    private final AtomicLong idGenerator = new AtomicLong(1);
    private final ConcurrentHashMap<Long, StrategyDTO> store = new ConcurrentHashMap<>();

    public List<StrategyDTO> list() {
        return List.copyOf(store.values());
    }

    public StrategyDTO create(StrategyCreateRequest request) {
        long id = idGenerator.getAndIncrement();
        StrategyDTO dto = new StrategyDTO(
                id,
                request.name(),
                request.description(),
                request.ruleExpression(),
                StrategyStatus.DRAFT,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        store.put(id, dto);
        return dto;
    }

    public StrategyValidateResult validate(Long id) {
        StrategyDTO strategy = store.get(id);
        if (strategy == null) {
            throw new com.wccat.common.exception.BusinessException("STRATEGY_NOT_FOUND", "策略不存在");
        }
        // TODO: 实现策略表达式解析与验证
        return new StrategyValidateResult(true, "策略语法验证通过", null);
    }
}
