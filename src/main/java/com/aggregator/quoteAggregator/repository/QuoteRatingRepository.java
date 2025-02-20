package com.aggregator.quoteAggregator.repository;

import com.aggregator.quoteAggregator.entity.QuoteRating;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRatingRepository extends JpaRepository<QuoteRating,Long> {

    @Query(value = "SELECT MIN(q.quoteText) AS quoteText,COUNT(q.quoteHash) AS voteCount "
            + "FROM QuoteRating q "
            + "GROUP BY q.quoteHash "
            + "ORDER BY voteCount DESC "
            + "LIMIT :limit")
    List<Object[]> findTopRatedQuotes(@Param("limit") int limit);

    QuoteRating findByUserIdAndQuoteTextAndAuthor(Long userId,String quoteText, String author);

}
