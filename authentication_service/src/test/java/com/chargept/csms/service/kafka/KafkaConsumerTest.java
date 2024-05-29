package test.java.com.chargept.csms.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.chargept.csms.model.response.AuthenticationRequest;
import main.java.com.chargept.csms.model.response.AuthenticationResponse;
import main.java.com.chargept.csms.service.kafka.KafkaConsumer;
import main.java.com.chargept.csms.service.kafka.KafkaProducer;
import main.java.com.chargept.csms.util.Whitelist;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KafkaConsumerTest {

    @Mock
    private KafkaProducer kafkaProducerService;

    @Mock
    private Whitelist whitelist;

    @InjectMocks
    private KafkaConsumer kafkaConsumer;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        kafkaConsumer = new KafkaConsumer();
        kafkaConsumer.setKafkaProducerService(kafkaProducerService);
    }

    @Test
    public void testListenToPartition_ValidDriverIdentifier_Accepted() throws IOException {
        // Arrange
        String validJson = "{\"driverIdentifier\":{\"id\":\"V4Lk7C9QzN2tMh0GJwXcR3u1pBv5YsWnK8j3T2\"},\"stationUuid\":\"3f50c1b8-2d56-4f30-9e59-7db3c1e7f123\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("csms_auth_request", 0, 0L, "key", validJson);

        AuthenticationRequest authRequest = objectMapper.readValue(validJson, AuthenticationRequest.class);

        // Act
        kafkaConsumer.listenToPartition(record);

        // Assert
        ArgumentCaptor<AuthenticationResponse> authResponseCaptor = ArgumentCaptor.forClass(AuthenticationResponse.class);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        verify(kafkaProducerService, times(1)).publishMessage(authResponseCaptor.capture(), stringCaptor.capture());

        AuthenticationResponse authResponse = authResponseCaptor.getValue();
        assertEquals("Accepted", authResponse.getAuthorizationStatus());
        assertEquals(authRequest.getStationUuid().toString(), stringCaptor.getValue());
    }

    @Test
    public void testListenToPartition_InvalidDriverIdentifier_Invalid() throws IOException {
        // Arrange
        String invalidJson = "{\"driverIdentifier\":{\"id\":\"short\"},\"stationUuid\":\"3f50c1b8-2d56-4f30-9e59-7db3c1e7f123\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("csms_auth_request", 0, 0L, "key", invalidJson);

        AuthenticationRequest authRequest = objectMapper.readValue(invalidJson, AuthenticationRequest.class);

        // Act
        kafkaConsumer.listenToPartition(record);

        // Assert
        ArgumentCaptor<AuthenticationResponse> authResponseCaptor = ArgumentCaptor.forClass(AuthenticationResponse.class);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        verify(kafkaProducerService, times(1)).publishMessage(authResponseCaptor.capture(), stringCaptor.capture());

        AuthenticationResponse authResponse = authResponseCaptor.getValue();
        assertEquals("Invalid", authResponse.getAuthorizationStatus());
        assertEquals(authRequest.getStationUuid().toString(), stringCaptor.getValue());
    }

    @Test
    public void testListenToPartition_UnknownDriverIdentifier_Unknown() throws IOException {
        // Arrange
        String unknownJson = "{\"driverIdentifier\":{\"id\":\"unknown-driver-identifier\"},\"stationUuid\":\"3f50c1b8-2d56-4f30-9e59-7db3c1e7f123\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("csms_auth_request", 0, 0L, "key", unknownJson);

        AuthenticationRequest authRequest = objectMapper.readValue(unknownJson, AuthenticationRequest.class);
        // Act
        kafkaConsumer.listenToPartition(record);

        // Assert
        ArgumentCaptor<AuthenticationResponse> authResponseCaptor = ArgumentCaptor.forClass(AuthenticationResponse.class);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        verify(kafkaProducerService, times(1)).publishMessage(authResponseCaptor.capture(), stringCaptor.capture());

        AuthenticationResponse authResponse = authResponseCaptor.getValue();
        assertEquals("Unknown", authResponse.getAuthorizationStatus());
        assertEquals(authRequest.getStationUuid().toString(), stringCaptor.getValue());
    }

    @Test
    public void testListenToPartition_ValidDriverIdentifier_Rejected() throws IOException {
        // Arrange
        String validJson = "{\"driverIdentifier\":{\"id\":\"xY4B8zP0mN5qKdL2Hf9Z\"},\"stationUuid\":\"3f50c1b8-2d56-4f30-9e59-7db3c1e7f123\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("csms_auth_request", 0, 0L, "key", validJson);

        AuthenticationRequest authRequest = objectMapper.readValue(validJson, AuthenticationRequest.class);

        // Act
        kafkaConsumer.listenToPartition(record);

        // Assert
        ArgumentCaptor<AuthenticationResponse> authResponseCaptor = ArgumentCaptor.forClass(AuthenticationResponse.class);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        verify(kafkaProducerService, times(1)).publishMessage(authResponseCaptor.capture(), stringCaptor.capture());

        AuthenticationResponse authResponse = authResponseCaptor.getValue();
        assertEquals("Rejected", authResponse.getAuthorizationStatus());
        assertEquals(authRequest.getStationUuid().toString(), stringCaptor.getValue());
    }
}
