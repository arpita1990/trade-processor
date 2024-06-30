package com.cme.trading.model;

import lombok.Data;

@Data
public class Quote {

    private String instrumentID;
    private double bidPx;
    private double askPx;
    private int bidVol;
    private int askVol;
    private long timestamp;

}
