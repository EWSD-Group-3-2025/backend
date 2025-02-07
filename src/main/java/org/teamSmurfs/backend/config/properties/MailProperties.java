/*
 * @Author : Thant Htoo Aung
 * @Date : 02/07/2025
 * @Time : 02:18 AM (UTC)
 */
package org.teamSmurfs.backend.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "mail")
public class MailProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private boolean debug;
    private boolean auth;
    private boolean starttlsEnable;
}
