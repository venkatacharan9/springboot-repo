package com.aggregator.quoteAggregator.service;

import com.aggregator.quoteAggregator.dto.QuoteRatingDto;
import com.aggregator.quoteAggregator.dto.QuoteSourceDto;
import com.aggregator.quoteAggregator.entity.QuoteRating;
import com.aggregator.quoteAggregator.entity.QuoteSource;
import com.aggregator.quoteAggregator.entity.User;
import com.aggregator.quoteAggregator.repository.QuoteRatingRepository;
import com.aggregator.quoteAggregator.repository.QuoteSourceRepository;
import com.aggregator.quoteAggregator.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuoteService {

    @Autowired
    private QuoteSourceRepository quoteSourceRepository;

    @Autowired
    private QuoteRatingRepository quoteRatingRepository;

    @Autowired
    private UserRepository userRepository;

    private final WebClient webClient= WebClient.builder().build();;  // Injecting WebClient Builder

    // Fetch a random quote from an active source
    public Mono<String> fetchRandomQuote(String author) {
        QuoteSource source = Optional.ofNullable(author)
                .map(a -> quoteSourceRepository.findByIsActiveTrueAndName("Simpsons"))
                .orElseGet(() -> quoteSourceRepository.findByIsActiveTrue());

        if (source != null) {
            String modifiedUrl = source.getApiUrl();
            if(author!=null && !author.isEmpty())
                 modifiedUrl = modifiedUrl + "?character=" + author;
            WebClient.RequestHeadersSpec<?> requestSpec = createRequest(source,modifiedUrl);
            return requestSpec.retrieve()
                    .bodyToMono(String.class);
        }
        return Mono.just("No active quote sources available");
    }

    private WebClient.RequestHeadersSpec<?> createRequest(QuoteSource quoteSource, String modifiedUrl) {
        WebClient.RequestBodySpec requestBodySpec = webClient
                .method(org.springframework.http.HttpMethod.valueOf(quoteSource.getHttpMethod()))
                .uri(modifiedUrl);
        quoteSource.getHeaders().forEach(requestBodySpec::header);
        return requestBodySpec;
    }

    // Allow a user to vote for a quote
    @Transactional
    public QuoteRatingDto voteForQuote(QuoteRatingDto quoteRatingDto) {
        QuoteRating quoteRating = new QuoteRating();
        QuoteRatingDto rateDto = new QuoteRatingDto();
        if(quoteRatingRepository.findByUserIdAndQuoteTextAndAuthor(quoteRatingDto.getUserId(),quoteRatingDto.getQuoteText(),quoteRatingDto.getAuthor())==null) {
            BeanUtils.copyProperties(quoteRatingDto, quoteRating);
            Optional<User> user = userRepository.findById(quoteRatingDto.getUserId());
            user.ifPresent(quoteRating::setUser);
            quoteRating.setQuoteHash(generateQuoteHash(quoteRatingDto.getQuoteText()));
            QuoteRating quote = quoteRatingRepository.save(quoteRating);
            rateDto.setUsername(user.map(User::getUsername).orElse("Unknown"));
            BeanUtils.copyProperties(quote, rateDto);
        }
        else{
        throw new IllegalStateException("Quote already rated by user.");
        }
        return rateDto;
    }

    public List<Map<String, Object>> getTopRatedQuotes(int limit) {
        List<Object[]> results = quoteRatingRepository.findTopRatedQuotes(limit);
        return results.stream().map(obj -> Map.of(
                "quoteText", obj[0],
                "voteCount", obj[1]
        )).collect(Collectors.toList());
    }

    // Add a new quote source
    @Transactional
    public QuoteSource addQuoteSource(QuoteSourceDto quoteSourceDto) {
        QuoteSource quoteSource = new QuoteSource();
        BeanUtils.copyProperties(quoteSourceDto, quoteSource);
        if (quoteSourceDto.getHeaders() != null) {
            quoteSource.setHeaders(new HashMap<>(quoteSourceDto.getHeaders()));
        }
        return quoteSourceRepository.save(quoteSource);
    }

    public static String generateQuoteHash(String quoteText) {
        try {
            String normalizedText = quoteText.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(normalizedText.getBytes(StandardCharsets.UTF_8));
            return String.format("%064x", new BigInteger(1, hashBytes));
        } catch (Exception e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }

}
