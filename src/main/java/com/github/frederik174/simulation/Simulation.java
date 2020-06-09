package com.github.frederik174.simulation;


import com.github.frederik174.parameter.LayoutParameter;
import com.github.frederik174.parameter.SimulationParameter;



public class Simulation {

    public static void main(String[] args) throws InterruptedException {
        // parameters
        String eventTopic = "OBJECT-EVENTS";
        String masterDataTopic = "MASTER-DATA-EVENTS";
        LayoutParameter params = new LayoutParameter();

        // simulation setup
        MES mes = new MES("MES0001");

        AssemblyLine BA1_1 = new AssemblyLine("BA1.1",params.BA1_1NAMES,params.BA1_1CENTERS);
        BA1_1.initVehicles(1);
        Gate gba11_1 = new Gate("RFIDGATE0001", BA1_1.cycles[0]); // Entry
        Gate gba11_2 = new Gate("RFIDGATE0002", BA1_1.cycles[3]);
        Gate gba11_3 = new Gate("RFIDGATE0003", BA1_1.cycles[6]);
        Gate gba11_4 = new Gate("RFIDGATE0004", BA1_1.cycles[9]);
        Gate gba11_5 = new Gate("RFIDGATE0005", BA1_1.cycles[12]);
        Gate gba11_6 = new Gate("RFIDGATE0006", BA1_1.cycles[15]);
        Gate gba11_7 = new Gate("RFIDGATE0007", BA1_1.cycles[18]);
        Gate gba11_8 = new Gate("RFIDGATE0008", BA1_1.cycles[21]);
        Gate gba11_9 = new Gate("RFIDGATE0009", BA1_1.cycles[24]); // Exit

        AssemblyLine OVB1 = new AssemblyLine("OVB1",params.OVB1_NAMES,params.OVB1_CENTERS);
        OVB1.initVehicles(BA1_1.productionCount);

        AssemblyLine BA2_1 = new AssemblyLine("BA2.1",params.BA2_1NAMES,params.BA2_1CENTERS);
        BA2_1.initVehicles(OVB1.productionCount);
        Gate gba21_1 = new Gate("RFIDGATE0010", BA2_1.cycles[0]); // Entry
        Gate gba21_2 = new Gate("RFIDGATE0011", BA2_1.cycles[3]);
        Gate gba21_3 = new Gate("RFIDGATE0012", BA2_1.cycles[6]);
        Gate gba21_4 = new Gate("RFIDGATE0013", BA2_1.cycles[9]);
        Gate gba21_5 = new Gate("RFIDGATE0014", BA2_1.cycles[12]);
        Gate gba21_6 = new Gate("RFIDGATE0015", BA2_1.cycles[15]);
        Gate gba21_7 = new Gate("RFIDGATE0016", BA2_1.cycles[18]);
        Gate gba21_8 = new Gate("RFIDGATE0017", BA2_1.cycles[21]); // Exit

        // simulation
        int n = 0;
        int maxNoOfCycles = 50;
        while (n < maxNoOfCycles){
            mes.sendEvent(masterDataTopic, BA1_1.cycles[0].vehicle,"BA1");
            gba11_1.sendEvent(eventTopic);
            gba11_2.sendEvent(eventTopic);
            gba11_3.sendEvent(eventTopic);
            gba11_4.sendEvent(eventTopic);
            gba11_5.sendEvent(eventTopic);
            gba11_6.sendEvent(eventTopic);
            gba11_7.sendEvent(eventTopic);
            gba11_8.sendEvent(eventTopic);
            gba11_9.sendEvent(eventTopic);

            mes.sendEvent(masterDataTopic, BA2_1.cycles[0].vehicle,"BA2");
            gba21_1.sendEvent(eventTopic);
            gba21_2.sendEvent(eventTopic);
            gba21_3.sendEvent(eventTopic);
            gba21_4.sendEvent(eventTopic);
            gba21_5.sendEvent(eventTopic);
            gba21_6.sendEvent(eventTopic);
            gba21_7.sendEvent(eventTopic);
            gba21_8.sendEvent(eventTopic);

            Thread.sleep((long)SimulationParameter.CYCLE_DURATION.getValue());

            Vehicle ba1_1OutgoingVehicle =  BA1_1.shiftVehiclesForward(new Vehicle(BA2_1.productionCount));
            Vehicle ovb1OutgoingVehicle =  OVB1.shiftVehiclesForward(ba1_1OutgoingVehicle);
            Vehicle ba2_1OutgoingVehicle =  BA2_1.shiftVehiclesForward(ovb1OutgoingVehicle);
            n++;
            System.out.println("completed production batch: " + n);
        }

        gba11_1.transmitter.close();
        gba11_2.transmitter.close();
        gba11_3.transmitter.close();
        gba11_4.transmitter.close();
        gba11_5.transmitter.close();
        gba11_6.transmitter.close();
        gba11_7.transmitter.close();
        gba11_8.transmitter.close();
        gba11_9.transmitter.close();

        gba21_1.transmitter.close();
        gba21_2.transmitter.close();
        gba21_3.transmitter.close();
        gba21_4.transmitter.close();
        gba21_5.transmitter.close();
        gba21_6.transmitter.close();
        gba21_7.transmitter.close();
        gba21_8.transmitter.close();

        mes.transmitter.close();
    }
}
