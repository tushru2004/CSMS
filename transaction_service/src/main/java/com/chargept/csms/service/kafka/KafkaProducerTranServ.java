package main.java.com.chargept.csms.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.chargept.csms.model.request.AuthenticationRequest;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
/**
 * Class for publishing authentication request messages to Kafka for the authentication service.
 */
@Service
public class KafkaProducerTranServ {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    /**
     * Publishes an authentication request message to the Kafka topic csms_auth_request.
     *
     * @param message the authentication request containing user driver identifier and station uuid
     */
    public void publishMessage(@RequestBody AuthenticationRequest message) {
        try {
            // Convert the authentication request message to JSON
            String jsonMessage = objectMapper.writeValueAsString(message);
            // Create a producer record with the topic, key, and value
            ProducerRecord<String, String> record = new ProducerRecord<>("csms_auth_request", message.getStationUuid().toString(), jsonMessage);
            // Send the record to the Kafka topic csms_auth_request
            kafkaTemplate.send(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
