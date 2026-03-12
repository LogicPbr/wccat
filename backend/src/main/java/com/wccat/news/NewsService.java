package com.wccat.news;

import com.wccat.common.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 资讯服务 - 拉取与存储金融资讯
 * 预留对接东方财富、同花顺、新浪财经等数据源
 */
@Service
public class NewsService {

    private final AtomicLong idGenerator = new AtomicLong(1);
    private final ConcurrentHashMap<Long, NewsDTO> store = new ConcurrentHashMap<>();

    public List<NewsDTO> list(int page, int size, String source, String keyword) {
        return store.values().stream()
                .filter(n -> source == null || source.equals(n.source()))
                .filter(n -> keyword == null || n.title().contains(keyword))
                .sorted((a, b) -> b.publishTime().compareTo(a.publishTime()))
                .skip((long) (page - 1) * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public NewsDTO getById(Long id) {
        NewsDTO news = store.get(id);
        if (news == null) {
            throw new BusinessException("NEWS_NOT_FOUND", "资讯不存在", org.springframework.http.HttpStatus.NOT_FOUND);
        }
        return news;
    }

    public void refresh() {
        // TODO: 对接实际数据源 API
        // 示例：添加一条模拟数据
        NewsDTO sample = new NewsDTO(
                idGenerator.getAndIncrement(),
                "A股市场早盘分析",
                "今日A股三大指数小幅高开...",
                "东方财富",
                "https://example.com/news/1",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        store.put(sample.id(), sample);
    }
}
