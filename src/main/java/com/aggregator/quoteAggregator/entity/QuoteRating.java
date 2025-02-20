package com.aggregator.quoteAggregator.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "quote_ratings")
public class QuoteRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String quoteText;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String quoteHash;

    @Column(nullable = false)
    private Integer rating=0;

    public QuoteRating() {
    }


    public QuoteRating(User user, String quoteText, String author, String quoteHash, Integer rating) {
        this.user = user;
        this.quoteText = quoteText;
        this.author = author;
        this.quoteHash = quoteHash;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
