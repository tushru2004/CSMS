package main.java.com.chargept.csms.service.kafka;

import lombok.Getter;
import lombok.Setter;
import main.java.com.chargept.csms.util.Whitelist;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.chargept.csms.model.response.AuthenticationRequest;
import main.java.com.chargept.csms.model.response.AuthenticationResponse;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
/**
 * Class for consuming authentication request messages from Kafka sent by the transaction service.
 * Listens to csms_auth_request topic
 */
@Component
public class KafkaConsumer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Setter
    @Getter
    @Autowired
    private KafkaProducer kafkaProducerService;
    /**
     * Kafka listener method for consuming messages from "csms_auth_request" topic.
     * Validates the driver identifier in  the received message and publishes a message on kafka on "csms_auth_response" topic.
     *
     * @param record the Kafka consumer record containing the authentication request
     */
    @KafkaListener(topics = "csms_auth_request")
    public void listenToPartition(ConsumerRecord<String, String> record) {
        //System.out.println("Received message: " + record.value() +
        //        " with key: " + record.key());

        AuthenticationRequest authMessage = null;
        AuthenticationResponse authRes = new AuthenticationResponse();
        // Convert json to Authentication Request object
        try {
            authMessage = objectMapper.readValue(record.value(), AuthenticationRequest.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (authMessage == null) throw new AssertionError();

        String driverIdentifier = authMessage.getDriverIdentifier().getId();
        //if driver identifier is between 20 and 80 then is invalid
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
                //Check if the card works
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