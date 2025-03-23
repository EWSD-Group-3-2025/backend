package org.teamSmurfs.backend.config.rabbitmq;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.teamSmurfs.backend.api.allocation.dto.EmailRequest;
import org.teamSmurfs.backend.api.allocation.model.Allocation;
import org.teamSmurfs.backend.api.user.model.Student;
import org.teamSmurfs.backend.api.user.model.Tutor;
import org.teamSmurfs.backend.config.service.MailService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @RabbitListener(queues = "eventCreationEmailQueue")
    public void eventCreationEmail(Map<String, Object> message) {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Allocation> allocations = objectMapper.convertValue(message.get("allocations"),
                    new TypeReference<>() {
                    });
            Tutor tutor = objectMapper.convertValue(message.get("tutor"), Tutor.class);

            String tutorName = tutor.getUser().getName();

            String studentNames = allocations.stream()
                    .map(allocation -> allocation.getStudent().getUser().getName())
                    .collect(Collectors.joining(", "));

            mailService.sendEventEmail(tutor.getUser().getEmail(), "TUTOR", tutorName, studentNames);

            for (Allocation allocation : allocations) {
                Student student = allocation.getStudent();
                mailService.sendEventEmail(student.getUser().getEmail(), "STUDENT", tutorName, student.getUser().getName());
            }
        } catch (Exception e) {
            log.error("Error processing eventCreationEmailQueue: ", e);
        }
    }
}
