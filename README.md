# WCCAT - AI 化投资辅助平台

B/S 架构、前后端分离的投资辅助平台，支持资讯拉取、策略验证、自定义回测、券商账户连接、自选股同步与买卖报警。

## 功能模块

| 模块 | 描述 |
|------|------|
| **资讯** | 拉取财经资讯 |
| **策略** | 验证交易策略 |
| **回测** | 自定义策略历史回测 |
| **券商** | 连接平安证券、华宝证券，获取持仓 |
| **自选股** | 连接同花顺，同步自选股 |
| **报警** | 持仓/自选股买卖提醒 |

## 技术栈

- **后端**: Java 21 + Spring Boot 3.4 + Gradle
- **前端**: Vue 3 + TypeScript + Vite + Pinia

## 快速开始

### 环境要求

- JDK 21
- Node.js 18+
- (可选) Docker：用于 MySQL、Redis

### 后端

```bash
cd backend
./gradlew bootRun
```

- API 文档: http://localhost:8080/api/swagger-ui.html
- 端口: 8080，上下文路径: `/api`

### 前端

```bash
cd frontend
npm install
npm run dev
```

- 地址: http://localhost:5173
- 开发代理已配置：`/api` 转发至后端

### Docker（可选）

```bash
docker-compose up -d   # MySQL 3306, Redis 6379
```

## 项目结构

```
wccat/
├── backend/          # Java 后端 (按功能分包)
├── frontend/         # Vue 3 前端
├── docs/             # 架构文档
└── docker-compose.yml
```

## 扩展指引

- 券商、同花顺对接需使用各平台开放 API 或 XTP/恒生等交易接口
- 资讯、回测数据可接入 tushare、akshare 等数据源
- 详见 [架构文档](docs/ARCHITECTURE.md)

## License

MIT
