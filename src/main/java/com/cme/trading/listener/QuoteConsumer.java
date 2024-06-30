package com.cme.trading.listener;

import com.cme.trading.cache.QuoteCache;
import com.cme.trading.model.Quote;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QuoteConsumer {

    @Autowired
    private QuoteCache quoteCache;

    @KafkaListener(topics = "quote-topic", groupId = "quote-processor", containerFactory = "quoteKafkaListenerContainerFactory")
    public void listenQuotes(Quote quote) {

        log.info("Quote received: {}", quote);
        quoteCache.put(quote.getInstrumentID(), quote);
        log.info("Quote cached: {}", quote);
    }

}
