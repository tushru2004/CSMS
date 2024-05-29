package main.java.com.chargept.csms.service;

import main.java.com.chargept.csms.service.kafka.KafkaProducerTranServ;
import main.java.com.chargept.csms.service.kafka.KafkaConsumerTransServ;
import main.java.com.chargept.csms.model.request.AuthenticationRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
/**
 * Rest controller for handling post api call for transaction/authorize endpoint.
 */

@RestController
@RequestMapping("transaction/authorize")
public class TransactionServiceController {

    @Autowired
    private KafkaProducerTranServ kafkaProducer;
    @Autowired
    private KafkaConsumerTransServ kafkaConsumer;
    /**
     * Authorizes a user based on his identifier and value in the card. Logic for authentication is located in the authentication
     * service. This service calls the authentication service to check is the user authorized to charge his card
     *
     * @param transactionRequestModel the authentication request containing user driver identifier and station uuid
     * @return the response message which indicates if the authorization is successful
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public String authorizeUser(@RequestBody AuthenticationRequest transactionRequestModel) {
        kafkaProducer.publishMessage(transactionRequestModel);
        // Fetch a single message
        CompletableFuture<String> futureMessage = kafkaConsumer.fetchMessage();
        String response = "";
        try {
            response = futureMessage.get(); // Block and get the message
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return response;
    }
}
