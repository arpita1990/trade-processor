package com.cme.trading.listener;

import com.cme.trading.cache.QuoteCache;
import com.cme.trading.model.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class QuoteConsumerTest {

    @Mock
    private QuoteCache quoteCache;

    @InjectMocks
    private QuoteConsumer quoteConsumer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCacheReceivedQuote() {
        Quote quote = new Quote();
        quote.setInstrumentID("instrument1");

        quoteConsumer.listenQuotes(quote);

        verify(quoteCache, times(1)).put(quote.getInstrumentID(), quote);
    }
}