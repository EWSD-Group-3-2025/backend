/*
 * @Author : Thant Htoo Aung
 * @Date : 02/07/2025
 * @Time : 02:14 AM (UTC)
 */
package org.teamSmurfs.backend.config.service;

public interface MailService {
    void sendMail(String to, String subject, String body);
}
