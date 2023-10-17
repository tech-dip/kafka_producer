package com.dip.kafka_producer.service;

import com.dip.kafka_producer.dto.Employee;
import com.dip.kafka_producer.dto.SignedMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.security.interfaces.RSAPublicKey;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class KafkaProducerService {

    String userHome = System.getProperty("user.home");
    String PUBLIC_KEY_FILE = userHome + File.separator + "public_key.txt";
    String PRIVATE_KEY_FILE = userHome + File.separator + "private_key.txt";

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    @Value("${kafka.topic}")
    private String topic;

    @Value("${kafka.dummyPublicKey}")
    private byte[] dummyPublicKey;

    public void sendMessage(List<Employee> employee) throws Exception {
        LocalDateTime startSendingTime = LocalDateTime.now();
        System.out.println("sending the message..time " + startSendingTime);
        ObjectMapper objectMapper = new ObjectMapper();
        KeyPair keyPair = generateKeyPair(); // Generate a key pair (public and private key)

        PublicKey publicKeyBytes = keyPair.getPublic();
        PrivateKey privateKeyBytes = keyPair.getPrivate();

        savePublicKey(publicKeyBytes);
        savePrivateKey(privateKeyBytes);

        List<Object> batch = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(1);
        employee.stream().forEach(it -> {
            try {
                byte[] employeeAsByte = objectMapper.writeValueAsBytes(it.getEmpId());
                byte[] signature = SignMessage(employeeAsByte, keyPair.getPrivate()); // Sign the document
                SignedMessage signedMessage = new SignedMessage();
                signedMessage.setEmployeeId(employeeAsByte);
                it.setEmpId(0);
                signedMessage.setEmployee(it);
                signedMessage.setSignature(signature);
                batch.add(signedMessage);

            } catch (Exception e) {
                e.printStackTrace();
            }
            count.getAndIncrement();
        });
        String jsonString = objectMapper.writeValueAsString(batch);
        LocalDateTime endProcessingTime = LocalDateTime.now();
        System.out.println("sending the message.. " + startSendingTime);
        kafkaTemplate.send(topic,jsonString);
        System.out.println("message sent and processed at " + endProcessingTime);

        long durationMillis = Duration.between(startSendingTime, endProcessingTime).toMillis();
        System.out.println("Time taken to send and process the message: " + durationMillis + " milliseconds");
    }

        // Sign a document using a private key
    public static byte[] SignMessage(byte[] message, PrivateKey privateKey) throws Exception {
        System.out.println("Signed the message .. " + LocalDateTime.now());
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message);
        return signature.sign();
    }

    // Generate a key pair
    public KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
    private String savePublicKey(PublicKey publicKey) throws Exception {
        String publicKeyInfo = "Public Key Info:\n" + publicKeyToString(publicKey) + "\nPublic Key File Path: " + PUBLIC_KEY_FILE + "\n";
        Files.write(Path.of(PUBLIC_KEY_FILE), publicKeyInfo.getBytes(), StandardOpenOption.CREATE);
        System.out.println("Public Key saved at: " + PUBLIC_KEY_FILE);
        return PUBLIC_KEY_FILE;
    }

    private String savePrivateKey(PrivateKey privateKey) throws Exception {
        String privateKeyInfo = "Private Key Info:\n" + privateKeyToString(privateKey) + "\nPrivate Key File Path: " + PRIVATE_KEY_FILE + "\n";
        Files.write(Path.of(PRIVATE_KEY_FILE), privateKeyInfo.getBytes(), StandardOpenOption.CREATE);
        System.out.println("Private Key saved at: " + PRIVATE_KEY_FILE);
        return PRIVATE_KEY_FILE;
    }

    private String publicKeyToString(PublicKey publicKey) {
        if (publicKey instanceof RSAPublicKey) {
            RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
            return "  params: " + rsaPublicKey.getParams() + "\n" +
                    "  modulus: " + rsaPublicKey.getModulus() + "\n" +
                    "  public exponent: " + rsaPublicKey.getPublicExponent();
        } else {
            return "Public key is not an instance of RSAPublicKey";
        }
    }

    private String privateKeyToString(PrivateKey privateKey) {
        if (privateKey instanceof RSAPrivateKey) {
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
            return "  params: " + rsaPrivateKey.getParams() + "\n" +
                    "  modulus: " + rsaPrivateKey.getModulus() + "\n" +
                    "  private exponent: " + rsaPrivateKey.getPrivateExponent();
        } else {
            return "Private key is not an instance of RSAPrivateKey";
        }
    }


}
