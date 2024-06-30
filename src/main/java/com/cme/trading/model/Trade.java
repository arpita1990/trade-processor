package com.cme.trading.model;

import lombok.Data;

@Data
public class Trade {

    private String instrumentID;
    private String customerID;
    private boolean side;
    private boolean aggressorIndicator;
    private double tradePx;
    private int tradeVol;
    private long timestamp;

}