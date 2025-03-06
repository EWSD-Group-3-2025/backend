package org.teamSmurfs.backend.config.rabbitmq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
class RabbitMQConfig {

    @Bean
    public Queue requestQueue() {
        return new Queue("requestQueue", false);
    }
}