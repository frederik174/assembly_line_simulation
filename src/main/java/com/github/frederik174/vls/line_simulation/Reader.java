package com.github.frederik174.vls.line_simulation;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Properties;

public class Reader {
    private String readerID;

    // Kafka Producer
    public KafkaProducer<String,String> producer;
    private Properties config;

    Reader(String bootstrapServer, String readerID){
        this.readerID = readerID;
        this.config = new Properties();

        // KafkaProducer Configurations
        config.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        config.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        config.setProperty(ProducerConfig.RETRIES_CONFIG, "3");
        config.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1"); // send asap
        // Exactly once semantics
        config.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,"true");

        this.producer = new KafkaProducer<String, String>(config);
    }
    private static Double[] calculateGeoCoordinates(Double xPos,Double yPos,Double[] referencePoint){
        // 1- Rotate coordinate system to compensate the angle between plant walls and latitudes
            double phi = 8.587; // in °
            double compensatedXDist;
            double compensatedYDist;
            if(xPos != 0.0 && yPos != 0.0) {
                double d = Math.sqrt(xPos * xPos + yPos * yPos);
                double gamma = Math.asin(xPos / d);
                compensatedXDist = d * Math.sin(phi + gamma);
                compensatedYDist = d * Math.cos(phi + gamma);
            }else{
                compensatedXDist = 0.0;
                compensatedYDist = 0.0;
            }
        // 2- Transform relative distance to Geo-Coordinates
            double d0 = 111.3; // distance between latitudes and longitudes around the Equator
            // calculate latitude
            double lat2 = referencePoint[0] - (compensatedXDist/d0);
            // calculate longitude
            double lat = (referencePoint[0] - lat2) * Math.PI / 360;
            double lng2 = referencePoint[1] - (compensatedYDist/(d0 * Math.cos(lat)));

        Double[] coords = {lat2,lng2};
        return coords;
    }

    public static ProducerRecord<String,String> identificationObject(AssemblyLine assemblyLine, Integer cycle, String topic, String readerID, Integer partition){
        // Object node for vehicle
        ObjectNode obj = JsonNodeFactory.instance.objectNode();
        obj.put("pin", assemblyLine.vehiclesOnLine[cycle].getPin());
        obj.put("timestamp", Instant.now().atZone(ZoneId.of("Europe/Berlin")).toString());

        ObjectNode process = JsonNodeFactory.instance.objectNode();
        process.put("processID",assemblyLine.processID[cycle]); // ProcessID des Taktes
        process.put("workstep", assemblyLine.workSteps[cycle]); // Beschreibung des Arbeitsschrittes in diesem Takt

        // EPCIS Business Location
        ObjectNode objLocation = JsonNodeFactory.instance.objectNode();
        objLocation.put("hallID", assemblyLine.hallID);
        objLocation.put("segment", assemblyLine.assemblySegment);

        ArrayNode xyposition = JsonNodeFactory.instance.arrayNode();
        xyposition.add(assemblyLine.workStationPositions[0][cycle]);
        xyposition.add(assemblyLine.workStationPositions[1][cycle]);

        objLocation.set("xy-position",xyposition);

        obj.set("process",process);
        obj.set("location", objLocation);

        // Object node for reader
        ObjectNode reader = JsonNodeFactory.instance.objectNode();
        reader.put( "readerID", readerID);

        ObjectNode readerLocation = JsonNodeFactory.instance.objectNode();
        readerLocation.put("hallID", assemblyLine.hallID);
        readerLocation.put("segment", assemblyLine.assemblySegment);

        ArrayNode readerCoordinates = JsonNodeFactory.instance.arrayNode();
        Double[] coords = calculateGeoCoordinates(assemblyLine.workStationPositions[0][cycle],assemblyLine.workStationPositions[1][cycle],assemblyLine.referencePoint);
        readerCoordinates.add(coords[0]);
        readerCoordinates.add(coords[1]);

        readerLocation.set("coordinates",readerCoordinates);
        reader.set("location",readerLocation);
/*
        reader.put("model", "standard-reader");
        reader.put("software-version", "version-1.0");
        reader.put("serial-number", "s0x123456789");
        reader.put("mac-address", "00:3x:c1:c4:5d:df");
        reader.put("hostname", "standard-host");
        reader.put("ip-address", "134.169.251.135");
*/
        // Information object
        ObjectNode informationObj = JsonNodeFactory.instance.objectNode();
        informationObj.set("object",obj);
        informationObj.set("reader",reader);

        return new ProducerRecord<>(topic,partition,readerID,informationObj.toString());
    }

    public String getReaderID(){
        return readerID;
    }
}
