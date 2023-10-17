package com.dip.kafka_producer.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SignedMessage {
    private byte[]  employeeId;
    private Employee employee;
    private byte[] signature;
}
