package main.java.com.chargept.csms.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.chargept.csms.model.response.AuthenticationResponse;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void publishMessage(AuthenticationResponse message, String stationID) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            ProducerRecord<String, String> record = new ProducerRecord<>("csms_auth_response", stationID, jsonMessage);
            kafkaTemplate.send(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
