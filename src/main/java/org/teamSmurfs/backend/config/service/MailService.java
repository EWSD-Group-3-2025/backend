/*
 * @Author : Thant Htoo Aung
 * @Date : 02/07/2025
 * @Time : 02:14 AM (UTC)
 */
package org.teamSmurfs.backend.config.service;

import org.teamSmurfs.backend.api.user.model.User;

public interface MailService {
    void sendMail(String to, String subject, String body);

    void sendUserCredentialsEmail(String email, String rawPassword);

    void sendAllocationEmail(String email, String role, String tutorName, String studentName);

    void notifyInactiveUsers();

	void sendEventEmail(String email, String role, String tutorName, String studentName);

    void sendEmailForResetPassword(final String email, final String newPassword);
}