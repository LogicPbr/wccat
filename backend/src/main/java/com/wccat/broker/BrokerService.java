package com.wccat.broker;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 券商服务 - 平安、华宝账户连接与持仓同步
 * 实际对接需使用各券商开放 API 或 XTP/恒生等交易接口
 */
@Service
public class BrokerService {

    private final ConcurrentHashMap<BrokerType, BrokerConnectionDTO> connections = new ConcurrentHashMap<>();

    public List<BrokerConnectionDTO> listConnections() {
        return new ArrayList<>(connections.values());
    }

    public BrokerConnectionDTO connect(BrokerType brokerType, BrokerConnectRequest request) {
        BrokerConnectionDTO conn = new BrokerConnectionDTO(
                brokerType,
                "***" + request.account().substring(Math.max(0, request.account().length() - 4)),
                BrokerConnectionStatus.CONNECTED,
                LocalDateTime.now()
        );
        connections.put(brokerType, conn);
        return conn;
    }

    public List<PositionDTO> getPositions() {
        // TODO: 从券商 API 获取真实持仓
        return List.of();
    }

    public void syncPositions() {
        // TODO: 调用各券商 API 同步持仓到本地
    }
}
