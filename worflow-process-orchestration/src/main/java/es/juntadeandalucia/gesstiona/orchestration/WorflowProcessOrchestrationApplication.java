package es.juntadeandalucia.gesstiona.orchestration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WorflowProcessOrchestrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorflowProcessOrchestrationApplication.class, args);
	}

}
