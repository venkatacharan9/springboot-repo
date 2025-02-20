package com.aggregator.quoteAggregator.dto;

import com.aggregator.quoteAggregator.entity.User;

public class QuoteRatingDto {

    private Long userId;

    private String quoteText;

    private String author;

    private String quoteHash;

    private Integer rating;

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQuoteHash() {
        return quoteHash;
    }

    public void setQuoteHash(String quoteHash) {
        this.quoteHash = quoteHash;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

}
