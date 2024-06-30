package com.cme.trading.service.impl;

import com.cme.trading.model.Output;
import com.cme.trading.model.Quote;
import com.cme.trading.model.Trade;
import com.cme.trading.service.OutputService;
import org.springframework.stereotype.Service;

@Service
public class OutputServiceImpl implements OutputService {

    @Override
    public Output createOutput(Trade trade, Quote quote) {
        double midPx = (quote.getBidPx() + quote.getAskPx()) / 2.0;
        Output output = new Output(trade, midPx);
        return output;
    }
}

