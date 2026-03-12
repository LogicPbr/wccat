# WCCAT 架构文档

## 项目概述

WCCAT 是一个 AI 化投资辅助平台，采用 B/S 架构，前后端分离设计。

### 核心功能

| 模块 | 描述 |
|------|------|
| **资讯** | 拉取财经资讯（东方财富、同花顺、新浪等） |
| **策略** | 创建、验证交易策略规则 |
| **回测** | 自定义策略历史数据回测 |
| **券商** | 连接平安证券、华宝证券，获取持仓 |
| **自选股** | 连接同花顺，同步自选股 |
| **报警** | 持仓/自选股价格、涨跌幅报警，多通道推送 |

---

## 技术栈

### 后端

- **Java 21** + **Spring Boot 3.4**
- **Gradle 8.x** 构建
- **Virtual Threads** (Project Loom) 提升并发
- **H2 / MySQL** 持久化
- **Spring Security** 认证（预留 JWT）
- **SpringDoc OpenAPI** 文档
- **Record / 不可变 DTO** 现代化数据建模

### 前端

- **Vue 3** + **TypeScript**
- **Vite 6** 构建
- **Vue Router 4** 路由
- **Pinia** 状态管理
- **Axios** HTTP 客户端

---

## 项目结构

```
wccat/
├── backend/                 # Java 后端
│   ├── src/main/java/com/wccat/
│   │   ├── WccatApplication.java
│   │   ├── common/          # 通用组件
│   │   │   ├── exception/   # 异常处理
│   │   │   └── web/         # 响应封装
│   │   ├── config/          # 配置
│   │   ├── news/            # 资讯模块
│   │   ├── strategy/        # 策略模块
│   │   ├── backtest/        # 回测模块
│   │   ├── broker/          # 券商模块
│   │   ├── watchlist/       # 自选股模块
│   │   └── alert/           # 报警模块
│   └── src/main/resources/
├── frontend/                # Vue 前端
│   ├── src/
│   │   ├── api/             # API 调用
│   │   ├── views/           # 页面
│   │   ├── layouts/         # 布局
│   │   ├── router/          # 路由
│   │   └── assets/          # 静态资源
│   └── index.html
├── docs/                    # 文档
├── docker-compose.yml       # 本地开发依赖
└── README.md
```

---

## 设计原则

### 1. 按功能分包 (Package by Feature)

每个业务模块独立包，内含 Controller、Service、DTO，便于定位与扩展。

### 2. 接口先行

- RESTful API 设计
- OpenAPI/Swagger 文档
- 统一 `ApiResponse<T>` 封装

### 3. 扩展点预留

- 券商、自选股、资讯均预留对接接口
- 可接入 tushare、akshare、东方财富等数据源
- 报警支持 WEB、EMAIL、WECHAT 等多通道

### 4. 安全与配置

- 生产环境需配置认证（JWT/OAuth2）
- 敏感配置通过环境变量/配置中心管理
- CORS 可配置

---

## 扩展指引

### 对接真实数据源

1. **资讯**：在 `NewsService` 中实现 `NewsFetcher` 接口，接入各数据源
2. **回测**：集成 tushare/akshare 获取历史 K 线，实现 `BacktestEngine`
3. **券商**：平安、华宝需使用其开放 API 或 XTP/恒生等交易接口
4. **同花顺**：使用同花顺开放 API 或 iFinD 接口

### 报警推送

- 实现 `AlertNotifier` 接口
- 支持 WebSocket 实时推送、邮件、企业微信等

---

## 本地开发

```bash
# 后端
cd backend && ./gradlew bootRun

# 前端
cd frontend && npm install && npm run dev

# API 文档：http://localhost:8080/api/swagger-ui.html
# 前端：http://localhost:5173
```
