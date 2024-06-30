package com.cme.trading.service;

import com.cme.trading.model.Output;
import com.cme.trading.model.Quote;
import com.cme.trading.model.Trade;

public interface OutputService {

    Output createOutput(Trade trade, Quote quote);
}
