package com.wccat.backtest;

import com.wccat.common.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 回测服务 - 策略历史数据回测
 * 预留对接 tushare、akshare 等数据源
 */
@Service
public class BacktestService {

    private final AtomicLong idGenerator = new AtomicLong(1);
    private final ConcurrentHashMap<Long, BacktestResultDTO> store = new ConcurrentHashMap<>();

    public List<BacktestResultDTO> list() {
        return store.values().stream()
                .sorted((a, b) -> b.runAt().compareTo(a.runAt()))
                .toList();
    }

    public BacktestResultDTO run(BacktestRequest request) {
        // TODO: 实现真实回测引擎，对接历史K线数据
        long id = idGenerator.getAndIncrement();
        BacktestResultDTO result = new BacktestResultDTO(
                id,
                request.strategyId(),
                request.stockCode(),
                request.startDate(),
                request.endDate(),
                new BigDecimal("0.15"),  // 模拟收益率
                new BigDecimal("1.25"),  // 模拟夏普比率
                120,  // 模拟交易次数
                LocalDateTime.now()
        );
        store.put(id, result);
        return result;
    }

    public BacktestResultDTO getById(Long id) {
        BacktestResultDTO result = store.get(id);
        if (result == null) {
            throw new BusinessException("BACKTEST_NOT_FOUND", "回测记录不存在", org.springframework.http.HttpStatus.NOT_FOUND);
        }
        return result;
    }
}
