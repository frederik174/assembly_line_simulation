package com.github.frederik174.vls.line_simulation;

public class AssemblyLineSimulation {
    public static void main(String[] args) {
        // Simulation parameters
        String bootstrapServer = "localhost:9092"; //3.122.224.56
        String topic = "information-objects";
        String hallID = "halle1A";

        String[] processID = {"p01","p01","p01","p01","p01","p01","p01","p01","p01","p01","p01","p01","p01","p01","p01","p01","p01","p01",
                "p01","p01","p01","p01","p01","p01","p01","p01","p01","p01","p01","p01","p01","p01"};

        String[] workSteps = {"Takt01","Takt02","Takt03","Takt04","Takt05","Takt06","Takt07","Takt08","Takt09","Takt10", "Takt11","Takt12",
                "Takt13","Takt14","Takt15","Takt16","Takt17","Takt18","Takt19","Takt20","Takt21","Takt22","Takt23","Takt24","Takt25","Takt26",
                "Takt27","Takt28","Takt29","Takt30","Takt31","Takt32"};

        Double[][] workStationPositions = {
                { 0.00, 6.00,12.00,18.00,24.00,30.00,36.00,42.00,48.00,50.00,50.00,48.00,42.00,36.00,30.00,24.00,18.00,12.00, 6.00, 4.00, 4.00, 6.00,12.00,18.00,24.00,30.00,36.00,42.00,48.00,54.00,60.00},
                { 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 4.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00,12.00,16.00,16.00,16.00,16.00,16.00,16.00,16.00,16.00,16.00,16.00,16.00}
        };
        Double[] referencePoint = {52.43019045537872, 10.768661499023438};

        Integer productionNumber = 0;
        Integer cycleTime = 6000; // in milliseconds

        // Init Simulation
        AssemblyLine assemblyLine = new AssemblyLine(referencePoint,hallID,processID,workSteps,workStationPositions,productionNumber);

        Reader reader01 = new Reader(bootstrapServer,"reader01");
        //Reader reader02 = new Reader(bootstrapServer,"reader02");

        // Start the simulation
        Integer productionBatch = 0;

        while(productionBatch < 3){
            System.out.println("Production batch:" + productionBatch);

            // cycle is represents the "takt" the reader is located in
            // System.out.println(reader02.identificationObject(assemblyLine,30,topic, reader02.getReaderID(),1));
            reader01.producer.send(reader01.identificationObject(assemblyLine,0,topic, reader01.getReaderID(),0));

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
    }
}
