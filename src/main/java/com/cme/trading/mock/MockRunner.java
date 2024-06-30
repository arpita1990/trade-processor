package com.cme.trading.mock;

import com.cme.trading.mock.publisher.MockQuotePublisher;
import com.cme.trading.mock.publisher.MockTradePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootConfiguration
@ComponentScan(basePackages = {"com.cme.trading.mock"})
@Slf4j
@ConditionalOnProperty(value = "mock.publisher.enabled", havingValue = "true")

public class MockRunner implements CommandLineRunner {

    @Autowired
    private MockQuotePublisher mockQuotePublisher;

    @Autowired
    private MockTradePublisher mockTradePublisher;


    public static void main(String[] args) {
        SpringApplication.run(MockRunner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("Starting mock data publisher");
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

        // Schedule to run every few seconds
        executorService.scheduleAtFixedRate(() -> {
            try {
                mockQuotePublisher.publishQuote("INSTR1", Math.random() * 100, Math.random() * 100, (int) (Math.random() * 10), (int) (Math.random() * 10));
                mockQuotePublisher.publishQuote("INSTR2", Math.random() * 200, Math.random() * 200, (int) (Math.random() * 20), (int) (Math.random() * 20));
            } catch (Exception e) {
                log.error("Error in scheduled task", e);
            }
        }, 0, 20, TimeUnit.SECONDS);

        executorService.scheduleAtFixedRate(() -> {
            mockTradePublisher.publishTrade("INSTR1", "CUST1", Math.random() > 0.5, Math.random() > 0.5, Math.random() * 100, (int) (Math.random() * 5));
            mockTradePublisher.publishTrade("INSTR2", "CUST2", Math.random() > 0.5, Math.random() > 0.5, Math.random() * 200, (int) (Math.random() * 10));
        }, 0, 20, TimeUnit.SECONDS);
        log.info("Mock data publishers started");
    }
}