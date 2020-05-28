package com.github.frederik174.simulation;


import com.github.frederik174.paramter.LayoutParameter;
import com.github.frederik174.paramter.SimulationParameter;
import org.relaxng.datatype.Datatype;

public class Simulation {

    public static void main(String[] args) {

        LayoutParameter params = new LayoutParameter();

        AssemblyLine BA1_1 = new AssemblyLine("BA1.1",params.BA1_1NAMES,params.BA1_1CENTERS);
        BA1_1.initVehicles(1);

        InlineGate g1 = new InlineGate("RFIDGATE0001", BA1_1.cycles[0]);
        //g1.transmitter.send(g1.createObjectEventRecord("EPCIS-EVENTS",1));
        System.out.println(g1.createObjectEventRecord("test",1));

    }

}
