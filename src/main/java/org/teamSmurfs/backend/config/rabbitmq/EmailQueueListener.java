package org.teamSmurfs.backend.config.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.teamSmurfs.backend.api.allocation.dto.EmailRequest;
import org.teamSmurfs.backend.config.service.MailService;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailQueueListener {

    private final MailService mailService;

    @RabbitListener(queues = "allocationEmailQueue")
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

    @RabbitListener(queues = "userCreationEmailQueue")
    public void processEmail(Map<String, String> message) {
        String email = message.get("email");
        String password = message.get("password");

        try {
            log.info("Processing email sending for: {}", email);
            mailService.sendUserCredentialsEmail(email, password);
            log.info("Email successfully sent to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", email, e);
        }
    }
}
