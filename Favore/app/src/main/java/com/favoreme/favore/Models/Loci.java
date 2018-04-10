package com.favoreme.favore.Models;

/**
 * This class takes in the latitude and longitude and converts them in terms of meters.
 */

public class Loci{
    long lati;
    long longi;
    public Loci(double longitude, double latitude) {
        this.longi = (long)((long) longitude*(10000/90)*1e3);
        this.lati = (long)((long) latitude*(10000/90)*1e3);
    }
    public double getLati() {
        return lati;
    }
    public void setLati(double latitude) {
        this.lati = (long)((long) latitude*(10000/90)*1e3);
    }
    public double getLongi() {
        return longi;
    }
    public void setLongi(double longitude) {
        this.longi = (long)((long) longitude*(10000/90)*1e3);
    }
}
