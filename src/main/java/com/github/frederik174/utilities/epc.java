package com.github.frederik174.utilities;

public class epc{
    private String epcString;

    // Default constructor
    public epc(){}

    // Custom constructor
    public epc(String epcString){
        this.epcString = epcString;
    }

    public String getEpc() {
        return epcString;
    }
    public void setEpc(String epc) {
        this.epcString = epc;
    }
}