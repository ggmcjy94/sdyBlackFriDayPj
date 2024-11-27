package com.example.PaymentService.service;

import blackfriday.protobuf.EdaMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class EventListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    PaymentService paymentService;

    @Autowired
    private KafkaTemplate<String , byte[]> kafkaTemplate;

    @KafkaListener(topics = "payment_request")
    public void consumePaymentRequest(byte[] message) throws Exception {
        var object = EdaMessage.PaymentRequestV1.parseFrom(message);
        log.info("[payment_request : {}]", object);

        var payment = paymentService.processPayment(
                object.getUserId(),
                object.getOrderId(),
                object.getAmountKRW(),
                object.getPaymentMethodId()
        );
        var paymentResultMessage = EdaMessage.PaymentResultV1.newBuilder()
                .setOrderId(payment.orderId)
                .setPaymentId(payment.id)
                .setPaymentStatus(payment.paymentStatus.toString())
                .build();
        kafkaTemplate.send("payment_result", paymentResultMessage.toByteArray());

    }

}
