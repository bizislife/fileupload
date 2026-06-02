package com.example.fileupload.configure;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXTERNAL_EMAIL_QUEUE = "external.email.queue";
    public static final String EXTERNAL_EMAIL_FANOUT_EXCHANGE = "external.email.fanout.exchange";

    // --- External Email: Fanout Exchange ---
    @Bean
    public Queue externalEmailQueue() {
        return new Queue(EXTERNAL_EMAIL_QUEUE, true);
    }

    @Bean
    public FanoutExchange externalEmailFanoutExchange() {
        return new FanoutExchange(EXTERNAL_EMAIL_FANOUT_EXCHANGE);
    }

    @Bean
    public Binding externalEmailBinding(Queue externalEmailQueue, FanoutExchange externalEmailFanoutExchange) {
        return BindingBuilder.bind(externalEmailQueue).to(externalEmailFanoutExchange);
    }

    // --- JSON message converter ---
    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    // --- RabbitTemplate with JSON converter ---
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jacksonJsonMessageConverter());
        return rabbitTemplate;
    }

    // --- Listener container factory with JSON converter ---
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonJsonMessageConverter());
        return factory;
    }

}
