package com.github.frederik174.simulation;

import com.github.frederik174.parameter.ReferencePointNames;
import com.github.frederik174.utilities.ObjectEvent;
import com.github.frederik174.utilities.vin;
import com.github.frederik174.utilities.epc;
import com.github.frederik174.utilities.id;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;

public class Gate extends SignalTransmitter {
    public String gateId;
    public Cycle cycle;

    // constructor for a gate on an assembly line (located within a cycle)
    public Gate(String gateId, Cycle cycle){
        super();
        this.gateId = gateId;
        this.cycle = cycle;
    }


    public ObjectEvent createObjectEvent(){
        String eventTime = Instant.now().atZone(ZoneId.of("Europe/Berlin")).toString().substring(0,29);
        String eventTimeZoneOffset = Instant.now().atZone(ZoneId.of("Europe/Berlin")).getOffset().toString();

        ArrayList<epc> epcArrayList = new ArrayList<epc>();
        epcArrayList.add(new epc("urn:jaif:id:obj:" + cycle.vehicle.getPin()));

        String action = "OBSERVE";
        ArrayList<vin> ILMD = new ArrayList<vin>();
        ILMD.add(new vin());

        String bizStep = "urn:epcglobal:cbv:bizstep:arriving";
        String disposition = "in_progress";

        ArrayList<id> readPoint = new ArrayList<id>();
        readPoint.add(new id("urn:vwg:vwgbv:v1.0:rp:gate:26SUN317559813" + gateId));

        ArrayList<id> bizLocation = new ArrayList<id>();
        bizLocation.add(new id("urn:vwg:vwgbv:v1.0:loc:refpko:25LUN313517870" +
                ReferencePointNames.REFERENCE_POINT_H2.getIdString() + "." +
                String.valueOf(cycle.CENTER_X).replace('.',',') + "." +
                String.valueOf(cycle.CENTER_Y).replace('.',',') + ".0"));

        return new ObjectEvent(eventTime,eventTimeZoneOffset,epcArrayList,action,ILMD,bizStep,disposition,readPoint,bizLocation);
    }

    public String createPayload(){
        ObjectEvent event = createObjectEvent();
        StringWriter sw = new StringWriter();
        String eventXmlString = null;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ObjectEvent.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(event,sw);

            eventXmlString = sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return eventXmlString;
    }

    public void sendEvent(String topic){
        this.transmitter.send(new ProducerRecord<>(topic, gateId, createPayload()));
    }
}