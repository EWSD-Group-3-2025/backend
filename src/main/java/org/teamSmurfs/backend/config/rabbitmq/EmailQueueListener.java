package org.teamSmurfs.backend.config.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.teamSmurfs.backend.api.allocation.dto.EmailRequest;
import org.teamSmurfs.backend.config.service.MailService;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailQueueListener {

    private final MailService mailService;

    @RabbitListener(queues = "emailQueue")
    public void processEmailRequest(EmailRequest emailRequest) {
        log.info("Processing email for {} ({})", emailRequest.getRecipientEmail(), emailRequest.getRecipientType());

        mailService.sendAllocationEmail(
                emailRequest.getRecipientEmail(),
                emailRequest.getRecipientType(),
                emailRequest.getTutorName(),
                emailRequest.getStudentName()
        );

        log.info("Email sent successfully to {}", emailRequest.getRecipientEmail());
    }
}
