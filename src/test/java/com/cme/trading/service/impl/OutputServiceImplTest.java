package com.cme.trading.service.impl;

import com.cme.trading.model.Output;
import com.cme.trading.model.Quote;
import com.cme.trading.model.Trade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OutputServiceImplTest {

    private OutputServiceImpl outputService;

    @BeforeEach
    public void setUp() {
        outputService = new OutputServiceImpl();
    }

    @Test
    public void createOutputShouldCalculateMidPxAndReturnOutput() {
        Trade trade = new Trade();
        Quote quote = new Quote();
        quote.setBidPx(10.0);
        quote.setAskPx(20.0);

        Output output = outputService.createOutput(trade, quote);

        assertEquals(15.0, output.getMidPx());
        assertEquals(trade, output.getTrade());
    }

    @Test
    public void createOutputShouldHandleZeroBidAndAskPx() {
        Trade trade = new Trade();
        Quote quote = new Quote();
        quote.setBidPx(0.0);
        quote.setAskPx(0.0);

        Output output = outputService.createOutput(trade, quote);

        assertEquals(0.0, output.getMidPx());
        assertEquals(trade, output.getTrade());
    }

    @Test
    public void createOutputShouldHandleNegativeBidAndAskPx() {
        Trade trade = new Trade();
        Quote quote = new Quote();
        quote.setBidPx(-10.0);
        quote.setAskPx(-20.0);

        Output output = outputService.createOutput(trade, quote);

        assertEquals(-15.0, output.getMidPx());
        assertEquals(trade, output.getTrade());
    }
}