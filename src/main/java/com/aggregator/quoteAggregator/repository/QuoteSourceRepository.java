package com.aggregator.quoteAggregator.repository;

import com.aggregator.quoteAggregator.entity.QuoteSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteSourceRepository extends JpaRepository<QuoteSource,Long> {
    @Query(value = "SELECT * FROM quote_sources q where q.is_active=true ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    QuoteSource findByIsActiveTrue();

    QuoteSource findByIsActiveTrueAndName(String name);
}
