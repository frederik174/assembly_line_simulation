package com.github.frederik174.simulation;

import com.github.frederik174.parameter.SimulationParameter;
import com.github.frederik174.properties.KafkaProperties;
import org.apache.kafka.clients.producer.KafkaProducer;


public class SignalTransmitter {
    private KafkaProperties config;
    public KafkaProducer<String,String> transmitter;

    // Default constructor
    public SignalTransmitter(){
        this.config = new KafkaProperties();
        this.transmitter = new KafkaProducer<>(config);
    }

    // Constructor for externally hosted kafka server (AWS or Docker Network)
    public SignalTransmitter(String bootstrapServer){
        this.config = new KafkaProperties(bootstrapServer);
        this.transmitter = new KafkaProducer<>(config);
    }

    public double[] localToGeoCoordinates(double xPos,double yPos){

        double[] projectedCoordinates = {SimulationParameter.REFERENCE_POINT_LAT.getValue(),
                SimulationParameter.REFERENCE_POINT_LNG.getValue()};

        if(xPos != 0.0 || yPos != 0.0){
            double dx = xPos / 1000; //[km]
            double dy = -yPos / 1000; //[km]

            // 1 - Calculate bearing angle
            double alpha = 90 - SimulationParameter.ANGLE.getValue();

            // direct distance between zero-point (reference point in local coordinate system) and target point
            double d = Math.sqrt(dx * dx + dy * dy);
            double phi = Math.toDegrees(Math.atan(dy/dx));

            /* distance d transformed to the unit sphere
               [°] where 1/111.2 is an approximation factor for division by the earth radius transformed from radian to degree */
            d = d / 111.2;
            alpha = alpha + phi;

            // 2 - Calculate the projected latitude coordinate lat2
            // lat2 = arcsin(sin(lat1)cos(d) + cos(lat1)sin(d)cos(alpha))
            double arg = Math.sin(Math.toRadians(SimulationParameter.REFERENCE_POINT_LAT.getValue())) *
                    Math.cos(Math.toRadians(d)) + Math.cos(Math.toRadians(SimulationParameter.REFERENCE_POINT_LAT.getValue())) *
                    Math.sin(Math.toRadians(d)) * Math.cos(Math.toRadians(alpha));

            double lat2 = Math.asin(arg);
            lat2 = Math.toDegrees(lat2);

            // 3 - Calculate the projected longitude coordinate lng2
            // lng2 = lat1 + arcsin((sin(d)/cos(lat2))*sin(alpha)
            double sub = Math.toDegrees(Math.asin((Math.sin(Math.toRadians(d))/Math.cos(Math.toRadians(lat2)))*Math.sin(Math.toRadians(alpha))));
            double lng2 = SimulationParameter.REFERENCE_POINT_LNG.getValue() + sub;

            projectedCoordinates = new double[]{lat2, lng2};
        }
        return projectedCoordinates;
    }
}