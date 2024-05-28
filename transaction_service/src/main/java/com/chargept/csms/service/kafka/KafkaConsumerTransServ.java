package main.java.com.chargept.csms.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaConsumerTransServ {
    private CompletableFuture<String> messageFuture;
    @KafkaListener(topics = "csms_auth_response")
    public void listenToPartition(ConsumerRecord<String, String> record) {
        // Complete the future with the message value
        if (messageFuture != null) {
            messageFuture.complete(record.value());
        }
    }
    public CompletableFuture<String> fetchMessage() {
        messageFuture = new CompletableFuture<>();
        return messageFuture;
    }
}