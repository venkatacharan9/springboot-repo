package com.aggregator.quoteAggregator.controller;

import com.aggregator.quoteAggregator.dto.QuoteRatingDto;
import com.aggregator.quoteAggregator.dto.QuoteSourceDto;
import com.aggregator.quoteAggregator.entity.QuoteRating;
import com.aggregator.quoteAggregator.entity.QuoteSource;
import com.aggregator.quoteAggregator.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    @Autowired
    private QuoteService quoteService;

    // Endpoint to fetch a random quote from one of the APIs
    @GetMapping("/random")
    public Mono<String> getRandomQuote(@RequestParam(value = "author", required = false) String author) {
        return quoteService.fetchRandomQuote(author);
    }

    // Endpoint to allow users to vote for a quote
    @PostMapping("/vote")
    public QuoteRatingDto voteForQuote(@RequestBody QuoteRatingDto quoteRatingDto) {
        return quoteService.voteForQuote(quoteRatingDto);
    }

    // Endpoint to get the hall of fame (top 10 ranked quotes)
    @GetMapping("/halloffame")
    public List<Map<String, Object>> getHallOfFame(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        return quoteService.getTopRatedQuotes(limit);
    }

    // Endpoint to add a new quote source (only admins should have this privilege)
    //After Implementing the authentication we can use Spring annotation @preAuthorize for restricting role based authorization
    @PostMapping("/add-source")
    public QuoteSource addQuoteSource(@RequestBody QuoteSourceDto quoteSourceDto) {
        return quoteService.addQuoteSource(quoteSourceDto);
    }
}