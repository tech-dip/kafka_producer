package com.dip.kafka_producer.service;

import com.dip.kafka_producer.dto.Employee;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.kafka.core.KafkaTemplate;

import java.security.KeyPair;
import java.security.PrivateKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KafkaProducerServiceTest {
    @Mock
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    @Test
    void testSendMessage() throws Exception {
//        Employee employee = new Employee();
//        employee.setEmpId(1);
//        employee.setEmp_firstname("John");
//        employee.setEmp_lastname("Doe");
//
//        KeyPair keyPair = kafkaProducerService.generateKeyPair();
//        PrivateKey privateKey = keyPair.getPrivate();
//
//        byte[] mockEncryptedMessage = new byte[10]; // Mock encrypted message
//        when(kafkaProducerService.encryptedMessage(any(), any())).thenReturn(mockEncryptedMessage);
//        when(kafkaProducerService.generateKeyPair()).thenReturn(keyPair);
//
//        kafkaProducerService.sendMessage(employee);
//
//        verify(kafkaTemplate).send(any(ProducerRecord.class));
    }

    @Test
    void encryptedMessage() {
    }

    @Test
    void generateKeyPair() {
    }
}