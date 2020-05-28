package com.github.frederik174.simulation;


import com.github.frederik174.parameter.LayoutParameter;
import com.github.frederik174.parameter.SimulationParameter;

public class Simulation {

    public static void main(String[] args) {

        LayoutParameter params = new LayoutParameter();

        AssemblyLine BA1_1 = new AssemblyLine("BA1.1",params.BA1_1NAMES,params.BA1_1CENTERS);
        BA1_1.initVehicles(1);

        InlineGate g1 = new InlineGate("RFIDGATE0001", BA1_1.cycles[0]);
        InlineGate g2 = new InlineGate("RFIDGATE0001", BA1_1.cycles[22]);

        int i = 0;
        while (i < 3){
            try {
                //g1.transmitter.send(g1.createObjectEventRecord("EPCIS-EVENTS",1));
                System.out.println(g1.createObjectEventRecord("test",1));


                //g2.transmitter.send(g2.createObjectEventRecord("EPCIS-EVENTS",1));
                System.out.println(g2.createObjectEventRecord("test",1));

                BA1_1.shiftVehiclesForward(new Vehicle(BA1_1.productionCount));
                i++;

                Thread.sleep((int)SimulationParameter.CYCLE_DURATION.getValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        g1.transmitter.close();
        g2.transmitter.close();
    }
}
