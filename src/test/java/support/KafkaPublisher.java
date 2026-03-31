package support;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaPublisher {
    private final KafkaProducer<String, String> producer;

    public KafkaPublisher(String bootstrapServers) {
        Properties properties = new Properties();

        // Kafka broker address
        properties.put("bootstrap.servers", bootstrapServers);

        // Serializers for key and value (String-based messages)
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // Ensure message delivery reliability
        properties.put("acks", "all");

        // Retry sending message up to 3 times in case of failure
        properties.put("retries", "3");

        this.producer = new KafkaProducer<>(properties);
    }

    // Publishes a message to a given Kafka topic
    public void publish(String topic, String key, String payload) {
        try {
            // Create Kafka record with topic, key, and message payload
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, payload);

            // Send message synchronously (wait for acknowledgment)
            producer.send(record).get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish booking request to Kafka topic: " + topic, e);
        }
    }

    public void close() {
        producer.close();
    }
}