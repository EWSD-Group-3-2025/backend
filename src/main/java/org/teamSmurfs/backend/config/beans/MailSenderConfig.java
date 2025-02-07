/*
 * @Author : Thant Htoo Aung
 * @Date : 02/07/2025
 * @Time : 2:08 AM (UTC)
 */
package org.teamSmurfs.backend.config.beans;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.teamSmurfs.backend.config.properties.MailProperties;

import java.util.Properties;
import java.util.TimeZone;

@Configuration
@RequiredArgsConstructor
public class MailSenderConfig {

    private final MailProperties mailProperties;

    @Bean
    public JavaMailSender javaMailSender() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Yangon"));

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", mailProperties.isAuth());
        props.put("mail.smtp.starttls.enable", mailProperties.isStarttlsEnable());
        props.put("mail.debug", mailProperties.isDebug());
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.writetimeout", "5000");

        return mailSender;
    }
}
