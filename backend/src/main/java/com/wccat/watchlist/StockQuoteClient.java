package com.wccat.watchlist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * 东方财富行情接口 - 获取实时股价
 * API: https://push2.eastmoney.com/api/qt/stock/get
 */
@Component
public class StockQuoteClient {

    private static final Logger log = LoggerFactory.getLogger(StockQuoteClient.class);
    private static final String BASE_URL = "https://push2.eastmoney.com/api/qt/stock/get";
    private static final String FIELDS = "f43,f57,f58,f60,f168,f169";  // 现价,代码,名称,昨收,涨跌整数,涨跌小数

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public StockQuoteClient(ObjectMapper objectMapper) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = objectMapper;
    }

    /**
     * 将股票代码转为 secid 格式
     * 沪市 6xxxxx -> 1.60xxxx
     * 深市/北交所 0xxxxx,3xxxxx,4xxxxx,8xxxxx -> 0.xxxxxx
     */
    public static String toSecId(String stockCode) {
        if (stockCode == null || stockCode.length() < 6) {
            return null;
        }
        String code = stockCode.trim();
        if (code.startsWith("6")) {
            return "1." + code;
        }
        return "0." + code;
    }

    /**
     * 根据股票代码推断市场
     */
    public static String inferMarket(String stockCode) {
        if (stockCode == null || stockCode.length() < 6) {
            return "UN";
        }
        char first = stockCode.charAt(0);
        return switch (first) {
            case '6' -> "SH";
            case '0', '3' -> "SZ";
            case '4', '8' -> "BJ";
            default -> "UN";
        };
    }

    public StockQuote fetchQuote(String stockCode) {
        String secId = toSecId(stockCode);
        if (secId == null) {
            return null;
        }
        try {
            String url = BASE_URL + "?secid=" + secId + "&fields=" + FIELDS;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Mozilla/5.0 (compatible; Wccat/1.0)")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.warn("Stock quote API returned {} for {}", response.statusCode(), stockCode);
                return null;
            }

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode data = root.path("data");
            if (data.isMissingNode() || data.isNull()) {
                log.debug("No quote data for {}", stockCode);
                return null;
            }

            BigDecimal currentPrice = toPrice(data.path("f43"));
            BigDecimal prevClose = toPrice(data.path("f60"));
            BigDecimal change = BigDecimal.ZERO;
            BigDecimal changePercent = BigDecimal.ZERO;
            if (prevClose.compareTo(BigDecimal.ZERO) > 0 && currentPrice != null) {
                change = currentPrice.subtract(prevClose);
                changePercent = change.divide(prevClose, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            }

            return new StockQuote(
                    data.path("f57").asText(""),
                    data.path("f58").asText(""),
                    currentPrice,
                    prevClose,
                    change,
                    changePercent
            );
        } catch (Exception e) {
            log.warn("Failed to fetch quote for {}: {}", stockCode, e.getMessage());
            return null;
        }
    }

    private static BigDecimal toPrice(JsonNode node) {
        if (node.isMissingNode() || node.isNull()) {
            return BigDecimal.ZERO;
        }
        long v = node.asLong(0);
        return BigDecimal.valueOf(v).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public record StockQuote(String code, String name, BigDecimal currentPrice,
                            BigDecimal prevClose, BigDecimal change, BigDecimal changePercent) {}
}
