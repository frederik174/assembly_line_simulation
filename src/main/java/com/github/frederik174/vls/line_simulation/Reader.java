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
    private static Double[] wayPointProjection(Double xPos,Double yPos,Double[] referencePoint){
        double dx = xPos / 1000; //[km]
        double dy = yPos / 1000; //[km]

        double lat1 = referencePoint[0]; // latitude of the reference point [°]
        double lng1 = referencePoint[1]; // longitude of the reference point [°]

        // 1 - Calculate bearing angle
        double alpha = 90 - 8.587; // angle between latitudes and plant walls in Wolfsburg [°]

        // direct distance between zero-point (reference point in local coordinate system) and target point
        double d = Math.sqrt(dx * dx + dy * dy);
        double phi = Math.toDegrees(Math.atan(dy/dx));

        // distance d transformed to the unit sphere
        d = d / 111.2; //[°] where 1/111.2 is an approximation factor for division by the earth radius transformed from radian to degree
        alpha = alpha + phi;

        // 2 - Calculate the projected latitude coordinate lat2
        // lat2 = arcsin(sin(lat1)cos(d) + cos(lat1)sin(d)cos(alpha))
        double arg = Math.sin(Math.toRadians(lat1))*Math.cos(Math.toRadians(d)) + Math.cos(Math.toRadians(lat1))*Math.sin(Math.toRadians(d))*Math.cos(Math.toRadians(alpha));

        double lat2 = Math.asin(arg);
        lat2 = Math.toDegrees(lat2);

        // 3 - Calculate the projected longitude coordinate lng2
        // lng2 = lat1 + arcsin((sin(d)/cos(lat2))*sin(alpha)
        double sub = Math.toDegrees(Math.asin((Math.sin(Math.toRadians(d))/Math.cos(Math.toRadians(lat2)))*Math.sin(Math.toRadians(alpha))));
        double lng2 = lng1 + sub;

        Double[] projectedCoords = {lat2,lng2};
        return projectedCoords;
    }

    public static ProducerRecord<String,String> identificationObject(AssemblyLine assemblyLine, Integer cycle, String topic, String readerID, Integer partition){
        // Object node for vehicle
        ObjectNode obj = JsonNodeFactory.instance.objectNode();
        obj.put("pin", assemblyLine.vehiclesOnLine[cycle].getPin());
        obj.put("timestamp", Instant.now().atZone(ZoneId.of("Europe/Berlin")).toString());

        ObjectNode process = JsonNodeFactory.instance.objectNode();
        process.put("processID",assemblyLine.processID[cycle]); // Beschreibung des Taktes
        process.put("workstep", assemblyLine.workSteps[cycle]); // Takt

        // EPCIS Business Location
        ObjectNode objLocation = JsonNodeFactory.instance.objectNode();
        objLocation.put("hallID", assemblyLine.hallID);
        objLocation.put("segment", assemblyLine.assemblySegment);

        ArrayNode objCoordinates = JsonNodeFactory.instance.arrayNode();
        Double[] objCoords = wayPointProjection(assemblyLine.workStationPositions[0][cycle],assemblyLine.workStationPositions[1][cycle],assemblyLine.referencePoint);
        objCoordinates.add(objCoords[0]);
        objCoordinates.add(objCoords[1]);

        objLocation.set("coordinates",objCoordinates);

        obj.set("process",process);
        obj.set("location", objLocation);

        // Object node for reader
        ObjectNode reader = JsonNodeFactory.instance.objectNode();
        reader.put( "readerID", readerID);

        ObjectNode readerLocation = JsonNodeFactory.instance.objectNode();
        readerLocation.put("hallID", assemblyLine.hallID);
        readerLocation.put("segment", assemblyLine.assemblySegment);

        ArrayNode readerCoordinates = JsonNodeFactory.instance.arrayNode();
        Double[] readerCoords = wayPointProjection(assemblyLine.workStationPositions[0][cycle],assemblyLine.workStationPositions[1][cycle],assemblyLine.referencePoint);
        readerCoordinates.add(readerCoords[0]);
        readerCoordinates.add(readerCoords[1]);

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
