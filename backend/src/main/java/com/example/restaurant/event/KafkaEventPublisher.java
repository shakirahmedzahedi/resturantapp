package com.example.restaurant.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class KafkaEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publish(OrderEvent event) {
        String topic = "ORDER_CREATED".equals(event.eventType())
                ? "restaurant.order.created"
                : "restaurant.order.status-changed";

        kafkaTemplate.send(topic, String.valueOf(event.orderId()), event);
    }
}
