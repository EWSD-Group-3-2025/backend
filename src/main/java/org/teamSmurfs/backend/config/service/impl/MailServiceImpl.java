/*
 * @Author : Thant Htoo Aung
 * @Date : 02/07/2025
 * @Time : 02:15 AM (UTC)
 */
package org.teamSmurfs.backend.config.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.visit_log.repository.VisitLogRepository;
import org.teamSmurfs.backend.config.service.MailService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    private final VisitLogRepository visitLogRepository;

    @Override
    public void sendMail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("teamSmurfs");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
            log.info("Email successfully sent to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    @Override
    public void sendUserCredentialsEmail(String email, String password) {
        try {
            String emailBody = this.buildUserCredentialsEmailBody(email, password);
            this.sendMail(email, "Welcome to Team Smurfs E-Tutoring - Login Credentials", emailBody);
        } catch (Exception e) {
            log.error("Failed to send credentials email: {}", e.getMessage());
            throw new RuntimeException("Failed to send user credentials email", e);
        }
    }

    @Override
    public void sendAllocationEmail(String email, String role, String tutorName, String studentName) {
        try {
            String subject = "New Allocation Notification - Team Smurfs E-Tutoring";
            String emailBody = this.buildAllocationEmailBody(role, tutorName, studentName);
            this.sendMail(email, subject, emailBody);
        } catch (Exception e) {
            log.error("Failed to send allocation email: {}", e.getMessage());
            throw new RuntimeException("Failed to send allocation email", e);
        }
    }

    private String buildAllocationEmailBody(String role, String tutorName, String studentName) {
        String roleSpecificContent = role.equals("TUTOR")
                ? String.format("You have been allocated the following student(s): %s.", studentName)
                : String.format("You have been allocated to a new tutor: %s.", tutorName);

        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>New Allocation Notification - Team Smurfs E-Tutoring</title>
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333333; background-color: #f4f4f4; padding: 0; margin: 0;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px; overflow: hidden;">
                    <tr>
                        <td style="background-color: #3b82f6; text-align: center; padding: 30px 20px; color: #ffffff;">
                            <h1 style="margin: 0; font-size: 24px;">New Allocation Notification</h1>
                        </td>
                    </tr>
                    <tr>
                        <td style="padding: 30px 20px;">
                            <p>Hello,</p>
                            <p>We are writing to inform you about a new allocation in the Team Smurfs E-Tutoring System.</p>

                            <p style="background-color: #f0f9ff; padding: 15px; border-left: 4px solid #3b82f6; border-radius: 4px;">
                                <strong>%s</strong>
                            </p>

                            <p>This allocation is effective immediately. Please log in to your account to view more details and get started with your tutoring session.</p>

                            <div style="text-align: center; margin: 30px 0;">
                                <a href="https://ewsd-frontend-app.vercel.app/login" 
                                   style="display: inline-block; padding: 12px 24px; background-color: #3b82f6; color: #ffffff; text-decoration: none; border-radius: 6px;">
                                    Login to Your Account
                                </a>
                            </div>

                            <p>If you have any questions or concerns about this allocation, please don't hesitate to contact our support team.</p>
                            <p>Best regards,<br><strong>Team Smurfs E-Tutoring System</strong></p>
                        </td>
                    </tr>
                    <tr>
                        <td style="background-color: #f8f9fa; text-align: center; padding: 20px; font-size: 12px; color: #666666;">
                            <p>This is an automated message, please do not reply.</p>
                            <p>© 2025 Team Smurfs E-Tutoring System. All rights reserved.</p>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
        """, roleSpecificContent);
    }

    private String buildUserCredentialsEmailBody(String email, String password) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Welcome to Team Smurfs E-Tutoring System</title>
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333333; background-color: #f4f4f4; padding: 0; margin: 0;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px; overflow: hidden;">
                    <tr>
                        <td style="background-color: #3b82f6; text-align: center; padding: 30px 20px; color: #ffffff;">
                            <h1 style="margin: 0; font-size: 24px;">Welcome to Team Smurfs E-Tutoring System</h1>
                        </td>
                    </tr>
                    <tr>
                        <td style="padding: 30px 20px;">
                            <p>Hello,</p>
                            <p>Your account has been successfully created. Below are your login credentials:</p>

                            <table width="100%%" cellpadding="10" cellspacing="0" style="background-color: #f8f9fa; border-radius: 8px; text-align: left;">
                                <tr>
                                    <td><strong>Email:</strong> %s</td>
                                </tr>
                                <tr>
                                    <td><strong>Temporary Password:</strong> %s</td>
                                </tr>
                            </table>

                            <p style="background-color: #fff7ed; padding: 15px; border-left: 4px solid #fbbf24; border-radius: 4px;">
                                <strong>🔐 Important Security Notice:</strong> For your security, please change your password immediately after your first login.
                            </p>

                            <div style="text-align: center; margin: 30px 0;">
                                <a href="https://ewsd-frontend-app.vercel.app/login" 
                                   style="display: inline-block; padding: 12px 24px; background-color: #3b82f6; color: #ffffff; text-decoration: none; border-radius: 6px;">
                                    Login to Your Account
                                </a>
                            </div>

                            <p>If you have any issues accessing your account, feel free to reach out to our support team.</p>
                            <p>Best regards,<br><strong>Team Smurfs E-Tutoring System</strong></p>
                        </td>
                    </tr>
                    <tr>
                        <td style="background-color: #f8f9fa; text-align: center; padding: 20px; font-size: 12px; color: #666666;">
                            <p>This is an automated message, please do not reply.</p>
                            <p>© 2025 Team Smurfs E-Tutoring System. All rights reserved.</p>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
        """, email, password);
    }

    @Scheduled(cron = "0 24 00 * * ?")  // Runs daily at night 12AM - you can adjust
    public void notifyInactiveUsers() {
        LocalDateTime cutoffDate = LocalDateTime.now().minus(28, ChronoUnit.DAYS);
        List<User> inactiveUsers = visitLogRepository.findInactiveUsers(cutoffDate);

        try {
            String subject = "Notify that You are not active - Team Smurfs E-Tutoring";
            String emailBody = this.buildNotifyInactiveUsersBody();
            for (User user : inactiveUsers) {
                this.sendMail(user.getEmail(), subject, emailBody);
            }

            log.info("Inactive user notification process completed. {} users notified.", inactiveUsers.size());

        } catch (Exception e) {
            log.error("Failed to send Notify Inactive Users email: {}", e.getMessage());
            throw new RuntimeException("Failed to send inactive users notify email", e);
        }

    }

    private String buildNotifyInactiveUsersBody() {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Inactive User Notification - Team Smurfs E-Tutoring</title>
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333333; background-color: #f4f4f4; padding: 0; margin: 0;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px; overflow: hidden;">
                    <tr>
                        <td style="background-color: #3b82f6; text-align: center; padding: 30px 20px; color: #ffffff;">
                            <h1 style="margin: 0; font-size: 24px;">New Inactive User Notification</h1>
                        </td>
                    </tr>
                    <tr>
                        <td style="padding: 30px 20px;">
                            <p>Hello,</p>
                            <p>We are writing to inform that you are not active within last 28 days in the Team Smurfs E-Tutoring System.</p>

                            <p style="background-color: #f0f9ff; padding: 15px; border-left: 4px solid #3b82f6; border-radius: 4px;">
                                <strong></strong>
                            </p>

                            <p>Please log in to your account</p>

                            <div style="text-align: center; margin: 30px 0;">
                                <a href="https://ewsd-frontend-app.vercel.app/login" 
                                   style="display: inline-block; padding: 12px 24px; background-color: #3b82f6; color: #ffffff; text-decoration: none; border-radius: 6px;">
                                    Login to Your Account
                                </a>
                            </div>

                            <p>If you have any questions or concerns about this notifying, please don't hesitate to contact our support team.</p>
                            <p>Best regards,<br><strong>Team Smurfs E-Tutoring System</strong></p>
                        </td>
                    </tr>
                    <tr>
                        <td style="background-color: #f8f9fa; text-align: center; padding: 20px; font-size: 12px; color: #666666;">
                            <p>This is an automated message, please do not reply.</p>
                            <p>© 2025 Team Smurfs E-Tutoring System. All rights reserved.</p>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
        """);
    }

	@Override
	public void sendEventEmail(String email, String role, String tutorName, String studentName) {
		 try {
	            String subject = "New Event Notification - Team Smurfs E-Tutoring";
	            String emailBody = this.EventEmailBody(role, tutorName, studentName);
	            this.sendMail(email, subject, emailBody);
	        } catch (Exception e) {
	            log.error("Failed to send event email: {}", e.getMessage());
	            throw new RuntimeException("Failed to send event email", e);
	        }	
	}
	
	 private String EventEmailBody(String role, String tutorName, String studentName) {
	        String roleSpecificContent = role.equals("TUTOR")
	                ? String.format("You have been assigned to the following student(s) for the event: %s.", studentName)
	                : String.format("You have been assigned to a new tutor for the event: %s.", tutorName);

	        return String.format("""
	            <!DOCTYPE html>
	            <html>
	            <head>
	                <meta charset="utf-8">
	                <meta name="viewport" content="width=device-width, initial-scale=1.0">
	                <title>New Event Notification - Team Smurfs E-Tutoring</title>
	            </head>
	            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333333; background-color: #f4f4f4; padding: 0; margin: 0;">
	                <table width="100%%" cellpadding="0" cellspacing="0" style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px; overflow: hidden;">
	                    <tr>
	                        <td style="background-color: #3b82f6; text-align: center; padding: 30px 20px; color: #ffffff;">
	                            <h1 style="margin: 0; font-size: 24px;">New Event Notification</h1>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td style="padding: 30px 20px;">
	                            <p>Hello,</p>
	                            <p>We are writing to inform you about a new event in the Team Smurfs E-Tutoring System.</p>

	                            <p style="background-color: #f0f9ff; padding: 15px; border-left: 4px solid #3b82f6; border-radius: 4px;">
	                                <strong>%s</strong>
	                            </p>

	                            <p>This event is effective immediately. Please log in to your account to view more details and get started with your tutoring session.</p>

	                            <div style="text-align: center; margin: 30px 0;">
	                                <a href="https://ewsd-frontend-app.vercel.app/login" 
	                                   style="display: inline-block; padding: 12px 24px; background-color: #3b82f6; color: #ffffff; text-decoration: none; border-radius: 6px;">
	                                    Login to Your Account
	                                </a>
	                            </div>

	                            <p>If you have any questions or concerns about this event, please don't hesitate to contact our support team.</p>
	                            <p>Best regards,<br><strong>Team Smurfs E-Tutoring System</strong></p>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td style="background-color: #f8f9fa; text-align: center; padding: 20px; font-size: 12px; color: #666666;">
	                            <p>This is an automated message, please do not reply.</p>
	                            <p>© 2025 Team Smurfs E-Tutoring System. All rights reserved.</p>
	                        </td>
	                    </tr>
	                </table>
	            </body>
	            </html>
	        """, roleSpecificContent);
	    }
}