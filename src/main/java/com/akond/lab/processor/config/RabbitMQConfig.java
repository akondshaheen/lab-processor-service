package com.akond.lab.processor.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration class.
 *
 * @Configuration marks this class as a source of bean definitions.
 * It tells Spring to scan this class for methods annotated with @Bean.
 *
 * The class itself (RabbitConfig) is registered as one Spring bean
 * because @Configuration is meta-annotated with @Component.
 *
 * @Configuration does NOT create exchange/queue/binding beans directly.
 * Those beans are created from the methods annotated with @Bean.
 *
 * In this class:
 * - RabbitConfig → 1 bean (the configuration class itself)
 * - Each @Bean method → 1 separate bean in the application context
 *
 * When the application starts, Spring creates:
 * - A TopicExchange bean
 * - A Queue bean
 * - A Binding bean
 *
 * These beans define the RabbitMQ exchange, queue, and their binding.
 */
@Configuration
public class    RabbitMQConfig {

    /**
     * Converts Java objects (POJOs) to JSON before sending to RabbitMQ,
     * and converts JSON back to Java objects when receiving.
     * Uses Jackson (JSON library) instead of Java binary serialization.
     **/
    @Bean
    public MessageConverter jacksonMessageConverter() {
        return new org.springframework.amqp.support.converter.JacksonJsonMessageConverter();
    }

    /**
     * RabbitTemplate is the main class used to send messages to RabbitMQ.
     * Here we inject:
     - ConnectionFactory → manages the TCP connection to RabbitMQ
     - MessageConverter → tells RabbitTemplate to use JSON conversion
     * Without setting the converter, Spring would use SimpleMessageConverter,
     * which only supports String, byte[], or Serializable objects.
     **/
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf,
                                         MessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(converter);
        return template;
    }
}
