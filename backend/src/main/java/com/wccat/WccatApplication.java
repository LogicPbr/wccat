package com.wccat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * WCCAT - AI化投资辅助平台
 * <p>
 * 功能模块：
 * - 资讯拉取 (News)
 * - 策略验证 (Strategy)
 * - 自定义回测 (Backtest)
 * - 券商账户连接 (平安、华宝持仓)
 * - 自选股连接 (同花顺)
 * - 报警规则 (持仓/自选股买卖提醒)
 */
@SpringBootApplication
public class WccatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WccatApplication.class, args);
    }
}
