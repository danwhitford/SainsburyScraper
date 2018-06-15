package model;

import com.fasterxml.jackson.annotation.JsonProperty;

class Total {

    public double getGross() {
        return gross;
    }

    public double getVat() {
        return vat;
    }

    @JsonProperty("gross")
    private double gross;

    @JsonProperty("vat")
    private double vat;

    Total(double gross, double vat) {
        this.gross = gross;
        this.vat = vat;
    }
}
