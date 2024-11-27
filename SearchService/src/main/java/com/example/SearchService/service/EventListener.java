package com.example.SearchService.service;

import blackfriday.protobuf.EdaMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;



@Component
public class EventListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SearchService searchService;


    @KafkaListener(topics = "product_tags_added")
    public void consumeTagAdded(byte[] message) throws InvalidProtocolBufferException {
        var object = EdaMessage.ProductTags.parseFrom(message);
        log.info("[product_tags_added : {}]", object);

        searchService.addTagCache(object.getProductId(), object.getTagsList());
    }


    @KafkaListener(topics = "product_tags_removed")
    public void consumeTagRemoved(byte[] message) throws InvalidProtocolBufferException {
        var object = EdaMessage.ProductTags.parseFrom(message);
        log.info("[product_tags_removed : {}]", object);

        searchService.removeTagCache(object.getProductId(), object.getTagsList());
    }

}
