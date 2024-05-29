package test.java.com.chargept.csms.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.chargept.csms.model.response.AuthenticationResponse;
import main.java.com.chargept.csms.service.kafka.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaProducer kafkaProducer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        kafkaProducer.setKafkaTemplate(kafkaTemplate);
    }

    @Test
    public void testPublishMessage() throws Exception {
        // Arrange
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setAuthorizationStatus("Accepted");
        String stationID = "1234-5678-9012";
        String expectedJsonMessage = objectMapper.writeValueAsString(authResponse);

        // Act
        kafkaProducer.publishMessage(authResponse, stationID);

        // Assert
        ArgumentCaptor<ProducerRecord<String, String>> recordCaptor = ArgumentCaptor.forClass(ProducerRecord.class);
        verify(kafkaTemplate, times(1)).send(recordCaptor.capture());

        ProducerRecord<String, String> actualRecord = recordCaptor.getValue();
        assertEquals("csms_auth_response", actualRecord.topic());
        assertEquals(stationID, actualRecord.key());
        assertEquals(expectedJsonMessage, actualRecord.value());
    }
}
