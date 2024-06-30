package com.cme.trading.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class Output {

    private Trade trade;
    private double midPx;

    public Output(Trade trade, double midPx) {
        this.trade = trade;
        this.midPx = midPx;
    }
}
