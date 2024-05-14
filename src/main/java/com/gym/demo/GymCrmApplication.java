package com.gym.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class GymCrmApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(GymCrmApplication.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void openBrowserAfterStartup() throws IOException, URISyntaxException {
		Desktop.getDesktop().browse(new URI("http://localhost:8080"));
	}
}
