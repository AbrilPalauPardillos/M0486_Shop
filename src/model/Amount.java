package model;

import java.text.DecimalFormat;

public class Amount {
    private double value;    
    private String currency = "€";
    
    private static final DecimalFormat df = new DecimalFormat("0.00");
    
    public Amount(double value) {
        this.value = value;
    }

    public Amount(double value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return df.format(value) + currency;
    }
}