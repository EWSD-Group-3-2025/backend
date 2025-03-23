package org.teamSmurfs.backend.config.rabbitmq;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue requestQueue() {
        return new Queue("requestQueue", false);
    }

    @Bean
    public Queue allocationMailQueue() { return new Queue("allocationEmailQueue", false); }

    @Bean
    public Queue userCreationEmailQueue() { return new Queue("userCreationEmailQueue", false); }

    @Bean
    public Queue eventCreationEmailQueue() { return new Queue("eventCreationEmailQueue", false); }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}