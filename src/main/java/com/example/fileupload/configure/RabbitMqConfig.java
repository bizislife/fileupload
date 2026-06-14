package com.example.fileupload.configure;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXTERNAL_EMAIL_QUEUE = "external.email.queue";

    // // --- These must match FormBuilder's values exactly ---
    // @Value("${spring.rabbitmq.condition-rpc.exchange}")
    // private String exchangeName; // condition.rpc.exchange

    // @Value("${spring.rabbitmq.condition-rpc.request-queue}")
    // private String requestQueueName; // condition.rpc.request.queue

    // @Value("${spring.rabbitmq.condition-rpc.ttl-ms:60000}")
    // private long ttlMs;

    // // --- Queue (durable + TTL, idempotent with FormBuilder's declaration) ---
    // @Bean
    // public Queue conditionRpcRequestQueue() {
    // return QueueBuilder.durable(requestQueueName).ttl((int) ttlMs).build();
    // }

    // // --- Exchange reference (declares if absent, no-op if exists) ---
    // @Bean
    // public FanoutExchange conditionRpcExchange() {
    // return new FanoutExchange(exchangeName);
    // }

    // // --- Binding (idempotent) ---
    // @Bean
    // public Binding conditionRpcBinding() {
    // return BindingBuilder.bind(conditionRpcRequestQueue()).to(conditionRpcExchange());
    // }

    // --- JSON converter (must match FormBuilder) ---
    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    // --- Listener factory with JSON converter ---
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonJsonMessageConverter());
        return factory;
    }

}
