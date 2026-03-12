package com.wccat.watchlist;

import com.wccat.common.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 自选股服务 - 本地自选股管理，行情对接东方财富
 */
@Service
public class WatchlistService {

    private final WatchlistRepository repository;
    private final StockQuoteClient quoteClient;

    public WatchlistService(WatchlistRepository repository, StockQuoteClient quoteClient) {
        this.repository = repository;
        this.quoteClient = quoteClient;
    }

    @Transactional(readOnly = true)
    public List<WatchlistStockDTO> getStocks() {
        List<WatchlistStock> list = repository.findAll();
        return list.stream()
                .sorted((a, b) -> Integer.compare(a.getSortOrder(), b.getSortOrder()))
                .map(this::toBasicDTO)
                .toList();
    }

    @Transactional
    public WatchlistStockDTO addStock(String stockCode) {
        String code = stockCode.trim();
        if (repository.existsByStockCode(code)) {
            throw new BusinessException("STOCK_EXISTS", "该股票已在自选股中");
        }

        // 预拉取行情以验证代码有效性并获取名称
        StockQuoteClient.StockQuote quote = quoteClient.fetchQuote(code);
        if (quote == null) {
            throw new BusinessException("INVALID_STOCK", "股票代码无效或无法获取行情");
        }

        int maxOrder = repository.findAll().stream()
                .mapToInt(WatchlistStock::getSortOrder)
                .max()
                .orElse(-1);

        WatchlistStock entity = new WatchlistStock();
        entity.setStockCode(code);
        entity.setMarket(StockQuoteClient.inferMarket(code));
        entity.setStockName(quote.name());
        entity.setSortOrder(maxOrder + 1);
        repository.save(entity);

        return new WatchlistStockDTO(
                entity.getId(),
                entity.getStockCode(),
                quote.name(),
                entity.getMarket(),
                quote.currentPrice(),
                quote.prevClose(),
                quote.change(),
                quote.changePercent(),
                entity.getCreatedAt(),
                LocalDateTime.now()
        );
    }

    /**
     * 从同花顺导出的文本中解析股票代码并批量添加
     * 支持格式：每行 600519、600519 贵州茅台、1,600519,贵州茅台 等
     */
    @Transactional
    public ImportResult importFromTonghuashun(String text) {
        List<String> codes = parseStockCodes(text);
        int added = 0;
        int skipped = 0;
        List<ImportResult.ImportFailure> failures = new java.util.ArrayList<>();

        for (String code : codes) {
            try {
                if (repository.existsByStockCode(code)) {
                    skipped++;
                    continue;
                }
                StockQuoteClient.StockQuote quote = quoteClient.fetchQuote(code);
                if (quote == null) {
                    failures.add(new ImportResult.ImportFailure(code, "无法获取行情"));
                    continue;
                }
                int maxOrder = repository.findAll().stream()
                        .mapToInt(WatchlistStock::getSortOrder)
                        .max()
                        .orElse(-1);

                WatchlistStock entity = new WatchlistStock();
                entity.setStockCode(code);
                entity.setMarket(StockQuoteClient.inferMarket(code));
                entity.setStockName(quote.name());
                entity.setSortOrder(maxOrder + 1);
                repository.save(entity);
                added++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } catch (BusinessException e) {
                failures.add(new ImportResult.ImportFailure(code, e.getMessage()));
            }
        }

        return new ImportResult(added, skipped, failures.size(), failures);
    }

    /**
     * 从文本中提取所有6位股票代码（去重、保持顺序）
     * 沪市6、深市0/3、北交所4/8
     */
    private List<String> parseStockCodes(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\b([036][0-9]{5}|[48][0-9]{5})\\b");
        java.util.regex.Matcher m = p.matcher(text);
        java.util.LinkedHashSet<String> set = new java.util.LinkedHashSet<>();
        while (m.find()) {
            set.add(m.group(1));
        }
        return new java.util.ArrayList<>(set);
    }

    @Transactional
    public void removeStock(Long id) {
        if (!repository.existsById(id)) {
            throw new BusinessException("STOCK_NOT_FOUND", "自选股不存在", org.springframework.http.HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<WatchlistStockDTO> refreshPrices() {
        List<WatchlistStock> list = repository.findAll();
        List<WatchlistStockDTO> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (WatchlistStock entity : list) {
            StockQuoteClient.StockQuote quote = quoteClient.fetchQuote(entity.getStockCode());
            WatchlistStockDTO dto;
            if (quote != null) {
                dto = new WatchlistStockDTO(
                        entity.getId(),
                        entity.getStockCode(),
                        quote.name(),
                        entity.getMarket(),
                        quote.currentPrice(),
                        quote.prevClose(),
                        quote.change(),
                        quote.changePercent(),
                        entity.getCreatedAt(),
                        now
                );
                // 更新缓存的名称（若与数据库不一致）
                if (!quote.name().equals(entity.getStockName())) {
                    entity.setStockName(quote.name());
                    repository.save(entity);
                }
            } else {
                dto = new WatchlistStockDTO(
                        entity.getId(),
                        entity.getStockCode(),
                        entity.getStockName(),
                        entity.getMarket(),
                        null,
                        null,
                        null,
                        null,
                        entity.getCreatedAt(),
                        null
                );
            }
            result.add(dto);
            try {
                Thread.sleep(100);  // 限速，避免请求过快
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return result;
    }

    private WatchlistStockDTO toBasicDTO(WatchlistStock entity) {
        return new WatchlistStockDTO(
                entity.getId(),
                entity.getStockCode(),
                entity.getStockName(),
                entity.getMarket(),
                null,
                null,
                null,
                null,
                entity.getCreatedAt(),
                null
        );
    }
}
