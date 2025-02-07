package org.teamSmurfs.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.Objects;

@ConfigurationPropertiesScan
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.directory("./")
				.filename(".env")
				.load();
		System.setProperty("SPRING_DATASOURCE_URL", Objects.requireNonNull(dotenv.get("SPRING_DATASOURCE_URL")));
		System.setProperty("SPRING_DATASOURCE_USERNAME", Objects.requireNonNull(dotenv.get("SPRING_DATASOURCE_USERNAME")));
		System.setProperty("SPRING_DATASOURCE_PASSWORD", Objects.requireNonNull(dotenv.get("SPRING_DATASOURCE_PASSWORD")));
		System.setProperty("MAIL_HOST", Objects.requireNonNull(dotenv.get("MAIL_HOST")));
		System.setProperty("MAIL_PORT", Objects.requireNonNull(dotenv.get("MAIL_PORT")));
		System.setProperty("MAIL_USERNAME", Objects.requireNonNull(dotenv.get("MAIL_USERNAME")));
		System.setProperty("MAIL_PASSWORD", Objects.requireNonNull(dotenv.get("MAIL_PASSWORD")));
		System.setProperty("MAIL_DEBUG", Objects.requireNonNull(dotenv.get("MAIL_DEBUG")));
		System.setProperty("MAIL_AUTH", Objects.requireNonNull(dotenv.get("MAIL_AUTH")));
		System.setProperty("MAIL_STARTTLS_ENABLE", Objects.requireNonNull(dotenv.get("MAIL_STARTTLS_ENABLE")));

		SpringApplication.run(BackendApplication.class, args);
	}

}
