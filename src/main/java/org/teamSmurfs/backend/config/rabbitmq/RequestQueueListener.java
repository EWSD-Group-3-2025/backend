package org.teamSmurfs.backend.config.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
class RequestQueueListener {

    private static final Logger logger = LoggerFactory.getLogger(RequestQueueListener.class);

    @RabbitListener(queues = "requestQueue")
    public void processRequestLog(String message) {
        logger.info("Processed from queue: {}", message);
    }
}