package com.dip.kafka_producer.api;
import com.dip.kafka_producer.dto.Employee;
import com.dip.kafka_producer.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class KafkaClientMessage {

    @Autowired
    KafkaProducerService kafkaProducerService;

    @PostMapping("/produceMessage")
    public ResponseEntity produceKafkaMessage(@RequestBody List<Employee> employee) throws Exception {
        kafkaProducerService.sendMessage(employee);
        return new ResponseEntity("Message send successfully",HttpStatus.OK);
    }
}
