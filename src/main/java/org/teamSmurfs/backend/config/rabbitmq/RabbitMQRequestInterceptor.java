package org.teamSmurfs.backend.config.rabbitmq;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class RabbitMQRequestInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQRequestInterceptor.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String QUEUE_NAME = "requestQueue";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String logMessage = "Incoming request: " + request.getMethod() + " " + request.getRequestURI();
        logger.info(logMessage);
        rabbitTemplate.convertAndSend(QUEUE_NAME, logMessage);
        return true; // Continue processing
    }
}
