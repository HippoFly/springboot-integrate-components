package com.example.springbootintegratekafka.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "test-topic")
    public void listen(String message) {
        System.out.println("Received Message: " + message);
    }
}