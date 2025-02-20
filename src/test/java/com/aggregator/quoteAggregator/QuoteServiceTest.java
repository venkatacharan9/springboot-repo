package com.aggregator.quoteAggregator;

import com.aggregator.quoteAggregator.dto.QuoteRatingDto;
import com.aggregator.quoteAggregator.dto.QuoteSourceDto;
import com.aggregator.quoteAggregator.entity.QuoteRating;
import com.aggregator.quoteAggregator.entity.QuoteSource;
import com.aggregator.quoteAggregator.entity.User;
import com.aggregator.quoteAggregator.repository.QuoteRatingRepository;
import com.aggregator.quoteAggregator.repository.QuoteSourceRepository;
import com.aggregator.quoteAggregator.repository.UserRepository;
import com.aggregator.quoteAggregator.service.QuoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuoteServiceTest {

    @Mock
    private QuoteSourceRepository quoteSourceRepository;

    @Mock
    private QuoteRatingRepository quoteRatingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private QuoteService quoteService;

    private QuoteSource activeQuoteSource;
    private User user;
    private QuoteRatingDto quoteRatingDto;

    @Test
    void fetchRandomQuote_test() {

        QuoteSource activeQuoteSource = new QuoteSource();
        activeQuoteSource.setApiUrl("https://thesimpsonsquoteapi.glitch.me/quotes");
        activeQuoteSource.setApiKey("some-api-key");
        activeQuoteSource.setName("Simpsons");
        activeQuoteSource.setActive(true);

        when(quoteSourceRepository.findByIsActiveTrueAndName("Simpsons")).thenReturn(activeQuoteSource);

        when(quoteSourceRepository.findByIsActiveTrueAndName("Simpsons")).thenReturn(null);
        Mono<String> quoteMono = quoteService.fetchRandomQuote("Ho");
        String result = quoteMono.block();
        assertEquals("No active quote sources available", result);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        activeQuoteSource = new QuoteSource();
        activeQuoteSource.setName("Simpsons");
        activeQuoteSource.setApiUrl("http://quotes.com");
        activeQuoteSource.setActive(true);

        user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        quoteRatingDto = new QuoteRatingDto();
        quoteRatingDto.setUserId(1L);
        quoteRatingDto.setQuoteText("Test Quote");
        quoteRatingDto.setAuthor("Test Author");
    }

    @Test
    void testVoteForQuote() {
        when(quoteRatingRepository.findByUserIdAndQuoteTextAndAuthor(anyLong(), anyString(), anyString()))
                .thenReturn(null);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        QuoteRating savedQuoteRating = new QuoteRating();
        savedQuoteRating.setQuoteText("Test Quote");
        savedQuoteRating.setAuthor("Test Author");
        savedQuoteRating.setUser(user);

        when(quoteRatingRepository.save(any(QuoteRating.class))).thenReturn(savedQuoteRating);

        QuoteRatingDto result = quoteService.voteForQuote(quoteRatingDto);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertEquals("Test Quote", result.getQuoteText());
    }

    @Test
    void testGetTopRatedQuotes() {
        List<Object[]> topRatedQuotes = List.of(
                new Object[]{"Test Quote", 5},
                new Object[]{"Another Quote", 3}
        );
        when(quoteRatingRepository.findTopRatedQuotes(anyInt())).thenReturn(topRatedQuotes);

        List<Map<String, Object>> result = quoteService.getTopRatedQuotes(2);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Quote", result.get(0).get("quoteText"));
        assertEquals(5, result.get(0).get("voteCount"));
    }

    @Test
    void testAddQuoteSource() {
        QuoteSourceDto quoteSourceDto = new QuoteSourceDto();
        quoteSourceDto.setName("Simpsons");
        quoteSourceDto.setApiUrl("http://quotes.com");
        quoteSourceDto.setActive(true);

        when(quoteSourceRepository.save(any(QuoteSource.class))).thenReturn(activeQuoteSource);

        QuoteSource result = quoteService.addQuoteSource(quoteSourceDto);

        assertNotNull(result);
        assertEquals("Simpsons", result.getName());
        assertEquals("http://quotes.com", result.getApiUrl());
    }

    @Test
    void testGenerateQuoteHash() {
        String quoteText = "Test Quote";
        String hash = quoteService.generateQuoteHash(quoteText);

        assertNotNull(hash);
        assertEquals(64, hash.length()); // SHA-256 hash should always be 64 characters
    }
}
