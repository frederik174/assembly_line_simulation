package com.github.frederik174.vls.line_simulation;

public class AssemblyLineSimulation {
    public static void main(String[] args) {
        // Simulation parameters
        String bootstrapServer = "localhost:9092"; //3.122.224.56
        String topic = "information-objects";
        String hallID = "halle1A";

        String[] processID = {"montage","montage","montage","montage","montage","montage","montage","montage","montage","montage","montage","montage","montage","montage","montage","montage","montage","montage",
                "montage","montage","montage","montage","montage","montage","montage","montage","montage","montage","montage","montage","montage","übergabe finish center"};

        Integer[] workSteps = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32};

        Double[][] workStationPositions = {
                { 0.00, 6.00,12.00,18.00,24.00,30.00,36.00,42.00,48.00,50.00,50.00,48.00,42.00,36.00,30.00,24.00,18.00,12.00, 6.00, 4.00, 4.00, 6.00,12.00,18.00,24.00,30.00,36.00,42.00,48.00,54.00,60.00},
                { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 4.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00,12.00,16.00,16.00,16.00,16.00,16.00,16.00,16.00,16.00,16.00,16.00,16.00}
        };
        Double[] referencePoint = {52.432365506153175, 10.768133103847504};

        Integer productionNumber = 0;
        Integer cycleTime = 20000; // in milliseconds

        // Init Simulation
        AssemblyLine assemblyLine = new AssemblyLine(referencePoint,hallID,processID,workSteps,workStationPositions,productionNumber);

        Reader reader01 = new Reader(bootstrapServer,"reader01");
        Reader reader02 = new Reader(bootstrapServer,"reader02");
        Reader reader03 = new Reader(bootstrapServer,"reader03");

        // Start the simulation
        Integer productionBatch = 0;

        while(productionBatch < 3){
            System.out.println("Production batch:" + productionBatch);

            // cycle is represents the "takt" the reader is located in
            reader01.producer.send(reader01.identificationObject(assemblyLine,0,topic, reader01.getReaderID(),0));
            reader02.producer.send(reader02.identificationObject(assemblyLine,10,topic, reader02.getReaderID(),1));
            reader02.producer.send(reader03.identificationObject(assemblyLine,30,topic, reader03.getReaderID(),2));

            // Shift push vehicles on assembly line forward
            assemblyLine.pushVehiclesOnLine(new Vehicle(assemblyLine.getProductionNumber(),assemblyLine.plantID,assemblyLine.assemblySegment));
            System.out.println("Line moved, produced vehicle: " + assemblyLine.vehiclesOnLine[0].getPin());

            productionBatch += 1;

            try{
                Thread.sleep(cycleTime);
            }catch (InterruptedException e){
                break;
            }
        }
        reader01.producer.close();
        reader02.producer.close();
        reader03.producer.close();
    }
}
