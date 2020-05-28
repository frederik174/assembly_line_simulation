package com.github.frederik174.paramter;

public enum SimulationParameter {
    /* referencePointLat: latitude of the reference point [°]
           referencePointLng: longitude of the reference point [°]
           angle := angle between latitudes and plant walls [°]
           Wolfsburg: 8.587°
           Hannover: 1,235° */

    REFERENCE_POINT_LAT(52.41929723315495),
    REFERENCE_POINT_LNG(9.669894576072693),
    CYCLE_DURATION(60000.00), // in ms
    ANGLE(1.235);

    double value;

    SimulationParameter(double value){
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
