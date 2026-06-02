package com.example.fileupload.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.example.fileupload.configure.RabbitMqConfig;
import com.example.fileupload.model.EmailMessage;

@Service
public class MessageQueueConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(MessageQueueConsumerService.class);

    @RabbitListener(queues = RabbitMqConfig.EXTERNAL_EMAIL_QUEUE)
    public void consumeExternalEmailMessage(EmailMessage message) {
        logger.info("[MQ Consumer] Received External Email message: {}", message);
    }

}
