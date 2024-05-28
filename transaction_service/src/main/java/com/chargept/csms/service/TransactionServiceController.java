package main.java.com.chargept.csms.service;

import main.java.com.chargept.csms.service.kafka.KafkaProducerTranServ;
import main.java.com.chargept.csms.service.kafka.KafkaConsumerTransServ;
import com.chargept.csms.model.request.AuthenticationRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("transaction/authorize")
public class TransactionServiceController {

    @Autowired
    private KafkaProducerTranServ kafkaProducer;
    @Autowired
    private KafkaConsumerTransServ kafkaConsumer;

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
