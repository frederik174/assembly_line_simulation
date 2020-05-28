package com.github.frederik174.simulation;


public class AssemblyLine {
   public final String NAME;

   public Cycle[] cycles;
   public int numberOfCycles;
   public int productionCount;

   public AssemblyLine(String name, String[] cycleNames, double[][] cycleCenters){
       this.NAME = name;
       this.numberOfCycles = cycleNames.length;

       // initialize cycles
       this.cycles = new Cycle[numberOfCycles];
       for(int n = 0; n < numberOfCycles; n++){
           cycles[n] = new Cycle(cycleNames[n],cycleCenters[0][n],cycleCenters[1][n]);
       }
       this.productionCount = 0;
   }

    public void initVehicles(int noOfFirstVeh){
       // Initializes vehicles on the assembly line
       for(int n = 1;n <= numberOfCycles; n++){
           cycles[numberOfCycles - n].vehicle = new Vehicle(noOfFirstVeh);
           noOfFirstVeh++;
       }
       productionCount = noOfFirstVeh-1;
   }

    public Vehicle shiftVehiclesForward(Vehicle newVehicle){
       // save leaving vehicle as return value
       Vehicle leavingVehicle = cycles[numberOfCycles-1].vehicle;

       // Shifts the vehicles on the assembly line one cycle forward
       for(int n = 1;n < numberOfCycles; n++){
           cycles[numberOfCycles-n].vehicle = cycles[numberOfCycles-n-1].vehicle;
       }
       // Put the new vehicle on the beginning of the line
       cycles[0].vehicle = newVehicle;
       productionCount++;

       return leavingVehicle;
   }
}
