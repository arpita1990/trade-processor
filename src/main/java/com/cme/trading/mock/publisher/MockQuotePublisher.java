package com.cme.trading.mock.publisher;

import com.cme.trading.model.Quote;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
@ConditionalOnProperty(value = "mock.publisher.enabled", havingValue = "true")
public class MockQuotePublisher {

    @Autowired
    private KafkaTemplate<String, Quote> quoteKafkaTemplate;

    public void publishQuote(String instrumentID, double bidPx, double askPx, int bidVol, int askVol) {
        log.info("Publishing quote for instrumentID: {}, bidPx: {}, askPx: {}, bidVol: {}, askVol: {}", instrumentID, bidPx, askPx, bidVol, askVol);

        Quote quote = new Quote();
        quote.setInstrumentID(instrumentID);
        quote.setBidPx(bidPx);
        quote.setAskPx(askPx);
        quote.setBidVol(bidVol);
        quote.setAskVol(askVol);
        quote.setTimestamp(Instant.now().toEpochMilli());

        quoteKafkaTemplate.send("quote-topic", instrumentID, quote);

        log.info("Quote published: {}", quote);
    }
}

