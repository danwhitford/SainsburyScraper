package model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Basic POJO to hold information about totals for the JSON.
 */
class Total {

    @JsonProperty("gross")
    private double gross;
    @JsonProperty("vat")
    private double vat;

    Total(double gross, double vat) {
        this.gross = gross;
        this.vat = vat;
    }

    public double getGross() {
        return gross;
    }

    public double getVat() {
        return vat;
    }
}
