package main.java.com.chargept.csms.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import main.java.com.chargept.csms.model.response.AuthenticationResponse;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

/**
 * Service for producing authentication response messages to Kafka.
 * Publishes response messages to csms_auth_response
 */
@Service
public class KafkaProducer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Setter
    @Getter
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Value("${spring.kafka.auth.response.topic}")
    private String authResponseTopic;

    /**
     * Publishes an authentication response message to the Kafka topic csms_auth_response.
     *
     * @param message   the authentication response message to publish
     * @param stationID the station ID to use as the key for the message
     */
    public void publishMessage(AuthenticationResponse message, String stationID) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            ProducerRecord<String, String> record = new ProducerRecord<>(authResponseTopic, stationID, jsonMessage);
            kafkaTemplate.send(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
