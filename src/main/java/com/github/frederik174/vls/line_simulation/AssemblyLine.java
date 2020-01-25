package com.github.frederik174.vls.line_simulation;

public class AssemblyLine {
    // zero-reference point [lat,lng] null point for internal position calculation based on
    public Double[] referencePoint;
    public String plantID = "11";
    public String assemblySegment = "3"; // "Montage-Segment"
    public String hallID;

    // ID for every sub-segment of the line
    public String[] processID;
    public Integer[] workSteps; // Takte
    public Double[][] workStationPositions;

    public Vehicle[] vehiclesOnLine;
    private Integer productionNumber;

    AssemblyLine(Double[] referencePoint, String hallID, String[] processID, Integer[] workSteps, Double[][] workStationPositions, Integer productionNumber){
        this.referencePoint = referencePoint;
        this.hallID = hallID;
        this.processID = processID;
        this.workSteps = workSteps;
        this.workStationPositions = workStationPositions;
        this.productionNumber = productionNumber;

        /* Initialize vehiclesOnLine (= Taktbelegung) with a set of recently produced vehicles. */
        this.vehiclesOnLine = new Vehicle[this.workStationPositions[0].length];
        for(int idx = 0; idx < vehiclesOnLine.length; idx++){
            incrementProductionNumber();

            int idx2 = vehiclesOnLine.length - 1 - idx;
            this.vehiclesOnLine[idx2] = new Vehicle(this.productionNumber, plantID, this.assemblySegment);
        }
        // increment production number for next vehicle to be produced
        incrementProductionNumber();
    }

    public Vehicle[] pushVehiclesOnLine(Vehicle vehicle){
        /* Initialize a new vehicle on the beginning of the line and push the other vehicles one position ahead.*/
        for(int idx = vehiclesOnLine.length - 1; idx > 0; idx--){
            vehiclesOnLine[idx] = vehiclesOnLine[idx - 1];
        }
        // Put a new vehicle at the beginning of the line
        vehiclesOnLine[0] = vehicle;
        // increment ongoing production counter
        incrementProductionNumber();
        return vehiclesOnLine;
    }

    public Integer getProductionNumber(){
        return this.productionNumber;
    }

    public void incrementProductionNumber(){
        this.productionNumber += 1;
    }
}