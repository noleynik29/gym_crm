package com.gym.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.gym.demo.*")
@EnableJpaRepositories(basePackages = "com.gym.demo.repository")
@EntityScan("com.gym.demo.model")
@EnableTransactionManagement
public class GymCrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymCrmApplication.class, args);
	}

}
