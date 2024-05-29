package main.java.com.chargept.csms.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
/**
 * Class for consuming authentication response messages from Kafka sent by the authentication service.
 * Listens to csms_auth_response topic
 */
@Service
public class KafkaConsumerTransServ {
    private CompletableFuture<String> messageFuture;
    /**
     * Kafka listener for the "csms_auth_response" topic. This method is called
     * when a message is received from the authentication service.
     *
     * @param record the Kafka consumer record containing the authentication response message
     */
    @KafkaListener(topics = "csms_auth_response")
    public void listenToPartition(ConsumerRecord<String, String> record) {
        // Complete the future with the message value when the message is received
        if (messageFuture != null) {
            messageFuture.complete(record.value());
        }
    }
    /**
     * Fetches a single message from the "csms_auth_response" topic.
     * This waits for the message to arrive at the csms_auth_response topic.
     * @return a CompletableFuture that will be completed with the authentication response message
     */
    public CompletableFuture<String> fetchMessage() {
        // Initialize the CompletableFuture to hold the incoming message
        messageFuture = new CompletableFuture<>();
        return messageFuture;
    }
}