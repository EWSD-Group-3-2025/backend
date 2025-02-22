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
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.config.service.MailService;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

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
}