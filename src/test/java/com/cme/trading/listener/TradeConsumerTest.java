package com.cme.trading.listener;

import com.cme.trading.cache.QuoteCache;
import com.cme.trading.listener.TradeConsumer;
import com.cme.trading.model.Output;
import com.cme.trading.model.Quote;
import com.cme.trading.model.Trade;
import com.cme.trading.service.OutputService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TradeConsumerTest {

    @Mock
    private QuoteCache quoteCache;

    @Mock
    private OutputService outputService;

    @Mock
    private KafkaTemplate<String, Output> outputKafkaTemplate;

    @InjectMocks
    private TradeConsumer tradeConsumer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldProcessTradeAndSendOutput() {
        Trade trade = new Trade();
        trade.setInstrumentID("instrument1");
        Quote quote = new Quote();
        Output output = new Output();

        when(quoteCache.take(trade.getInstrumentID())).thenReturn(quote);
        when(outputService.createOutput(trade, quote)).thenReturn(output);

        tradeConsumer.listenTrades(trade);

        verify(outputKafkaTemplate).send("output-topic", output);
    }
}