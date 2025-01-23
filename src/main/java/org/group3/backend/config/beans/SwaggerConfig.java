/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 11:11 AM
 */
package org.group3.backend.config.beans;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port}")
    private String port;

    @Value("${api.base.path}")
    private String apiBasePath;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${application.initial-name}")
    private String applicationInitialName;

    @Value("${application.contact.info}")
    private String applicationContactInfo;

    @Value("${application.version}")
    private String applicationVersion;

    @Value("${application.host.url}")
    private String applicationHostUrl;

    @Value("${server.description}")
    private String applicationDescription;

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl(applicationHostUrl +":" + port + "/" + apiBasePath);
        server.setDescription(applicationDescription);

        Contact myContact = new Contact();
        myContact.setName(applicationInitialName);
        myContact.setEmail(applicationContactInfo);

        Info information =
                new Info()
                        .title(applicationName + " API")
                        .version(applicationVersion)
                        .description("This API exposes endpoints to manage " + applicationName)
                        .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}