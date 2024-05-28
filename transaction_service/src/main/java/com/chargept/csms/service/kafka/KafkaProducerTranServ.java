package main.java.com.chargept.csms.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.chargept.csms.model.request.AuthenticationRequest;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerTranServ {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void publishMessage(@RequestBody AuthenticationRequest message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            ProducerRecord<String, String> record = new ProducerRecord<>("csms_auth_request", message.getStationUuid().toString(), jsonMessage);
            kafkaTemplate.send(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
