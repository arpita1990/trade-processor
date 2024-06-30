package com.cme.trading.mock.publisher;

import com.cme.trading.model.Trade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
@ConditionalOnProperty(value = "mock.publisher.enabled", havingValue = "true")
public class MockTradePublisher {

    private final KafkaTemplate<String, Trade> kafkaTemplate;

    @Autowired
    public MockTradePublisher(KafkaTemplate<String, Trade> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishTrade(String instrumentID, String customerID, boolean side, boolean aggressorIndicator, double tradePx, int tradeVol) {

        log.info("Publishing trade for instrumentID: {}, customerID: {}, side: {}, aggressorIndicator: {}, tradePx: {}, tradeVol: {}", instrumentID, customerID, side, aggressorIndicator, tradePx, tradeVol);
        Trade trade = new Trade();
        trade.setInstrumentID(instrumentID);
        trade.setCustomerID(customerID);
        trade.setSide(side);
        trade.setAggressorIndicator(aggressorIndicator);
        trade.setTradePx(tradePx);
        trade.setTradeVol(tradeVol);
        trade.setTimestamp(Instant.now().toEpochMilli());

        kafkaTemplate.send("trade-topic", instrumentID, trade);
        log.info("Trade published: {}", trade);
    }
}

