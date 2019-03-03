package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({ "application.control", "application.service" })
@SpringBootApplication
public class Main {

	public static void main(final String[] args) {
		SpringApplication.run(Main.class, args);
	}

}