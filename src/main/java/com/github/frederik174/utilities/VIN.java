package com.github.frederik174.utilities;

public class VIN {
    private String VIN;

    // default constructor
    public VIN(){}

    // custom constructor
    public VIN(String VIN){
        this.VIN = VIN;
    }

    public String getVIN() {
        return VIN;
    }
    public void setVIN(String VIN) {
        this.VIN = VIN;
    }
}
