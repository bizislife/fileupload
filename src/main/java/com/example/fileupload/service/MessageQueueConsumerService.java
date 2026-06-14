package com.example.fileupload.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.example.fileupload.configure.RabbitMqConfig;
import com.example.fileupload.model.ConditionRpcReply;
import com.example.fileupload.model.ConditionRpcRequest;
import com.example.fileupload.model.EmailMessage;

@Service
public class MessageQueueConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(MessageQueueConsumerService.class);

    @RabbitListener(queues = RabbitMqConfig.EXTERNAL_EMAIL_QUEUE)
    public void consumeExternalEmailMessage(EmailMessage message) {
        logger.info("[MQ Consumer] Received External Email message: {}", message);
    }

    @RabbitListener(queues = "${spring.rabbitmq.condition-rpc.request-queue}")
    public Map<String, String> receiveConditionRpcRequest(ConditionRpcRequest request) {
        logger.info("[MQ Consumer] Received Condition RPC request: requestId={}, conditionType={}, payload={}",
                request.getRequestId(), request.getConditionType(), request.getConditionPayload());
        // Evaluate the condition — for demonstration, return true for known condition
        // types
        boolean result = evaluateCondition(request);
        Map<String, String> reply = Map.of("requestId", request.getRequestId(), "result", Boolean.toString(result),
                "status", "SUCCESS", "timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "processingNode", "formbuilder-service");

        logger.info("[MQ Consumer] Condition evaluated: requestId={}, conditionType={}, result={}",
                request.getRequestId(), request.getConditionType(), result);
        return reply;
    }

    /**
     * Sample condition evaluation logic for RPC-based conditions. For demonstration, test-condition01 always returns
     * true; other types default to false.
     */
    private boolean evaluateCondition(ConditionRpcRequest request) {
        if ("test-condition01".equals(request.getConditionType())) {
            logger.info("[MQ Consumer] test-condition01 evaluated: returning true");
            return true;
        }
        logger.warn("[MQ Consumer] Unknown condition type for RPC evaluation: {}", request.getConditionType());
        return false;
    }

}
