package com.mate.helgather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class HelGatherApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelGatherApplication.class, args);
	}

}
