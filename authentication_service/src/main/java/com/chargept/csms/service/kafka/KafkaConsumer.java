package main.java.com.chargept.csms.service.kafka;

import main.java.com.chargept.csms.service.kafka.KafkaProducer;
import com.chargept.csms.util.Whitelist;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.chargept.csms.model.response.AuthenticationRequest;
import main.java.com.chargept.csms.model.response.AuthenticationResponse;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class KafkaConsumer {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private KafkaProducer kafkaProducerService;

    @KafkaListener(topics = "csms_auth_request")
    public void listenToPartition(ConsumerRecord<String, String> record) {
        System.out.println("Received message: " + record.value() +
                " with key: " + record.key());

        AuthenticationRequest authMessage = null;
        AuthenticationResponse authRes = new AuthenticationResponse();

        try {
            authMessage = objectMapper.readValue(record.value(), AuthenticationRequest.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (authMessage == null) throw new AssertionError();
        String driverIdentifier = authMessage.getDriverIdentifier().getId();
        //Driver identifier is between 20 and 80
        if(driverIdentifier.length() < 20 || driverIdentifier.length() > 80)
        {
            authRes.setAuthorizationStatus("Invalid");
        }
        else {
            Whitelist wl = new Whitelist();
            boolean wlDbCheck = wl.check(driverIdentifier);
            // Check the whitelist database if it contains identifier in the system
            if (wlDbCheck) {
                boolean cardWorks = wl.checkCardChargeStatus(driverIdentifier);
                if (cardWorks) {
                    authRes.setAuthorizationStatus("Accepted");
                } else {
                    authRes.setAuthorizationStatus("Rejected");
                }
            } else {
                // identifier does not exist in the system
                authRes.setAuthorizationStatus("Unknown");
            }
        }
        kafkaProducerService.publishMessage(authRes, authMessage.getStationUuid().toString());
    }
}