package com.github.frederik174.properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProperties extends Properties {
    // Default constructor
    public KafkaProperties(){
        // KafkaProducer Configurations
        this.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        this.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        this.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        this.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        this.setProperty(ProducerConfig.RETRIES_CONFIG, "3");
        this.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1"); // send asap
        // Exactly once semantics
        this.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,"true");
    }
}
