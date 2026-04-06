package com.dinosaur.infrastructure.out.messaging;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dinosaur.domain.port.out.DinosaurMessagingPort;


@Component
public class RabbitMQAdapter implements DinosaurMessagingPort {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQAdapter.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    public RabbitMQAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendStatusUpdate(Long dinosaurId, String newStatus, LocalDateTime timestamp) {
        DinosaurStatusUpdateMessage message = new DinosaurStatusUpdateMessage(dinosaurId, newStatus, timestamp);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
        logger.info("Sent status update message for dinosaur {} with status {} at {}", dinosaurId, newStatus, timestamp);
    }


}
