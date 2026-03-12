package com.wccat.news;

import com.wccat.common.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资讯模块 - 拉取金融资讯
 */
@RestController
@RequestMapping("/news")
@Tag(name = "资讯", description = "金融资讯拉取与管理")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/list")
    @Operation(summary = "获取资讯列表")
    public ApiResponse<List<NewsDTO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String keyword) {
        return ApiResponse.ok(newsService.list(page, size, source, keyword));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取资讯详情")
    public ApiResponse<NewsDTO> getById(@PathVariable Long id) {
        return ApiResponse.ok(newsService.getById(id));
    }

    @PostMapping("/refresh")
    @Operation(summary = "手动刷新资讯")
    public ApiResponse<Void> refresh() {
        newsService.refresh();
        return ApiResponse.ok();
    }
}
