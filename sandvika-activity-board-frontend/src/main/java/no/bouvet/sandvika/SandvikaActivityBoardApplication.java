package no.bouvet.sandvika;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@EnableScheduling
@SpringBootApplication
public class SandvikaActivityBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SandvikaActivityBoardApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplateService(RestTemplateBuilder builder) {
		return builder.build();
	}
}
