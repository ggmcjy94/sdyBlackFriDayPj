package com.example.SearchService.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

    @KafkaListener(topics = "product_tags_adde")
    public void consumeTagAdded(byte[] message) {

    }

}
