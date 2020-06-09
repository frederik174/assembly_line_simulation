package com.github.frederik174.simulation;

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

public class MES extends SignalTransmitter {
    private String systemId;

    public MES(String systemId){
        this.systemId = systemId;
    }

    public ObjectEvent createMasterDataEvent(Vehicle vehicle, String zone){
        String eventTime = Instant.now().atZone(ZoneId.of("Europe/Berlin")).toString().substring(0,29);
        String eventTimeZoneOffset = Instant.now().atZone(ZoneId.of("Europe/Berlin")).getOffset().toString();

        ArrayList<epc> epcArrayList = new ArrayList<epc>();
        epcArrayList.add(new epc("urn:jaif:id:obj:" + vehicle.getPin()));

        String action = "ADD";

        ArrayList<vin> ILMD = new ArrayList<vin>();
        ILMD.add(new vin(vehicle.getVin()));

        String bizStep = null;
        String disposition = null;

        ArrayList<id> readPoint = new ArrayList<id>();
        readPoint.add(new id("urn:vwg:vwgbv:v1.0:rp:system:26SUN317559813" + systemId));

        ArrayList<id> bizLocation = new ArrayList<id>();
        //urn:vwg:vwgbv:v1.0:loc:marker:<DI><IAC><CIN><Werk>.<Bau>.<Etage>.<Zone>
        //urn:vwg:vwgbv:v1.0:loc:marker:25LUN31351787012.B2.EG.Z1
        bizLocation.add(new id("urn:vwg:vwgbv:v1.0:loc:marker:25LUN31351787012.B2.EG." + zone));

        return new ObjectEvent(eventTime,eventTimeZoneOffset,epcArrayList,action,ILMD,bizStep,disposition,readPoint,bizLocation);
    }

    public String createPayload(Vehicle vehicle, String zone){
        ObjectEvent event = createMasterDataEvent(vehicle, zone);
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

    public void sendEvent(String topic, Vehicle vehicle, String zone){
        this.transmitter.send(new ProducerRecord<>(topic, systemId, createPayload(vehicle, zone)));
    }
}
