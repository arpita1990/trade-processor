package com.cme.trading.listener;

import com.cme.trading.cache.QuoteCache;
import com.cme.trading.model.Output;
import com.cme.trading.model.Quote;
import com.cme.trading.model.Trade;
import com.cme.trading.service.OutputService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TradeConsumer {

    @Autowired
    private QuoteCache quoteCache;

    @Autowired
    private OutputService outputService;

    @Autowired
    private KafkaTemplate<String, Output> outputKafkaTemplate;

    @KafkaListener(topics = "trade-topic", groupId = "trade-processor", containerFactory = "tradeKafkaListenerContainerFactory")
    public void listenTrades(Trade trade) {

        log.info("Trade received: {}", trade);
        Quote quote = quoteCache.take(trade.getInstrumentID());
        log.info("Quote retrieved from cache: {}", quote);

        Output output = outputService.createOutput(trade, quote);
        log.info("Output created: {}", output);

        outputKafkaTemplate.send("output-topic", output);
        log.info("Output sent to output-topic: {}", output);
        log.info("\n\n**********TradeProcessed**********\n\n");

    }
}
