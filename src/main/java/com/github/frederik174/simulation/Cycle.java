package com.github.frederik174.simulation;

public class Cycle {
    // cell description
    public String CYCLE_NAME;

    // relative coordinates for cycle center
    public final double CENTER_X;
    public final double CENTER_Y;

    // each cycle is assumed to contain a vehicle and optional signal transmitters
    public Vehicle vehicle;

    public Cycle(String name, double centerX,double centerY){
        this.CYCLE_NAME = name;
        this.CENTER_X = centerX;
        this.CENTER_Y= centerY;
    }
}