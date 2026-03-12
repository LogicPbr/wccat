package com.wccat.watchlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WatchlistRepository extends JpaRepository<WatchlistStock, Long> {

    Optional<WatchlistStock> findByStockCode(String stockCode);

    boolean existsByStockCode(String stockCode);
}
